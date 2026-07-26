package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.api.furniture.EffectRule;
import net.chaolux.vanilladelight.api.furniture.FurniturePart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class FurnitureQuadBuilder {
    private FurnitureQuadBuilder() {

    }

    public static List<BakedQuad> buildStatic(BakedQuad bakedQuad, ResolvedFurnitureMaterial furnitureMaterial, FurniturePart furniturePart, Map<ResourceLocation, TextureAtlasSprite> overlays) {
        List<BakedQuad> result=new ArrayList<>();
        if(furnitureMaterial.gap() && furnitureMaterial.underlaySprite() != null) result.add(transform(bakedQuad,furnitureMaterial.underlaySprite(),furniturePart,furnitureMaterial.tint(),List.of(),0.0f,0.0f,1.0f,false));
        result.add(transform(bakedQuad,furnitureMaterial.atlasSprite(),furniturePart,furnitureMaterial.tint(),furniturePart.effectRules(),0.0f,0.0f,1.0f,furnitureMaterial.gap()));
        for (EffectRule effectRule : furniturePart.effectRules()) {
            if (!effectRule.animated() && effectRule.overlay() != null) {
                TextureAtlasSprite overlay=overlays.get(effectRule.overlay());
                if(overlay != null) result.add(transform(bakedQuad,overlay,furniturePart,-1,List.of(effectRule),0.0f,0.0f,1.0f,true));
            }
        }
        return result;
    }

    public static List<BakedQuad> buildAnimated(BakedQuad bakedQuad,ResolvedFurnitureMaterial furnitureMaterial,FurniturePart furniturePart,Map<ResourceLocation,TextureAtlasSprite> overlays,long tick,float partialTick) {
        List<BakedQuad> result=new ArrayList<>();
        for (EffectRule effectRule : furniturePart.effectRules()) {
            if(!effectRule.animated()) continue;
            TextureAtlasSprite textureAtlasSprite=effectRule.overlay() == null ? furnitureMaterial.atlasSprite() : overlays.get(effectRule.overlay());
            if(textureAtlasSprite == null) continue;
            float tickFloat=tick + partialTick;
            result.add(transform(bakedQuad,textureAtlasSprite,furniturePart,effectRule.overlay() == null ? furnitureMaterial.tint() : -1,List.of(effectRule),effectRule.scrollU() * tickFloat,effectRule.scrollV() * tickFloat,effectRule.pulse(tick,partialTick),true));
        }
        return result;
    }

    public static BakedQuad rotate(BakedQuad bakedQuad, Direction direction) {
        if(direction == Direction.NORTH) return bakedQuad;
        int[] vertices=bakedQuad.getVertices().clone();
        int stride=vertices.length / 4;
        int steps=rotationSteps(direction);
        for (int vertex = 0;vertex < 4;vertex++) {
            int base=vertex * stride;
            float x=Float.intBitsToFloat(vertices[base]);
            float z=Float.intBitsToFloat(vertices[base + 2]);
            for (int step = 0;step < steps;step++) {
                float nextX=1.0f - z;
                z=x;
                x=nextX;
            }
            vertices[base]=Float.floatToRawIntBits(x);
            vertices[base + 2]=Float.floatToRawIntBits(z);
        }
        Direction facing=bakedQuad.getDirection();
        for (int step=0;step < steps && facing.getAxis().isHorizontal();step++) {
            facing=facing.getClockWise();
        }
        int normal=packNormal(facing);
        for (int vertex = 0;vertex < 4;vertex++) {
            vertices[vertex * stride + 7]=normal;
        }
        return new BakedQuad(vertices,bakedQuad.getTintIndex(),facing,bakedQuad.getSprite(),bakedQuad.isShade(),bakedQuad.hasAmbientOcclusion());
    }

    public static Direction direction(Direction direction,Direction facing) {
        Direction result=direction;
        int steps=rotationSteps(facing);
        for (int step=0;step < steps && result.getAxis().isHorizontal();step++) {
            result=result.getCounterClockWise();
        }
        return result;
    }

    private static int rotationSteps(Direction direction) {
        return switch (direction) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
    }

    private static int packNormal(Direction direction) {
        int x=direction.getStepX() * 127 & 255;
        int y=direction.getStepY() * 127 & 255;
        int z=direction.getStepZ() * 127 & 255;
        return x | y << 8 | z << 16;
    }

    private static BakedQuad transform(BakedQuad bakedQuad,TextureAtlasSprite textureAtlasSprite,FurniturePart furniturePart,int tint,List<EffectRule> effectRules,float animationU,float animationV,float pulse,boolean offset) {
        int[] vertices=bakedQuad.getVertices().clone();
        int stride=vertices.length / 4;
        Direction direction=bakedQuad.getDirection();
        TextureAtlasSprite atlasSprite=bakedQuad.getSprite();
        ColorTransform colorTransform=ColorTransform.from(tint,effectRules,pulse);
        boolean emissive=effectRules.stream().anyMatch(EffectRule::emissive);
        for (int vertex=0;vertex < 4;vertex++) {
            int base=vertex * stride;
            float x=Float.intBitsToFloat(vertices[base]);
            float y=Float.intBitsToFloat(vertices[base + 1]);
            float z=Float.intBitsToFloat(vertices[base + 2]);
            if(offset) {
                float offsetFloat=0.0005f;
                x += direction.getStepX() * offsetFloat;
                y += direction.getStepY() * offsetFloat;
                z += direction.getStepZ() * offsetFloat;
                vertices[base]=Float.floatToRawIntBits(x);
                vertices[base + 1]=Float.floatToRawIntBits(y);
                vertices[base + 2]=Float.floatToRawIntBits(z);
            }
            int color=vertices[base + 3];
            vertices[base + 3]=colorTransform.apply(color);
            float sourceU=Float.intBitsToFloat(vertices[base + 4]);
            float sourceV=Float.intBitsToFloat(vertices[base + 5]);
            float[] map=FurnitureTextureMapper.map(sourceU,sourceV,x,y,z,direction,atlasSprite,textureAtlasSprite,furniturePart.mapping(),animationU,animationV);
            vertices[base + 4]=Float.floatToRawIntBits(map[0]);
            vertices[base + 5]=Float.floatToRawIntBits(map[1]);
            if(emissive) vertices[base + 6]= LightTexture.FULL_BRIGHT;
        }
        return new BakedQuad(vertices,-1,direction,textureAtlasSprite,bakedQuad.isShade(),bakedQuad.hasAmbientOcclusion());
    }

    private record ColorTransform(float red,float green,float blue,float alpha) {
        private static ColorTransform from(int tint,List<EffectRule> effectRules,float pulse) {
            float red=tint < 0 ? 1.0f : ((tint >> 16) & 255) / 255.0f;
            float green=tint < 0 ? 1.0f : ((tint >> 8) & 255) / 255.0f;
            float blue=tint < 0 ? 1.0f : (tint & 255) / 255.0f;
            float alpha=1.0f;
            float shade=1.0f;
            float metallic=0.0f;
            for (EffectRule effectRule : effectRules) {
                int tinted=effectRule.tint();
                red *= ((tinted >> 16) & 255) / 255.0f;
                green *= ((tinted >> 8) & 255) / 255.0f;
                blue *= (tinted & 255) / 255.0f;
                alpha *=effectRule.alpha() * ((tinted >>> 24) & 255) / 255.0f;
                shade *= effectRule.shade();
                metallic=Math.max(metallic,effectRule.metallic());
            }
            float brightness=shade * pulse * (1.0f + metallic * 0.2f);
            return new ColorTransform(clamp(red * brightness),clamp(green * brightness),clamp(blue * brightness),clamp(alpha));
        }
        private int apply(int defaultApply) {
            int defaultRed=defaultApply & 255;
            int defaultGreen=defaultApply >> 8 & 255;
            int defaultBlue=defaultApply >> 16 & 255;
            int defaultAlpha=defaultApply >>> 24 & 255;
            int red=Math.round(defaultRed * this.red);
            int green=Math.round(defaultGreen * this.green);
            int blue=Math.round(defaultBlue * this.blue);
            int alpha=Math.round(defaultAlpha * this.alpha);
            return alpha << 24 | blue << 16 | green << 8 | red;
        }
        private static float clamp(float value) {
            return Math.max(0.0f,Math.min(1.0f,value));
        }
    }
}
