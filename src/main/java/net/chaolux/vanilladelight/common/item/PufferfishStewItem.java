package net.chaolux.vanilladelight.common.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import javax.annotation.Nullable;
import java.util.List;

public class PufferfishStewItem extends ConsumableItem {

    public PufferfishStewItem(Properties properties) {
        super(properties);
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

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            MobEffectInstance regen=new MobEffectInstance(MobEffects.REGENERATION,200,3);
            MobEffectInstance poison=new MobEffectInstance(MobEffects.POISON,200,3);
            MutableComponent regenText=effectText(regen).withStyle(ChatFormatting.BLUE);
            MutableComponent poisonText=effectText(poison).withStyle(ChatFormatting.RED);
            Component orText=Component.translatable("vanilladelight.tooltip.or").withStyle(ChatFormatting.GOLD);
            tooltip.add(Component.empty().append(poisonText).append(Component.literal(" ").append(orText).append(" ").append(regenText)));
        }
    }

    private static MutableComponent effectText(MobEffectInstance instance) {
        MutableComponent component=Component.translatable(instance.getDescriptionId());
        if (instance.getAmplifier() > 0) {
            component = Component.translatable("potion.withAmplifier", component, Component.translatable("potion.potency." + instance.getAmplifier()));
        }

        if (instance.getDuration() > 20) {
            component = Component.translatable("potion.withDuration", component, MobEffectUtil.formatDuration(instance, 1.0f,20.0f));
        }
        return component;
    }
}
