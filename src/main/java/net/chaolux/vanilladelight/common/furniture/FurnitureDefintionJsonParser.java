package net.chaolux.vanilladelight.common.furniture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.chaolux.vanilladelight.api.furniture.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.*;

public class FurnitureDefintionJsonParser {
    private FurnitureDefintionJsonParser() {

    }

    public static FurnitureDefintion parse(ResourceLocation id, JsonObject jsonObject) {
        FurnitureDefintionBuilder builder=FurnitureDefintion.builder(id);
        JsonObject materials= GsonHelper.getAsJsonObject(jsonObject,"materials");
        for (Map.Entry<String, JsonElement> entry : materials.entrySet()) builder.material(entry.getKey(),parseMaterial(entry.getValue()));
        JsonArray styles=GsonHelper.getAsJsonArray(jsonObject,"styles");
        for (JsonElement element : styles) {
            if(element.isJsonPrimitive()) {
                builder.style(new StylePresent(element.getAsString(),null,null));
            } else {
                JsonObject style=element.getAsJsonObject();
                builder.style(new StylePresent(GsonHelper.getAsString(style,"id"),location(style,"model"),location(style,"open_model")));
            }
        }
        if(jsonObject.has("default_style")) builder.defaultStyle(GsonHelper.getAsString(jsonObject,"default_style"));
        if(jsonObject.has("container_size")) builder.containerSize(GsonHelper.getAsInt(jsonObject,"container_size"));
        if(jsonObject.has("sections")) {
            for(JsonElement element : GsonHelper.getAsJsonArray(jsonObject,"sections")) {
                JsonObject section=element.getAsJsonObject();
                builder.section(new FurnitureSection(GsonHelper.getAsString(section,"id"),GsonHelper.getAsString(section,"material"),aabb(section),stringSet(section,"styles"),GsonHelper.getAsInt(section,"priority",0)));
            }
        }
        if(jsonObject.has("parts")) {
            for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject,"parts")) {
                JsonObject part=element.getAsJsonObject();
                List<EffectRule> effectRules=new ArrayList<>();
                if(part.has("effects")) {
                    for (JsonElement effect : GsonHelper.getAsJsonArray(part,"effects")) {
                        effectRules.add(parseEffect(effect.getAsJsonObject()));
                    }
                }
                TextureMapping mapping;
                if(part.has("mapping")) {
                    mapping=parseMapping(part.getAsJsonObject("mapping"));
                } else {
                    mapping=TextureMapping.DEFAULT;
                }
                builder.part(new FurniturePart(GsonHelper.getAsString(part,"id"),new ResourceLocation(GsonHelper.getAsString(part,"placeholder")),GsonHelper.getAsString(part,"material"),mapping,effectRules));
            }
        }
        if(jsonObject.has("collision")) {
            for(JsonElement element : GsonHelper.getAsJsonArray(jsonObject,"collision")) {
                builder.collision(aabb(element.getAsJsonObject()));
            }
        }
        return builder.build();
    }

    private static BlockState parseMaterial(JsonElement element) {
        JsonObject object;
        if(element.isJsonPrimitive()) {
            object=new JsonObject();
            object.addProperty("block",element.getAsString());
        } else {
            object=element.getAsJsonObject();
        }
        ResourceLocation id=new ResourceLocation(GsonHelper.getAsString(object,"block"));
        Block block= BuiltInRegistries.BLOCK.getOptional(id).orElseThrow(() -> new JsonParseException("Unknown block: " + id));
        BlockState blockState=block.defaultBlockState();
        if(object.has("properties")) {
            JsonObject properties=object.getAsJsonObject("properties");
            for (Map.Entry<String,JsonElement> entry : properties.entrySet()) {
                Property<?> property=block.getStateDefinition().getProperty(entry.getKey());
                if(property != null) blockState=apply(blockState,property,entry.getValue().getAsString());
            }
        }
        return blockState;
    }

    private static TextureMapping parseMapping(JsonObject jsonObject) {
        TextureMapping.Mode mode=TextureMapping.Mode.valueOf(GsonHelper.getAsString(jsonObject,"mode","preserve_source_faces").toUpperCase());
        float[] crop=floats(jsonObject,"crop",new float[]{0.0f,0.0f,1.0f,1.0f});
        return new TextureMapping(mode,GsonHelper.getAsFloat(jsonObject,"scale_u",1.0f),GsonHelper.getAsFloat(jsonObject,"scale_v",1.0f),GsonHelper.getAsFloat(jsonObject,"offset_u",0.0f),GsonHelper.getAsFloat(jsonObject,"offset_v",0.0f),GsonHelper.getAsInt(jsonObject,"rotation",0),GsonHelper.getAsBoolean(jsonObject,"mirror_u",false),GsonHelper.getAsBoolean(jsonObject,"mirror_v",false),crop[0],crop[1],crop[2],crop[3]);
    }

    private static EffectRule parseEffect(JsonObject jsonObject) {
        return new EffectRule(color(jsonObject.get("tint"),0xFFFFFFFF),GsonHelper.getAsFloat(jsonObject,"shade",1.0f),GsonHelper.getAsFloat(jsonObject,"alpha",1.0f),GsonHelper.getAsBoolean(jsonObject,"emissive",false),location(jsonObject,"overlay"),GsonHelper.getAsFloat(jsonObject,"metallic",0.0f),GsonHelper.getAsFloat(jsonObject,"pulse_speed",0.0f),GsonHelper.getAsFloat(jsonObject,"pulse_min",1.0f),GsonHelper.getAsFloat(jsonObject,"pulse_max",1.0f),GsonHelper.getAsFloat(jsonObject,"scroll_u",0.0f),GsonHelper.getAsFloat(jsonObject,"scroll_v",0.0f));
    }

    private static AABB aabb(JsonObject jsonObject) {
        float[] from=floats(jsonObject,"from",new float[] {
                0.0f,0.0f,0.0f
        });
        float[] to=floats(jsonObject,"to",new float[] {
                16.0f,16.0f,16.0f
        });
        return new AABB(from[0] / 16.0,from[1] / 16.0,from[2] / 16.0,to[0] / 16.0,to[1] / 16.0,to[2] / 16.0);
    }

    private static float[] floats(JsonObject jsonObject, String string,float[] defaultFloat) {
        if(!jsonObject.has(string)) return defaultFloat;
        JsonArray jsonArray=GsonHelper.getAsJsonArray(jsonObject,string);
        float[] values=new float[jsonArray.size()];
        for(int index=0; index < values.length; index++) {
            values[index]=jsonArray.get(index).getAsFloat();
        }
        return values;
    }

    private static Set<String> stringSet(JsonObject jsonObject, String string) {
        Set<String> values=new LinkedHashSet<>();
        if(jsonObject.has(string)) {
            for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject,string)) {
                values.add(element.getAsString());
            }
        }
        return values;
    }

    @Nullable
    private static ResourceLocation location(JsonObject jsonObject, String string) {
        if(!jsonObject.has(string)) return null;
        return new ResourceLocation(GsonHelper.getAsString(jsonObject,string));
    }

    private static int color(@Nullable JsonElement jsonElement, int defaultInt) {
        if(jsonElement == null) return defaultInt;
        if(jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber()) {
            return jsonElement.getAsInt();
        }
        String value=jsonElement.getAsString().replace("#","");
        long parsed=Long.parseLong(value,16);
        if(value.length() <= 6) parsed |= 0xFF000000L;
        return (int) parsed;
    }

    private static <T extends Comparable<T>> BlockState apply(BlockState blockState, Property<T> property, String string) {
        Optional<T> parsed=property.getValue(string);
        return parsed.map(result -> blockState.setValue(property,result)).orElse(blockState);
    }
}
