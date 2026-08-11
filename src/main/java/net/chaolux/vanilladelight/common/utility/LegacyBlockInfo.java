package net.chaolux.vanilladelight.common.utility;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LegacyBlockInfo {
    public static final String VERSION="1.0.6";
    public static final String REMOVE_VERSION="1.0.8";
    public static final String VERSION_KEY="VaniilaDelightLegacy";

    public static void appendLegacy(List<Component> componentList) {
        componentList.add(Component.translatable("tooltip.vanilladelight.legacy_block").withStyle(ChatFormatting.DARK_AQUA));
        componentList.add(Component.translatable("tooltip.vanilladelight.legacy_block.remove",REMOVE_VERSION,VERSION).withStyle(ChatFormatting.RED));
    }

    public static Component join() {
        return Component.literal("[Vanilla Delight] ").withStyle(ChatFormatting.GOLD).append(Component.translatable("message.vanilladelight.legacy_warning",REMOVE_VERSION,VERSION).withStyle(ChatFormatting.DARK_PURPLE));
    }
}
