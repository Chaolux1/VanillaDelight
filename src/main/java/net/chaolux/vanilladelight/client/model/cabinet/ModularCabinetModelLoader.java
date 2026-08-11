package net.chaolux.vanilladelight.client.model.cabinet;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public enum ModularCabinetModelLoader implements IGeometryLoader<UnbakedModularCabinetModel> {
    INSTANCE;

    @Override
    public UnbakedModularCabinetModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
        ResourceLocation resourceLocation=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"definition"));
        ResourceLocation location=ResourceLocation.parse(GsonHelper.getAsString(jsonObject,"particle","minecraft:block/oak_planks"));
        return new UnbakedModularCabinetModel(resourceLocation,location);
    }

}
