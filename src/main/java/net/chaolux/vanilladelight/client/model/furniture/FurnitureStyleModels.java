package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record FurnitureStyleModels(ResourceLocation close, @Nullable ResourceLocation open) {
    public ResourceLocation model(boolean modelOpen) {
        if(modelOpen && this.open != null) return this.open;
        return this.close;
    }
}
