package net.chaolux.vanilladelight.common.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class CookedRedMushroomItem extends ConsumableItem {
    public CookedRedMushroomItem(Properties properties) {
        super(properties,true,false);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        MobEffectInstance selectedEffect = consumer.getEffect(MobEffects.WITHER);
        if (selectedEffect != null && !MinecraftForge.EVENT_BUS.post(new MobEffectEvent.Remove(consumer, selectedEffect))) {
            consumer.removeEffect(MobEffects.WITHER);
        }
    }
}
