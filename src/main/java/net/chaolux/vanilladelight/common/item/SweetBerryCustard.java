package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.utility.VDTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class SweetBerryCustard extends DrinkableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public SweetBerryCustard(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = true;
        this.hasCustomTooltip = true;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide) return;
        double radius=3.0;
        List<LivingEntity> mobs=level.getEntitiesOfClass(LivingEntity.class,consumer.getBoundingBox().inflate(radius),entity -> entity != consumer && entity.isAlive() && !(entity instanceof Player));
        for(LivingEntity entity : mobs) {
            entity.hurt(entity.damageSources().thorns(consumer),2.0f);
            Vec3 direction=entity.position().subtract(consumer.position()).normalize().scale(0.4);
            entity.push(direction.x,0.1,direction.z);
        }
        level.levelEvent(2001,consumer.blockPosition(), Block.getId(Blocks.SWEET_BERRY_BUSH.defaultBlockState()));
        level.playSound(null,consumer.blockPosition(), SoundEvents.PLAYER_HURT_SWEET_BERRY_BUSH, SoundSource.PLAYERS,0.8f,1.0f);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
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
