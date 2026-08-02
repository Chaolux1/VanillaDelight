//package net.chaolux.vanilladelight.client.model.modularcuttingboard;
//
//import net.minecraft.client.renderer.texture.TextureAtlasSprite;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ModularCuttingBoardControlMap {
//    private static final int MIN_ALPHA=8;
//    private static final int SHADE_STEPS=32;
//    public static final float TOP_SHADE=0.96f;
//    private static final float SHADOW_STRENGTH=0.2f;
//    private static final float HIGHLIGHT_STRENGTH=0.04f;
//    private static final float MIN_SHADE=0.74f;
//    private static final float MAX_SHADE=1.0f;
//    private static final float MIN_DELTA=0.01f;
//    public static final ModularCuttingBoardControlMap EMPTY=new ModularCuttingBoardControlMap(List.of());
//    private final List<Region> regions;
//
//    private ModularCuttingBoardControlMap(List<Region> regions) {
//        this.regions=List.copyOf(regions);
//    }
//
//    public List<Region> regions() {
//        return this.regions;
//    }
//
//    public static ModularCuttingBoardControlMap from(TextureAtlasSprite textureAtlasSprite) {
//        int width=textureAtlasSprite.contents().width();
//        int height=textureAtlasSprite.contents().height();
//        if(width <= 0 || height <= 0) return EMPTY;
//        Cell[][] cells=new Cell[height][width];
//        for(int y=0;y < height;y++) {
//            for (int x=0;x < width;x++) {
//                int rgba=textureAtlasSprite.getPixelRGBA(0,x,y);
//                int red=rgba & 255;
//                int green=rgba >>> 8 & 255;
//                int alpha=rgba >>> 24 & 255;
//                cells[y][x]=classify(red,green,alpha);
//            }
//        }
//        return new ModularCuttingBoardControlMap(mergeRegions(cells,width,height));
//    }
//
//    private static Cell classify(int red,int green,int alpha) {
//        if(alpha < MIN_ALPHA) return null;
//        int quantizeRed=quantizeChannel(red);
//        int quantizeGreen=quantizeChannel(green);
//        if(quantizeRed == 0 && quantizeGreen == 0) return null;
//        float shadow=quantizeRed / 255.0f;
//        float highlight=quantizeGreen / 255.0f;
//        float shade=TOP_SHADE - shadow * SHADOW_STRENGTH + highlight * HIGHLIGHT_STRENGTH;
//        shade=clamp(shade,MIN_SHADE,MAX_SHADE);
//        if(Math.abs(shade - TOP_SHADE) < MIN_DELTA) return null;
//        return new Cell(quantizeShade(shade));
//    }
//
//    private static int quantizeChannel(int value) {
//        if(value < 24) return 0;
//        if(value < 96) return 64;
//        if(value < 160) return 128;
//        if(value < 224) return 192;
//        return 255;
//    }
//
//    private static int quantizeShade(float shade) {
//        return Math.round(clamp(shade,0.0f,1.0f) * SHADE_STEPS);
//    }
//
//    private static List<Region> mergeRegions(Cell[][] cells,int width,int height) {
//        boolean[][] used=new boolean[height][width];
//        List<Region> result=new ArrayList<>();
//        float pixelWidth=16.0f / width;
//        float pixelHeight=16.0f / height;
//        for (int y=0;y < height;y++) {
//            for (int x=0;x < width;x++) {
//                Cell cell=cells[y][x];
//                if(cell == null || used[y][x]) continue;
//                int regionWidth=1;
//                int regionHeight=1;
//                boolean canGrow=true;
//                while (x + regionWidth < width && !used[y][x + regionWidth] && cell.equals(cells[y][x + regionWidth])) {
//                    regionWidth++;
//                }
//                while (y + regionHeight < height && canGrow) {
//                    for (int indexX=x;indexX < x + regionWidth;indexX++) {
//                        if(used[y + regionHeight][indexX] || !cell.equals(cells[y + regionHeight][indexX])) {
//                            canGrow=false;
//                            break;
//                        }
//                    }
//                    if(canGrow) regionHeight++;
//                }
//                for (int indexY=y;indexY < y + regionHeight;indexY++) {
//                    for (int indexX=x;indexX < x + regionWidth;indexX++) {
//                        used[indexY][indexX]=true;
//                    }
//                }
//                float minX=x * pixelWidth;
//                float maxX=(x + regionWidth) * pixelWidth;
//                float maxZ=16.0f - y * pixelHeight;
//                float minZ=16.0f - (y + regionHeight) * pixelHeight;
//                result.add(new Region(minX,minZ,maxX,maxZ,cell.shadeStep() / (float) SHADE_STEPS));
//            }
//        }
//        return result;
//    }
//
//    private static float clamp(float value,float min,float max) {
//        return Math.max(min,Math.min(max,value));
//    }
//
//    private record Cell(int shadeStep) {
//
//    }
//
//    public record Region(float minX,float minZ,float maxX,float maxZ,float shade) {
//
//    }
//}
