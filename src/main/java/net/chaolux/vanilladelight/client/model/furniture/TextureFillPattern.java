package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.Objects;

public record TextureFillPattern(TextureAtlasSprite textureAtlasSprite,int sourceX,int sourceY,int sourceWidth,int sourceHeight,int spriteWidth,int spriteHeight) {
    public TextureFillPattern {
        Objects.requireNonNull(textureAtlasSprite,"Texture fill sprite cannot be null");
        if(sourceX < 0 || sourceY < 0) throw new IllegalArgumentException("Texture fill coordinates cannot be negative");
        if(sourceWidth <= 0 || sourceHeight <= 0) throw new IllegalArgumentException("Texture fill size must be zero");
        if(spriteWidth <= 0 || spriteHeight <= 0) throw new IllegalArgumentException("Sprite size must be zero");
        if(sourceX + sourceWidth > spriteWidth || sourceY + sourceHeight > spriteHeight) throw new IllegalArgumentException("Texture fill region is outside sprite");
    }

}
