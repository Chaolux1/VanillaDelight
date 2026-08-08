package net.chaolux.vanilladelight.common.furniture;

import net.minecraft.util.Mth;

import java.util.Locale;

public enum ModularStoveFireStyle {
    ORANGE("orange",0xFF8A22,0xFFD45A),BLUE("blue",0x2CD9FF,0x5EA8FF),PURPLE("purple",0xB45CFF,0xFF77F4),GREEN("green",0x62FF62,0xB4FF5A),RAINBOW("rainbow",0xFF5252,0x49BFFF);
    private static final int[] RAINBOW_COLOR= {
            0xFF5252, 0xFFB52E, 0xFFF35A, 0x55E57A, 0x49BFFF, 0xB45CFF
    };
    private final String id;
    private final int firstColor;
    private final int secondColor;

    ModularStoveFireStyle(String id,int firstColor,int secondColor) {
        this.id=id;
        this.firstColor=firstColor;
        this.secondColor=secondColor;
    }

    public String id() {
        return this.id;
    }

    public int firstColor() {
        return this.firstColor;
    }

    public int particleColor(long tick,int seed) {
        if(this == RAINBOW) {
            float previousHue=tick * 0.018f + seed + 0.137f;
            float hue=previousHue - Mth.floor(previousHue);
            return Mth.hsvToRgb(hue,0.78f,1.0f);
        }
        return ((tick / 3L + seed) & 1L) == 0L ? this.firstColor : this.secondColor;
    }

    public int[] modelColor() {
        if(this == RAINBOW) return RAINBOW_COLOR.clone();
        return new int[] {
                this.firstColor
        };
    }

    public static ModularStoveFireStyle fromId(String id) {
        if(id == null || id.isBlank()) return ORANGE;
        String normal=id.toLowerCase(Locale.ROOT);
        for(ModularStoveFireStyle stoveFireStyle : values()) {
            if(stoveFireStyle.id.equals(normal)) return stoveFireStyle;
        }
        return ORANGE;
    }

}
