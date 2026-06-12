package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.utility.VDTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class GlowBerriesJamItem extends DrinkableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public GlowBerriesJamItem(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = true;
        this.hasCustomTooltip = true;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide) return;
        double radius=25.0;
        List<LivingEntity> mobs=level.getEntitiesOfClass(LivingEntity.class,consumer.getBoundingBox().inflate(radius),entity -> entity != consumer && entity.isAlive());
        for(LivingEntity livingEntity : mobs) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.GLOWING,200,0,false,false));
        }
        if(consumer instanceof Player player) {
            level.playSound(null,player.blockPosition(), SoundEvents.GLOW_SQUID_AMBIENT, SoundSource.PLAYERS,0.8f,1.0f);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = VDTextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }

            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip, 1.0F);
            }
        }
    }
}
