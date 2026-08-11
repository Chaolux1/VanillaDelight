package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class FurnitureBakedModelRegistry {
    private static final Map<ResourceLocation,DynamicFurnitureBakedModel> MODELS=new ConcurrentHashMap<>();
    private FurnitureBakedModelRegistry() {

    }

    public static void register(ResourceLocation resourceLocation,DynamicFurnitureBakedModel furnitureBakedModel) {
        MODELS.put(resourceLocation,furnitureBakedModel);
    }

    public static DynamicFurnitureBakedModel dynamicFurnitureBakedModel(ResourceLocation resourceLocation) {
        return MODELS.get(resourceLocation);
    }

    public static void clear() {
        MODELS.clear();
    }
}
