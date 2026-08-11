package net.chaolux.vanilladelight.common.block;

import java.util.Locale;

public enum CuttingBoardPattern {
    PLAIN("plain"),SPIRAL("spiral"),FRAME("frame"),TILES("tiles"),LINES("lines"),DIAMOND("diamond");
    private final String id;
    CuttingBoardPattern(String string) {
        this.id=string;
    }

    public String id() {
        return this.id;
    }

    public String translationKey() {
        return "pattern.vanilladelight.modular_cutting_board." + this.id;
    }

    public static CuttingBoardPattern boardPattern(String string) {
        if(string == null || string.isBlank()) return PLAIN;
        String normalized=string.toLowerCase(Locale.ROOT);
        for (CuttingBoardPattern cuttingBoardPattern : values()) {
            if(cuttingBoardPattern.id.equals(normalized)) return cuttingBoardPattern;
        }
        return PLAIN;
    }
}
