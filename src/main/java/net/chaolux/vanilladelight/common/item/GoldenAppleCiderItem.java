package net.chaolux.vanilladelight.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.item.DrinkableItem;

public class GoldenAppleCiderItem extends DrinkableItem {
    public GoldenAppleCiderItem(Properties properties) {
        super(properties,true,false);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide) return;
        consumer.heal(2.0f);
        if(consumer instanceof Player player) {
            player.setAbsorptionAmount(Math.min(player.getAbsorptionAmount() + 4.0f,20.0f));
            level.playSound(null,player.blockPosition(), SoundEvents.AMETHYST_BLOCK_HIT, SoundSource.PLAYERS,0.6f,1.3f);
        }
    }
}
