package net.chaolux.vanilladelight.client.model.modularstove;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ModularStoveModelLoader implements IGeometryLoader<UnbakedModularStoveModel> {
    INSTANCE;

    @Override
    public UnbakedModularStoveModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
        ResourceLocation resourceLocation=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"definition"));
        ResourceLocation location=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"particle","minecraft:block/bricks"));
        ResourceLocation fixedSide=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"fixed_side"));
        ResourceLocation topOff=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"top_off"));
        ResourceLocation arc=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"arc"));
        JsonObject object=GsonHelper.getAsJsonObject(jsonObject,"styles");
        Map<String,UnbakedModularStoveModel.StyleTextures> stringStyleTextureMap=new LinkedHashMap<>();
        for(Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            JsonObject style=stringJsonElementEntry.getValue().getAsJsonObject();
            ResourceLocation front=ResourceLocation.parse(GsonHelper.getAsString(style,"front"));
            ResourceLocation top=ResourceLocation.parse(GsonHelper.getAsString(style,"top"));
            stringStyleTextureMap.put(stringJsonElementEntry.getKey(),new UnbakedModularStoveModel.StyleTextures(front,top));
        }
        return new UnbakedModularStoveModel(resourceLocation,location,fixedSide,topOff,arc,stringStyleTextureMap);
    }
}
