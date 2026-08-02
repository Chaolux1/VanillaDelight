package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import net.chaolux.vanilladelight.client.model.furniture.TextureFillPattern;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class CuttingBoardQuadBuilder {
    private CuttingBoardQuadBuilder() {

    }

    public static BakedQuad create(Direction direction, float minA, float minB, float maxA, float maxB, float plane, float offset, TextureAtlasSprite textureAtlasSprite,int tint,float shade,boolean stretch) {
        float a0=minA / 16.0f;
        float b0=minB / 16.0f;
        float a1=maxA / 16.0f;
        float b1=maxB / 16.0f;
        float normalizedU0;
        float normalizedV0;
        float normalizedU1;
        float normalizedV1;
        if(stretch) {
            normalizedU0=0.0f;
            normalizedV0=0.0f;
            normalizedU1=1.0f;
            normalizedV1=1.0f;
        } else {
            normalizedU0=a0;
            normalizedU1=a1;
            if(direction.getAxis() == Direction.Axis.Y) {
                normalizedV0=b0;
                normalizedV1=b1;
            } else {
                normalizedV0=1.0f - b1;
                normalizedV1=1.0f - b0;
            }
        }
        float u0=lerp(normalizedU0,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v0=lerp(normalizedV0,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        float u1=lerp(normalizedU1,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v1=lerp(normalizedV1,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        return createWithUv(direction,a0,b0,a1,b1,plane / 16.0f,offset,textureAtlasSprite,tint,shade,u0,v0,u1,v1);
    }

    public static BakedQuad createRegion(Direction direction, float minA, float minB, float maxA, float maxB, float plane, float offset, TextureFillPattern textureFillPattern,float sourceX,float sourceY,float width,float height,int tint,float shade) {
        TextureAtlasSprite textureAtlasSprite= textureFillPattern.textureAtlasSprite();
        float sourceMinX=textureFillPattern.sourceX() + sourceX;
        float sourceMinY=textureFillPattern.sourceY() + sourceY;
        float sourceMaxX=sourceMinX + width;
        float sourceMaxY=sourceMinY + height;
        float normalizedU0=sourceMinX / textureFillPattern.spriteWidth();
        float normalizedV0=sourceMinY / textureFillPattern.spriteHeight();
        float normalizedU1=sourceMaxX / textureFillPattern.spriteWidth();
        float normalizedV1=sourceMaxY / textureFillPattern.spriteHeight();
        float u0=lerp(normalizedU0,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v0=lerp(normalizedV0,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        float u1=lerp(normalizedU1,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v1=lerp(normalizedV1,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        return createWithUv(direction,minA / 16.0f,minB / 16.0f,maxA / 16.0f,maxB / 16.0f,plane / 16.0f,offset,textureAtlasSprite,tint,shade,u0,v0,u1,v1);
    }

    private static BakedQuad createWithUv(Direction direction,float a0,float b0,float a1,float b1,float plane,float offset,TextureAtlasSprite textureAtlasSprite,int tint,float shade,float u0,float v0,float u1,float v1) {
        Vertex[] vertices=vertices(direction,a0,b0,a1,b1,plane,offset,u0,v0,u1,v1);
        int[] vertexData=new int[32];
        int color=color(tint,shade);
        int normal=packNormal(direction);
        for (int index=0;index < vertices.length;index++) {
            putVertex(vertexData,index,vertices[index],color,normal);
        }
        return new BakedQuad(vertexData,-1,direction,textureAtlasSprite,true,true);
    }

    private static Vertex[] vertices(Direction direction,float a0,float b0,float a1,float b1,float plane,float offset,float u0,float v0,float u1,float v1) {
        return switch (direction) {
            case NORTH -> {
                float z = plane - offset;
                yield new Vertex[]{
                        new Vertex(a0, b0, z, u0, v1), new Vertex(a0, b1, z, u0, v0), new Vertex(a1, b1, z, u1, v0), new Vertex(a1, b0, z, u1, v1)
                };
            }
            case SOUTH -> {
                float z = plane + offset;
                yield new Vertex[]{
                        new Vertex(a1, b0, z, u0, v1), new Vertex(a1, b1, z, u0, v0), new Vertex(a0, b1, z, u1, v0), new Vertex(a0, b0, z, u1, v1)
                };
            }
            case WEST -> {
                float x = plane - offset;
                yield new Vertex[]{
                        new Vertex(x, b0, a1, u0, v1), new Vertex(x, b1, a1, u0, v0), new Vertex(x, b1, a0, u1, v0), new Vertex(x, b0, a0, u1, v1)
                };
            }
            case EAST -> {
                float x = plane + offset;
                yield new Vertex[]{
                        new Vertex(x, b0, a0, u0, v1), new Vertex(x, b1, a0, u0, v0), new Vertex(x, b1, a1, u1, v0), new Vertex(x, b0, a1, u1, v1)
                };
            }
            case UP -> {
                float y = plane + offset;
                yield new Vertex[]{
                        new Vertex(a0, y, b0, u0, v1), new Vertex(a0, y, b1, u0, v0), new Vertex(a1, y, b1, u1, v0), new Vertex(a1, y, b0, u1, v1)
                };
            }
            case DOWN -> {
                float y = plane - offset;
                yield new Vertex[]{
                        new Vertex(a0, y, b1, u0, v1), new Vertex(a0, y, b0, u0, v0), new Vertex(a1, y, b0, u1, v0), new Vertex(a1, y, b1, u1, v1)
                };
            }
        };
    }

    private static void putVertex(int[] vertexData,int vertexIndex,Vertex vertex,int color,int normal) {
        int offset=vertexIndex * 8;
        vertexData[offset]=Float.floatToRawIntBits(vertex.x());
        vertexData[offset + 1]=Float.floatToRawIntBits(vertex.y());
        vertexData[offset + 2]=Float.floatToRawIntBits(vertex.z());
        vertexData[offset + 3]=color;
        vertexData[offset + 4]=Float.floatToRawIntBits(vertex.u());
        vertexData[offset + 5]=Float.floatToRawIntBits(vertex.v());
        vertexData[offset + 6]=0;
        vertexData[offset + 7]=normal;
    }

    private static int color(int tint,float shade) {
        int red=tint < 0 ? 255 : tint >> 16 & 255;
        int green=tint < 0 ? 255 : tint >> 8 & 255;
        int blue=tint < 0 ? 255 : tint & 255;
        red=clamp(Math.round(red * shade));
        green=clamp(Math.round(green * shade));
        blue=clamp(Math.round(blue * shade));
        return 255 << 24 | blue << 16 | green << 8 | red;
    }

    private static int packNormal(Direction direction) {
        int x=direction.getStepX() * 127 & 255;
        int y=direction.getStepY() * 127 & 255;
        int z=direction.getStepZ() * 127 & 255;
        return x | y << 8 | z << 16;
    }

    private static float lerp(float value,float start,float end) {
        return start + value * (end - start);
    }

    private static int clamp(int value) {
        return Math.max(0,Math.min(255,value));
    }

    private record Vertex(float x,float y,float z,float u,float v) {

    }
}
