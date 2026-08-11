package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.api.furniture.TextureMapping;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class FurnitureTextureMapper {
    private FurnitureTextureMapper() {

    }

    public static float[] map(float sourceU, float sourceV, float x, float y, float z, Direction direction, TextureAtlasSprite textureAtlasSprite, TextureAtlasSprite atlasSprite, TextureMapping textureMapping,float animationU,float animationV) {
        float u=normalize(sourceU,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v=normalize(sourceV,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        switch (textureMapping.mode()) {
            case TILE -> {
                float[] projected=project(x,y,z,direction);
                u=repeat(projected[0] * textureMapping.scaleU() + textureMapping.offsetU() + animationU);
                v=repeat(projected[1] * textureMapping.scaleV() + textureMapping.offsetV() + animationV);
            }
            case PRESERVE_SCALE -> {
                u=repeat(u * textureMapping.scaleU() + textureMapping.offsetU() + animationU);
                v=repeat(v * textureMapping.scaleV() + textureMapping.offsetV() + animationV);
            }
            case CROP -> {
                u=textureMapping.cropMinU() + u * (textureMapping.cropMaxU() - textureMapping.cropMinU());
                v=textureMapping.cropMinV() + v * (textureMapping.cropMaxV() - textureMapping.cropMinV());
                u=u * textureMapping.scaleU() + textureMapping.offsetU() + animationU;
                v=v * textureMapping.scaleV() + textureMapping.offsetV() + animationV;
            }
            default -> {
                u=u * textureMapping.scaleU() + textureMapping.offsetU() + animationU;
                v=v * textureMapping.scaleV() + textureMapping.offsetV() + animationV;
            }
        }
        if (textureMapping.mirrorU()) u=1.0f - u;
        if (textureMapping.mirrorV()) v=1.0f - v;
        float[] rotateFloat=rotate(u,v,textureMapping.rotation());
        float mapU=atlasSprite.getU0() + rotateFloat[0] * (atlasSprite.getU1() - atlasSprite.getU0());
        float mapV=atlasSprite.getV0() + rotateFloat[1] * (atlasSprite.getV1() - atlasSprite.getV0());
        return new float[] {
                mapU,mapV
        };
    }

    private static float normalize(float value,float min,float max) {
        float size=max - min;
        if(size == 0.0f) return 0.0f;
        return (value - min) / size;
    }

    private static float[] project(float x,float y,float z,Direction direction) {
        return switch (direction) {
            case UP -> new float[] {
                    x,z
            };
            case DOWN -> new float[] {
                    x,1.0f - z
            };
            case NORTH -> new float[] {
                    1.0f - x,1.0f - y
            };
            case SOUTH -> new float[] {
                    x,1.0f - y
            };
            case WEST -> new float[] {
                    z,1.0f - y
            };
            case EAST -> new float[] {
                    1.0f - z,1.0f - y
            };
        };
    }

    private static float[] rotate(float u,float v,int rotate) {
        return switch (rotate) {
            case 90 -> new float[] {
                    1.0f - v,u
            };
            case 180 -> new float[] {
                    1.0f-u,1.0f-v
            };
            case 270 -> new float[] {
                    v,1.0f - u
            };
            default -> new float[] {
                    u,v
            };
        };
    }

    private static float repeat(float value) {
        return value - (float) Math.floor(value);
    }
}
