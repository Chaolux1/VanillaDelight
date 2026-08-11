package net.chaolux.vanilladelight.common.furniture;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record FurnitureRenderData(ResourceLocation id, String style, Map<String, MaterialState> materialStateMap,boolean open) {
    public FurnitureRenderData {
        materialStateMap=Map.copyOf(materialStateMap);
    }

    public MaterialState materialState(String string,MaterialState materialState) {
        return this.materialStateMap.getOrDefault(string,materialState);
    }
}
