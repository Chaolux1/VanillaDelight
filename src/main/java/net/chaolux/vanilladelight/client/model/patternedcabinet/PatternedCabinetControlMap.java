package net.chaolux.vanilladelight.client.model.patternedcabinet;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

import java.util.ArrayList;
import java.util.List;

public class PatternedCabinetControlMap {
    private static final int MIN_ALPHA=8;
    private static final int ACCENT_STEP_MIN=250;
    private static final int SHADE_STEPS=32;
    private final List<Region> regions;
    private static final int ACCENT_BLUE_MIN=48;
    private static final int ACCENT_SHADOW_MAX=112;
    private static final int ACCENT_MAX=208;
    private static final int CAVITY_RED_MIN=240;
    private static final int CAVITY_BLUE_MIN=240;
    private static final int CAVITY_GREEN_MAX=20;
    public static final float FRONT_SHADE=0.92f;
    private static final float SHADOW_STRENGTH=0.72f;
    private static final float HIGHLIGHT_STRENGTH=0.18f;
    private static final float MIN_SHADE=0.22f;
    private static final float MAX_SHADE=1.0f;
    private static final float MIN_DELTA=0.015f;
    private static final float ACCENT_SHADOW_SHADE=0.5f;
    private static final float ACCENT_SHADE=0.78f;
    private static final float ACCENT_HIGHLIGHT_SHADE=1.0f;

    private PatternedCabinetControlMap(List<Region> regions) {
        this.regions=List.copyOf(regions);
    }

    public List<Region> regions() {
        return this.regions;
    }

    public static PatternedCabinetControlMap from(TextureAtlasSprite textureAtlasSprite) {
        int width=textureAtlasSprite.contents().width();
        int height=textureAtlasSprite.contents().height();
        if(width <= 0 || height <= 0) return new PatternedCabinetControlMap(List.of());
        Cell[][] cells= new Cell[height][width];
        for(int y=0;y < height;y++) {
            for (int x=0;x < width;x++) {
                int rgba=textureAtlasSprite.getPixelRGBA(0,x,y);
                int red=rgba & 255;
                int green=rgba >>> 8 & 255;
                int blue=rgba >>> 16 & 255;
                int alpha=rgba >>> 24 & 255;
                cells[y][x]=classify(red,green,blue,alpha);
            }
        }
        return new PatternedCabinetControlMap(mergeRegions(cells,width,height));
    }

    private static Cell classify(int red,int green,int blue,int alpha) {
        if(alpha < MIN_ALPHA) return null;
        if(red >= CAVITY_RED_MIN && green <= CAVITY_GREEN_MAX && blue >= CAVITY_BLUE_MIN) return new Cell(Layer.CAVITY,quantizeShade(0.18f));

        if(blue >= ACCENT_BLUE_MIN && red <= 20 && green <= 20) {
            float accentShade;
            if(blue <= ACCENT_SHADOW_MAX) {
                accentShade=ACCENT_SHADOW_SHADE;
            } else if(blue <= ACCENT_MAX) {
                accentShade=ACCENT_SHADE;
            } else {
                accentShade=ACCENT_HIGHLIGHT_SHADE;
            }
            return new Cell(Layer.ACCENT,quantizeShade(accentShade));
        }
        int quantizeRed=quantizeChannel(red);
        int quantizeGreen=quantizeChannel(green);
        if(quantizeRed == 0 && quantizeGreen == 0) return null;
        float shadow=quantizeRed / 255.0f;
        float highlight=quantizeGreen / 255.0f;
        float shade=FRONT_SHADE - shadow * SHADOW_STRENGTH + highlight * HIGHLIGHT_STRENGTH;
        shade=clamp(shade,MIN_SHADE,MAX_SHADE);
        if(Math.abs(shade - FRONT_SHADE) < MIN_DELTA) return null;
        return new Cell(Layer.BODY_SHADE,quantizeShade(shade));
    }

    private static int quantizeChannel(int value) {
        if(value < 23) return 0;
        if(value < 96) return 64;
        if(value < 160) return 128;
        if(value < 224) return 192;
        return 255;
    }

    private static int quantizeShade(float shade) {
        return Math.round(clamp(shade,0.0f,1.0f) * SHADE_STEPS);
    }

    private static List<Region> mergeRegions(Cell[][] cells,int width,int height) {
        boolean[][] used=new boolean[height][width];
        List<Region> result=new ArrayList<>();
        float pixelWidth=16.0f / width;
        float pixelHeight=16.0f / height;
        for (int y=0;y < height;y++) {
            for (int x=0;x < width;x++) {
                Cell cell=cells[y][x];
                if(cell == null || used[y][x]) continue;
                int regionWidth=1;
                int regionHeight=1;
                boolean canGrow=true;
                while (x + regionWidth < width && !used[y][x + regionWidth] && cell.equals(cells[y][x + regionWidth])) {
                    regionWidth++;
                }
                while (y + regionHeight < height && canGrow) {
                    for (int indexX=x;indexX < x + regionWidth;indexX++) {
                        if(used[y + regionHeight][indexX] || !cell.equals(cells[y + regionHeight][indexX])) {
                            canGrow=false;
                            break;
                        }
                    }
                    if(canGrow) regionHeight++;
                }
                for(int indexY=y;indexY < y + regionHeight;indexY++) {
                    for (int markX=x;markX < x + regionWidth;markX++) {
                        used[indexY][markX]=true;
                    }
                }
                float minX=x * pixelWidth;
                float maxX=(x + regionWidth) * pixelWidth;
                float maxY=16.0f - y * pixelHeight;
                float minY=16.0f - (y + regionHeight) * pixelHeight;
                result.add(new Region(minX,minY,maxX,maxY,cell.layer(), cell.shadeStep() / (float) SHADE_STEPS));
            }
        }
        return result;
    }

    private static float clamp(float value,float min,float max) {
        return Math.max(min,Math.min(max,value));
    }

    private record Cell(Layer layer,int shadeStep) {

    }

    public enum Layer {
        BODY_SHADE,ACCENT,CAVITY;
    }

    public record Region(float minX,float minY,float maxX,float maxY,Layer layer,float shade) {

    }
}
