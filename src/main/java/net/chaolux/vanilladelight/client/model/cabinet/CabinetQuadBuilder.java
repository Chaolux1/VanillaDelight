package net.chaolux.vanilladelight.client.model.cabinet;

import net.chaolux.vanilladelight.client.model.furniture.TextureFillPattern;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class CabinetQuadBuilder {
    private CabinetQuadBuilder() {

    }

    public static BakedQuad create(Direction direction, float minA, float minB, float maxA, float maxB, float offset, TextureAtlasSprite textureAtlasSprite,int tint,float shade,boolean stretch) {
        float a0=minA / 16.0f;
        float a1=maxA / 16.0f;
        float b0=minB / 16.0f;
        float b1=maxB / 16.0f;
        float normalizeU0;
        float normalizeV0;
        float normalizeU1;
        float normalizeV1;
        if(stretch) {
            normalizeU0=0.0f;
            normalizeV0=0.0f;
            normalizeU1=1.0f;
            normalizeV1=1.0f;
        } else {
            normalizeU0=a0;
            normalizeU1=a1;
            if(direction.getAxis() == Direction.Axis.Y) {
                normalizeV0=b0;
                normalizeV1=b1;
            } else {
                normalizeV0=1.0f - b1;
                normalizeV1=1.0f - b0;
            }
        }
        float u0=lerp(normalizeU0,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v0=lerp(normalizeV0,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        float u1=lerp(normalizeU1,textureAtlasSprite.getU0(),textureAtlasSprite.getU1());
        float v1=lerp(normalizeV1,textureAtlasSprite.getV0(),textureAtlasSprite.getV1());
        Vertex[] vertices=vertices(direction,a0,b0,a1,b1,offset,u0,v0,u1,v1);
        int[] intData=new int[32];
        int color=color(tint,shade);
        int normal=packNormal(direction);
        for (int index=0;index < vertices.length;index++) {
            putVertex(intData,index,vertices[index],color,normal);
        }
        return new BakedQuad(intData,-1,direction,textureAtlasSprite,true,true);
    }

    private static Vertex[] vertices(Direction direction,float a0,float b0,float a1,float b1,float offset,float u0,float v0,float u1,float v1) {
        return switch (direction) {
            case NORTH -> new Vertex[] {
                    new Vertex(a0,b0,-offset,u0,v1),new Vertex(a0,b1,-offset,u0,v0),new Vertex(a1,b1,-offset,u1,v0),new Vertex(a1,b0,-offset,u1,v1)
            };
            case SOUTH -> new Vertex[] {
                    new Vertex(a1,b0,1.0f + offset,u0,v1),new Vertex(a1,b1,1.0f + offset,u0,v0),new Vertex(a0,b1,1.0f + offset,u1,v0),new Vertex(a0,b0,1.0f + offset,u1,v1)
            };
            case WEST -> new Vertex[] {
                    new Vertex(-offset,b0,a1,u0,v1),new Vertex(-offset,b1,a1,u0,v0),new Vertex(-offset,b1,a0,u1,v0),new Vertex(-offset,b0,a0,u1,v1)
            };
            case EAST -> new Vertex[] {
                    new Vertex(1.0f + offset,b0,a0,u0,v1),new Vertex(1.0f + offset,b1,a0,u0,v0),new Vertex(1.0f + offset,b1,a1,u1,v0),new Vertex(1.0f + offset,b0,a1,u1,v1)
            };
            case UP -> new Vertex[] {
                    new Vertex(a0,1.0f + offset,b0,u0,v1),new Vertex(a0,1.0f + offset,b1,u0,v0),new Vertex(a1,1.0f + offset,b1,u1,v0),new Vertex(a1,1.0f + offset,b0,u1,v1)
            };
            case DOWN -> new Vertex[] {
                    new Vertex(a0,-offset,b1,u0,v1),new Vertex(a0,-offset,b0,u0,v0),new Vertex(a1,-offset,b0,u1,v0),new Vertex(a1,-offset,b1,u1,v1)
            };
        };
    }

    private static void putVertex(int[] intData,int vertices,Vertex vertex,int color,int normal) {
        int offset=vertices * 8;
        intData[offset]=Float.floatToRawIntBits(vertex.x());
        intData[offset + 1]=Float.floatToRawIntBits(vertex.y());
        intData[offset + 2]=Float.floatToRawIntBits(vertex.z());
        intData[offset + 3]=color;
        intData[offset + 4]=Float.floatToRawIntBits(vertex.u());
        intData[offset + 5]=Float.floatToRawIntBits(vertex.v());
        intData[offset + 6]=0;
        intData[offset + 7]=normal;
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

    public static BakedQuad createRegion(Direction direction, float minA, float minB, float maxA, float maxB, float offset, TextureFillPattern textureFillPattern,float sourceOffsetX,float sourceOffsetY,float width,float height,int tint,float shade) {
        TextureAtlasSprite textureAtlasSprite=textureFillPattern.textureAtlasSprite();
        float a0=minA / 16.0f;
        float a1=maxA / 16.0f;
        float b0=minB / 16.0f;
        float b1=maxB / 16.0f;
        float sourceMinX=textureFillPattern.sourceX() + sourceOffsetX;
        float sourceMinY= textureFillPattern.sourceY() + sourceOffsetY;
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
        return createWithUV(direction,a0,b0,a1,b1,offset,textureAtlasSprite,tint,shade,u0,v0,u1,v1);
    }

    private static BakedQuad createWithUV(Direction direction,float a0,float b0,float a1,float b1,float offset,TextureAtlasSprite textureAtlasSprite,int tint,float shade,float u0,float v0,float u1,float v1) {
        Vertex[] vertices=vertices(direction,a0,b0,a1,b1,offset,u0,v0,u1,v1);
        int[] intData=new int[32];
        int color=color(tint,shade);
        int normal=packNormal(direction);
        for(int index=0;index < vertices.length;index++) {
            putVertex(intData,index,vertices[index],color,normal);
        }
        return new BakedQuad(intData,-1,direction,textureAtlasSprite,true,true);
    }

    private record Vertex(float x,float y,float z,float u,float v) {

    }

}
