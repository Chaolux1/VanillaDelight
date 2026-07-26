package net.chaolux.vanilladelight.client.model.furniture;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public enum FurnitureModelLoader implements IGeometryLoader<UnbakedFurnitureModel> {
    INSTANCE;

    @Override
    public UnbakedFurnitureModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
        return new UnbakedFurnitureModel(FurnitureModelDefinition.fromJson(jsonObject));
    }
}
