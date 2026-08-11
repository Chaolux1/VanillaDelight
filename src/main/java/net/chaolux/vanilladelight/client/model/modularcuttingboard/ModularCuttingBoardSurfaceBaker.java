package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import net.chaolux.vanilladelight.client.model.patternedcabinet.PatternedCabinetControlMap;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModularCuttingBoardSurfaceBaker {
    private static final int SIZE=16;
    private static final int PALETTE_SIZE=5;
    private static final int MIN_ALPHA=8;
    private static final float PATTERN= PatternedCabinetControlMap.FRONT_SHADE;
    private final TextureAtlasSprite textureAtlasSprite;
    private final Map<Key,Surface> CACHE=new ConcurrentHashMap<>();

    public ModularCuttingBoardSurfaceBaker(TextureAtlasSprite textureAtlasSprite) {
        this.textureAtlasSprite=textureAtlasSprite;
    }

    TextureAtlasSprite surfaceTemplate() {
        return this.textureAtlasSprite;
    }

    public Surface bake(TextureAtlasSprite textureAtlasSprite, int tint, @Nullable PatternedCabinetControlMap patternedCabinetControlMap) {
        Key key=new Key(textureAtlasSprite,tint,patternedCabinetControlMap);
        return this.CACHE.computeIfAbsent(key,uncacheKey -> this.bakeUncache(textureAtlasSprite,tint,patternedCabinetControlMap));
    }

    public void clear() {
        this.CACHE.clear();
    }

    private Surface bakeUncache(TextureAtlasSprite textureAtlasSprite,int tint,@Nullable PatternedCabinetControlMap patternedCabinetControlMap) {
        Palette palette=Palette.palette(textureAtlasSprite,tint);
        int[][] color=new int[SIZE][SIZE];
        for(int z=0;z < SIZE;z++) {
            for (int x=0;x < SIZE;x++) {
                int level=this.templateLevel(x,z);
                int patternLevel=patternLevel(patternedCabinetControlMap,x + 0.5f,z + 0.5f);
                int endLevel=patternLevel >= 0 ? patternLevel : level;
                color[z][x]=palette.color(clamp(endLevel,0,PALETTE_SIZE -1));
            }
        }
        return new Surface(mergeRegion(color));
    }

    private int templateLevel(int x,int z) {
        int width=this.textureAtlasSprite.contents().width();
        int height=this.textureAtlasSprite.contents().height();
        int sourceX=surfaceCoordinate(x,width);
        int sourceY=surfaceCoordinate(SIZE - 1 - z,height);
        int rgba=this.textureAtlasSprite.getPixelRGBA(0,sourceX,sourceY);
        int red=rgba & 255;
        int green=rgba >>> 8 & 255;
        int blue=rgba >>> 16 & 255;
        int gray=Math.round(red * 0.2126f + green * 0.7152f + blue * 0.0722f);
        if((rgba >> 24 & 255) < MIN_ALPHA) return PALETTE_SIZE / 2;
        return clamp(Math.round(gray / 255.0f * (PALETTE_SIZE - 1)),0,PALETTE_SIZE - 1);
    }

    private static int patternLevel(@Nullable PatternedCabinetControlMap patternedCabinetControlMap,float x,float z) {
        if(patternedCabinetControlMap == null) return -1;
        for(PatternedCabinetControlMap.Region region : patternedCabinetControlMap.regions()) {
            if(region.layer() != PatternedCabinetControlMap.Layer.BODY_SHADE) continue;
            if(x < region.minX() || x >= region.maxX() || z < region.minY() || z >= region.maxY()) continue;
            float shade=region.shade() - PATTERN;
            if(shade <= -0.28f) return 0;
            if(shade < -0.02f)  return 1;
            if(shade > 0.16f) return 4;
            if(shade > 0.02f) return 3;
            return 2;
        }
        return -1;
    }

    private static List<Region> mergeRegion(int[][] color) {
        boolean[][] used=new boolean[SIZE][SIZE];
        List<Region> result=new ArrayList<>();
        for(int z=0;z < SIZE;z++) {
            for (int x=0;x < SIZE;x++) {
                if(used[z][x]) continue;
                int colors=color[z][x];
                int width=1;
                int height=1;
                boolean canGrow=true;
                while (x + width < SIZE && !used[z][x + width] && color[z][x + width]  == colors) {
                    width++;
                }
                while (z + height < SIZE && canGrow) {
                    for (int indexX=x; indexX < x + width; indexX++) {
                        if(used[z + height][indexX] || color[z + height][indexX] != colors) {
                            canGrow=false;
                            break;
                        }
                    }
                    if(canGrow) height++;
                }
                for(int indexZ=z;indexZ < z + height;indexZ++) {
                    for(int indexX=x;indexX < x + width;indexX++) {
                        used[indexZ][indexX]=true;
                    }
                }
                result.add(new Region(x,z,x + width,z + height,colors));
            }
        }
        return List.copyOf(result);
    }

    private static int surfaceCoordinate(int coordinate,int size) {
        if(size <= 1) return 0;
        float normal=(coordinate + 0.5f) / SIZE;
        return clamp((int) Math.floor(normal * size),0,size - 1);
    }

    private static int clamp(int value,int min,int max) {
        return Math.max(min,Math.min(max,value));
    }

    public record Surface(List<Region> regions) {
        public Surface {
            regions=List.copyOf(regions);
        }
    }

    public record Region(float minX,float minZ,float maxX,float maxZ,int color) {

    }

    private record Key(TextureAtlasSprite textureAtlasSprite,int tint,@Nullable PatternedCabinetControlMap patternedCabinetControlMap) {

    }

    private record Pixel(int red,int green,int blue,float light) {

    }

    private record Palette(int[] color) {
        private Palette {
            color=color.clone();
        }

        private int color(int level) {
            return this.color[clamp(level,0,this.color.length - 1)];
        }

        private static Palette palette(TextureAtlasSprite textureAtlasSprite,int tint) {
            int width=textureAtlasSprite.contents().width();
            int height=textureAtlasSprite.contents().height();
            int tintRed=tint < 0 ? 255 : tint >> 16 & 255;
            int tintGreen=tint < 0 ? 255 : tint >> 8 & 255;
            int tintBlue=tint < 0 ? 255 : tint & 255;
            List<Pixel> pixels=new ArrayList<>();
            for(int y=0;y < height;y++) {
                for (int x=0;x < width;x++) {
                    int rgba=textureAtlasSprite.getPixelRGBA(0,x,y);
                    if((rgba >>> 24 & 255) < MIN_ALPHA) continue;
                    int red=multiply(rgba & 255,tintRed);
                    int green=multiply(rgba >>> 8 & 255,tintGreen);
                    int blue=multiply(rgba >>> 16 & 255,tintBlue);
                    float light=red * 0.2126f + green * 0.7152f + blue * 0.0722f;
                    pixels.add(new Pixel(red,green,blue,light));
                }
            }
            if (pixels.isEmpty()) return new Palette(new int[] {
                    0x5A3B20,0x79512B,0x986A3A,0xB9854D,0xD8AA6A
            });
            pixels.sort(Comparator.comparingDouble(Pixel::light));
            int[] color=new int[PALETTE_SIZE];
            for(int level=0;level < PALETTE_SIZE;level++) {
                int start=level * pixels.size() / PALETTE_SIZE;
                int end=(level + 1) * pixels.size() / PALETTE_SIZE;
                if(end <= start) end=Math.min(pixels.size(),start + 1);
                long red=0L;
                long green=0L;
                long blue=0L;
                for(int index=start;index < end;index++) {
                    Pixel pixel=pixels.get(index);
                    red += pixel.red();
                    green += pixel.green();
                    blue += pixel.blue();
                }
                int count=Math.max(1,end - start);
                color[level]=rgb(Math.round(red / (float) count),Math.round(green / (float) count),Math.round(blue / (float) count));
            }
            return new Palette(stabilize(color));
        }

        private static int[] stabilize(int[] color) {
            int[] result=color.clone();
            for(int index=1;index < result.length;index++) {
                int previous=light(result[index - 1]);
                int current=light(result[index]);
                if(current < previous + 8) result[index]=lights(result[index],previous + 8 - current);
            }
            return result;
        }

        private static int multiply(int multiply,int tint) {
            return Math.round(multiply * tint / 255.0f);
        }

        private static int light(int color) {
            int red=color >> 16 & 255;
            int green=color >> 8 & 255;
            int blue=color & 255;
            return Math.round(red * 0.2126f + green * 0.7152f + blue * 0.0722f);
        }

        private static int lights(int color,int value) {
            return rgb(clamp((color >> 16 & 255) + value,0,255),clamp((color >> 8 & 255) + value,0,255),clamp((color & 255) + value,0,255));
        }

        private static int rgb(int red,int green,int blue) {
            return clamp(red,0,255) << 16 | clamp(green,0,255) << 8 | clamp(blue,0,255);
        }
    }
}
