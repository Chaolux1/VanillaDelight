package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.utility.VDTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class CookedRedMushroomItem extends ConsumableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;

    public CookedRedMushroomItem(Item.Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = true;
        this.hasCustomTooltip = true;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        MobEffectInstance selectedEffect = consumer.getEffect(MobEffects.WITHER);
        if (selectedEffect != null && !MinecraftForge.EVENT_BUS.post(new MobEffectEvent.Remove(consumer, selectedEffect))) {
            consumer.removeEffect(MobEffects.WITHER);
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
