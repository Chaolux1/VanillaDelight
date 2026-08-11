package net.chaolux.vanilladelight.api.furniture;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record FurniturePart(String id, ResourceLocation placeholder, String material, TextureMapping mapping, List<EffectRule> effectRules) {
    public FurniturePart {
        effectRules=List.copyOf(effectRules);
    }
}
