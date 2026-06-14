package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.utility.VDTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class SweetFishSoupItem extends ConsumableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public SweetFishSoupItem(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = true;
        this.hasCustomTooltip = true;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide || !(consumer instanceof Player player)) return;
        double radius=8.0;
        List<Cat> cats=level.getEntitiesOfClass(Cat.class,consumer.getBoundingBox().inflate(radius), cat -> true);
        for(Cat cat : cats) {
            if(cat.isOrderedToSit()) {
                cat.setInSittingPose(false);
            }
            cat.getLookControl().setLookAt(player,30.0f,30.0f);
            PathNavigation navigation=cat.getNavigation();
            if(navigation !=null) {
                navigation.moveTo(player,2.0);
            }
        }
        level.playSound(null,player.blockPosition(), SoundEvents.CAT_PURR, SoundSource.PLAYERS,0.9f,1.0f);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.ENABLE_FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = VDTextUtils.getTranslation("tooltip." + BuiltInRegistries.ITEM.getKey(this).getPath(), new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }

            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
            }
        }
    }
}

