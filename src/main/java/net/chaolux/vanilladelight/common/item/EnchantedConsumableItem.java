package net.chaolux.vanilladelight.common.item;

import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class EnchantedConsumableItem extends ConsumableItem {
    public EnchantedConsumableItem(Properties properties) {
        super(properties,true);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}
