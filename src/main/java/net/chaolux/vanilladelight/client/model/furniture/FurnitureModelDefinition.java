package net.chaolux.vanilladelight.client.model.furniture;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.chaolux.vanilladelight.api.furniture.EffectRule;
import net.chaolux.vanilladelight.api.furniture.FurniturePart;
import net.chaolux.vanilladelight.api.furniture.TextureMapping;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.*;

public record FurnitureModelDefinition(ResourceLocation resourceLocation, String string, Map<String,FurnitureStyleModels> styleModelsMap, Map<ResourceLocation, FurniturePart> furniturePartMap, Map<String, MaterialState> materialStateMap,Map<ResourceLocation,String> locationStringMap) {
    public FurnitureModelDefinition {
        styleModelsMap= Collections.unmodifiableMap(new LinkedHashMap<>(styleModelsMap));
        furniturePartMap=Collections.unmodifiableMap(new LinkedHashMap<>(furniturePartMap));
        materialStateMap=Collections.unmodifiableMap(new LinkedHashMap<>(materialStateMap));
        locationStringMap=Collections.unmodifiableMap(new LinkedHashMap<>(locationStringMap));
    }

    public static FurnitureModelDefinition fromJson(JsonObject jsonObject) {
        ResourceLocation definition=new ResourceLocation(GsonHelper.getAsString(jsonObject,"definition"));
        String defaultStyle=GsonHelper.getAsString(jsonObject,"default_style");
        Map<String,FurnitureStyleModels> furnitureStyleModelsMap=new LinkedHashMap<>();
        for(Map.Entry<String, JsonElement> entry : GsonHelper.getAsJsonObject(jsonObject,"styles").entrySet()) {
            if(entry.getValue().isJsonPrimitive()) {
                furnitureStyleModelsMap.put(entry.getKey(),new FurnitureStyleModels(new ResourceLocation(entry.getValue().getAsString()),null));
            } else {
                JsonObject object=entry.getValue().getAsJsonObject();
                furnitureStyleModelsMap.put(entry.getKey(),new FurnitureStyleModels(new ResourceLocation(GsonHelper.getAsString(object,"closed")),location(object,"open")));
            }
        }
        Map<String,MaterialState> stateMap=new LinkedHashMap<>();
        for (Map.Entry<String,JsonElement> entry : GsonHelper.getAsJsonObject(jsonObject,"defaults").entrySet()) {
            stateMap.put(entry.getKey(),MaterialState.from(parseState(entry.getValue())));
        }
        Map<ResourceLocation,FurniturePart> partMap=new LinkedHashMap<>();
        for (JsonElement element : GsonHelper.getAsJsonArray(jsonObject,"parts")) {
            JsonObject object=element.getAsJsonObject();
            ResourceLocation location=new ResourceLocation(GsonHelper.getAsString(object,"placeholder"));
            List<EffectRule> effectRules=new ArrayList<>();
            if(object.has("effects")) {
                for (JsonElement jsonElement : GsonHelper.getAsJsonArray(object,"effects")) {
                    effectRules.add(effect(jsonElement.getAsJsonObject()));
                }
            }
            TextureMapping textureMapping=object.has("mapping") ? mapping(object.getAsJsonObject("mapping")) : TextureMapping.DEFAULT;
            FurniturePart furniturePart=new FurniturePart(GsonHelper.getAsString(object,"id"),location,GsonHelper.getAsString(object,"material"),textureMapping,effectRules);
            partMap.put(location,furniturePart);
        }
        Map<ResourceLocation,String> stringMap=new LinkedHashMap<>();
        if(jsonObject.has("overlays")) {
            for (Map.Entry<String,JsonElement> entry : GsonHelper.getAsJsonObject(jsonObject,"overlays").entrySet()) {
                stringMap.put(new ResourceLocation(entry.getKey()),entry.getValue().getAsString());
            }
        }
        return new FurnitureModelDefinition(definition,defaultStyle,furnitureStyleModelsMap,partMap,stateMap,stringMap);
    }

    private static BlockState parseState(JsonElement element) {
        JsonObject jsonObject;
        if(element.isJsonPrimitive()) {
            jsonObject=new JsonObject();
            jsonObject.addProperty("block",element.getAsString());
        } else {
            jsonObject=element.getAsJsonObject();
        }
        Block block= BuiltInRegistries.BLOCK.get(new ResourceLocation(GsonHelper.getAsString(jsonObject,"block")));
        BlockState blockState=block.defaultBlockState();
        if(jsonObject.has("properties")) {
            for (Map.Entry<String,JsonElement> entry : jsonObject.getAsJsonObject("properties").entrySet()) {
                Property<?> property=block.getStateDefinition().getProperty(entry.getKey());
                if(property != null) blockState=apply(blockState,property,entry.getValue().getAsString());
            }
        }
        return blockState;
    }

    private static TextureMapping mapping(JsonObject jsonObject) {
        float[] crop=array(jsonObject,"crop",new float[]{
                0.0f,0.0f,1.0f,1.0f
        });
        return new TextureMapping(TextureMapping.Mode.valueOf(GsonHelper.getAsString(jsonObject,"mode","preserve_source_faces").toUpperCase()),GsonHelper.getAsFloat(jsonObject,"scale_u",1.0f),GsonHelper.getAsFloat(jsonObject,"scale_v",1.0f),GsonHelper.getAsFloat(jsonObject,"offset_u",0.0f),GsonHelper.getAsFloat(jsonObject,"offset_v",0.0f),GsonHelper.getAsInt(jsonObject,"rotation",0),GsonHelper.getAsBoolean(jsonObject,"mirror_u",false),GsonHelper.getAsBoolean(jsonObject,"mirror_v",false),crop[0],crop[1],crop[2],crop[3]);
    }

    private static EffectRule effect(JsonObject jsonObject) {
        return new EffectRule(color(jsonObject.get("tint"),0xFFFFFFFF),GsonHelper.getAsFloat(jsonObject,"shade",1.0f),GsonHelper.getAsFloat(jsonObject,"alpha",1.0f),GsonHelper.getAsBoolean(jsonObject,"emissive",false),location(jsonObject,"overlay"),GsonHelper.getAsFloat(jsonObject,"metallic",0.0f),GsonHelper.getAsFloat(jsonObject,"pulse_speed",0.0f),GsonHelper.getAsFloat(jsonObject,"pulse_min",1.0f),GsonHelper.getAsFloat(jsonObject,"pulse_max",1.0f),GsonHelper.getAsFloat(jsonObject,"scroll_u",0.0f),GsonHelper.getAsFloat(jsonObject,"scroll_v",0.0f));
    }

    private static float[] array(JsonObject jsonObject,String id,float[] defaultFloat) {
        if(!jsonObject.has(id)) return defaultFloat;
        JsonArray jsonArray=GsonHelper.getAsJsonArray(jsonObject,id);
        float[] result=new float[jsonArray.size()];
        for (int index=0;index < result.length;index++) {
            result[index]=jsonArray.get(index).getAsFloat();
        }
        return result;
    }

    private static int color(@Nullable JsonElement jsonElement,int defaultInt) {
        if(jsonElement == null) return defaultInt;
        if(jsonElement.getAsJsonPrimitive().isNumber()) return jsonElement.getAsInt();
        String value=jsonElement.getAsString().replace("#","");
        long parsed=Long.parseLong(value,16);
        return (int) (value.length() <= 6 ? parsed | 0xFF000000L : parsed);
    }

    @Nullable
    private static ResourceLocation location(JsonObject jsonObject,String id) {
        if(!jsonObject.has(id)) return null;
        return new ResourceLocation(GsonHelper.getAsString(jsonObject,id));
    }

    private static <T extends Comparable<T>> BlockState apply(BlockState blockState,Property<T> property,String value) {
        Optional<T> parsed=property.getValue(value);
        return parsed.map(result -> blockState.setValue(property,result)).orElse(blockState);
    }
}