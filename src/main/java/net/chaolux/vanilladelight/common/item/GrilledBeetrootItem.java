package net.chaolux.vanilladelight.common.item;

import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class GrilledBeetrootItem extends ConsumableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public GrilledBeetrootItem(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = false;
        this.hasCustomTooltip = true;
    }
    public int getUseDuration(ItemStack stack) {
        return 128;
    }

}

