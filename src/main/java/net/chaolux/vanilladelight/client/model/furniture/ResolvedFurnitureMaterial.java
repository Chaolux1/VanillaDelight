package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public record ResolvedFurnitureMaterial(TextureAtlasSprite atlasSprite,TextureAtlasSprite underlaySprite, int tint, RenderType renderType,boolean gap) {

}
