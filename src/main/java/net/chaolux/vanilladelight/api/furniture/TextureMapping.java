package net.chaolux.vanilladelight.api.furniture;

public record TextureMapping(Mode mode,float scaleU,float scaleV,float offsetU,float offsetV,int rotation,boolean mirrorU,boolean mirrorV,float cropMinU,float cropMinV, float cropMaxU, float cropMaxV) {
    public static final TextureMapping DEFAULT=new TextureMapping(Mode.PRESERVE_SOURCE_FACES,1.0f,1.0f,0.0f,0.0f,0,false,false,0.0f,0.0f,1.0f,1.0f);

    public TextureMapping {
        if(mode == null) mode = Mode.PRESERVE_SOURCE_FACES;
        if(scaleU == 0.0f) scaleU = 1.0f;
        if(scaleV == 0.0f) scaleV = 1.0f;
        rotation=Math.floorMod(rotation,360);
        if(rotation % 90 != 0) throw new IllegalArgumentException("Texture rotation must be divisible by 90");
        cropMinU=clamp(cropMinU);
        cropMinV=clamp(cropMinV);
        cropMaxU=clamp(cropMaxU);
        cropMaxV=clamp(cropMaxV);
        if(cropMaxU < cropMinU || cropMaxV < cropMinV) throw new IllegalArgumentException("Invalid texture crop");
    }

    private static float clamp(float value) {
        return Math.max(0.0f,Math.min(1.0f,value));
    }

    public enum Mode {
        STRETCH,TILE,CROP,PRESERVE_SCALE,PRESERVE_SOURCE_FACES
    }
}
