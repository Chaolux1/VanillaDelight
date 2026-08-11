package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import java.util.LinkedHashMap;
import java.util.Map;

public enum ModularCuttingBoardModelLoader implements IGeometryLoader<UnbakedModularCuttingBoardModel> {
    INSTANCE;

    @Override
    public UnbakedModularCuttingBoardModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
        ResourceLocation resourceLocation=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"definition"));
        Map<String,String> stringMap=new LinkedHashMap<>();
        if(jsonObject.has("patterns")) {
            JsonObject object=GsonHelper.getAsJsonObject(jsonObject,"patterns");
            for (Map.Entry<String, JsonElement> elementEntry : object.entrySet()) {
                stringMap.put(elementEntry.getKey(),elementEntry.getValue().getAsString());
            }
        }
        return new UnbakedModularCuttingBoardModel(resourceLocation,stringMap);
    }
}
