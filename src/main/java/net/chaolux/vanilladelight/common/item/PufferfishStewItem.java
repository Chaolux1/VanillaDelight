package net.chaolux.vanilladelight.common.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import vectorwing.farmersdelight.common.item.ConsumableItem;

public class PufferfishStewItem extends ConsumableItem {
    public PufferfishStewItem(Properties properties) {
        super(properties,true,false);
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide) return;
        if(level.random.nextBoolean()) {
            consumer.addEffect(new MobEffectInstance(MobEffects.POISON,200,3));
            level.playSound(null,consumer.blockPosition(), SoundEvents.PUFFER_FISH_BLOW_UP, SoundSource.PLAYERS,0.8f,0.8f);
        } else {
            consumer.addEffect(new MobEffectInstance(MobEffects.REGENERATION,200,3));
            level.playSound(null,consumer.blockPosition(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.PLAYERS,0.8f,0.8f);
        }
    }
}
