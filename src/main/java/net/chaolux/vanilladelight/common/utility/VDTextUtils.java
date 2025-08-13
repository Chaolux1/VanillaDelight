package net.chaolux.vanilladelight.common.utility;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class VDTextUtils {
    public static MutableComponent getTranslation(String key, Object... args) {
        return Component.translatable("vanilladelight." + key, args);
    }
}
