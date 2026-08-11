package net.chaolux.vanilladelight.api.furniture;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public record StylePresent(String id, @Nullable ResourceLocation model, @Nullable ResourceLocation openModel) {
    @Nullable
    public ResourceLocation model(boolean open) {
        if(open && this.openModel != null) return this.openModel;
        return this.model;
    }
}
