package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record ResolvedFurnitureMaterial(TextureAtlasSprite atlasSprite,TextureFillPattern fillPattern, int tint, RenderType renderType) {
    public boolean gap() {
        return this.fillPattern != null;
    }
}
