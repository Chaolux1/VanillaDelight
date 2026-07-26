package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.awt.*;

public class TextureGapFiller {
    private TextureGapFiller() {

    }

    public static TextureFillPattern textureFillPattern(TextureAtlasSprite textureAtlasSprite) {
        SpriteContents spriteContents=textureAtlasSprite.contents();
        int width=spriteContents.width();
        int height=spriteContents.height();
        if(width <= 0 || height <= 0) return null;
        int[] frames=spriteContents.getUniqueFrames().toArray();
        if(frames.length == 0) frames=new int[]{0};
        int[] heights=new int[width];
        int[] widths=new int[width + 1];
        Rectangle bestRectangle=null;
        boolean hasTransparentPixel=false;
        boolean hasOpaquePixel=false;
        for(int y=0;y < height;y++) {
            for(int x=0;x < width;x++) {
                boolean opaque=isOpaqueInEveryFrame(spriteContents,frames,x,y);
                if(opaque) {
                    heights[x]++;
                    hasOpaquePixel=true;
                } else {
                    heights[x] = 0;
                    hasTransparentPixel=true;
                }
            }
            int stackSize=0;
            for(int x=0;x <= width;x++) {
                int currentHeight=x == width ? 0 : heights[x];
                while (stackSize > 0 && heights[widths[stackSize - 1]] > currentHeight) {
                    int heightIndex=widths[--stackSize];
                    int rectangleHeight=heights[heightIndex];
                    int left=stackSize == 0 ? 0 : widths[stackSize - 1] + 1;
                    int rectangleWidth=x - left;
                    Rectangle rectangle=new Rectangle(left,y - rectangleHeight + 1,rectangleWidth,rectangleHeight);
                    if(bestRectangle == null || rectangle.isBetterThan(bestRectangle)) bestRectangle=rectangle;
                }
                if(x < width) widths[stackSize++] = x;
            }
        }
        if(!hasTransparentPixel) return null;
        if(!hasOpaquePixel || bestRectangle == null) return null;
        if(bestRectangle.width() <= 0 || bestRectangle.height() <= 0) return null;
        return new TextureFillPattern(textureAtlasSprite,bestRectangle.x(),bestRectangle.y(),bestRectangle.width(),bestRectangle.height(),width,height);
    }

    private static boolean isOpaqueInEveryFrame(SpriteContents spriteContents,int[] frames,int x,int y) {
        for(int frame : frames) {
            if(spriteContents.isTransparent(frame,x,y)) return false;
        }
        return true;
    }

    private record Rectangle(int x,int y,int width,int height) {
        private int area() {
            return this.width * this.height;
        }

        private int shortSide() {
            return Math.min(this.width,this.height);
        }

        private boolean isBetterThan(Rectangle rectangle) {
            int area=this.area();
            int intArea=rectangle.area();
            if(area != intArea) return area > intArea;
            int shortSide=this.shortSide();
            int intShortSide=rectangle.shortSide();
            if(shortSide != intShortSide) return shortSide > intShortSide;
            return this.width > rectangle.width;
        }
    }
}
