package net.chaolux.vanilladelight.client.model.patternedcabinet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.chaolux.vanilladelight.client.model.furniture.UnbakedFurnitureModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.LinkedHashMap;
import java.util.Map;

public enum PatternedCabinetModelLoader implements IGeometryLoader<UnbakedPatternedCabinetModel> {
    INSTANCE;

    @Override
    public UnbakedPatternedCabinetModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
        ResourceLocation resourceLocation=new ResourceLocation(GsonHelper.getAsString(jsonObject,"definition"));
        JsonObject object=GsonHelper.getAsJsonObject(jsonObject,"patterns");
        Map<String,UnbakedPatternedCabinetModel.TextureKey> stringTextureKeysMap=new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> stringJsonElementEntry : object.entrySet()) {
            JsonObject valueObject=stringJsonElementEntry.getValue().getAsJsonObject();
            String close=GsonHelper.getAsString(valueObject,"closed");
            String open=GsonHelper.getAsString(valueObject,"open",close);
            stringTextureKeysMap.put(stringJsonElementEntry.getKey(),new UnbakedPatternedCabinetModel.TextureKey(close,open));
        }
        return new UnbakedPatternedCabinetModel(resourceLocation,stringTextureKeysMap);
    }
}
