package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.common.utility.VDTextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.item.DrinkableItem;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nullable;
import java.util.List;

public class CaramelBottleItem extends DrinkableItem {
    private final boolean hasFoodEffectTooltip;
    private final boolean hasCustomTooltip;
    public CaramelBottleItem(Properties properties) {
        super(properties);
        this.hasFoodEffectTooltip = false;
        this.hasCustomTooltip = true;
    }

    @Override
    public void affectConsumer(ItemStack stack, Level level, LivingEntity consumer) {
        if(level.isClientSide || !(consumer instanceof Player player)) return;
        double radius=4.0;
        AABB box=new AABB(player.getX()-radius,player.getY()-radius,player.getZ()-radius,player.getX()+radius,player.getY()+radius,player.getZ()+radius);
        List<ItemEntity> items=level.getEntitiesOfClass(ItemEntity.class,box,event -> !event.hasPickUpDelay());
        for(ItemEntity entity : items) {
            ItemStack itemStack=entity.getItem();
            if(itemStack.isEmpty()) continue;
            if(player.getInventory().add(itemStack.copy())) {
                entity.discard();
            } else {
                Vec3 direction=player.position().subtract(entity.position()).normalize().scale(0.5);
                entity.setDeltaMovement(entity.getDeltaMovement().add(direction));
            }
        }
        level.playSound(null,player.blockPosition(), SoundEvents.HONEY_BLOCK_STEP, SoundSource.PLAYERS,0.8f,1.2f);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag isAdvanced) {
        if ((Boolean) Configuration.FOOD_EFFECT_TOOLTIP.get()) {
            if (this.hasCustomTooltip) {
                MutableComponent textEmpty = VDTextUtils.getTranslation("tooltip." + this, new Object[0]);
                tooltip.add(textEmpty.withStyle(ChatFormatting.BLUE));
            }

            if (this.hasFoodEffectTooltip) {
                TextUtils.addFoodEffectTooltip(stack, tooltip::add, 1.0F, context.tickRate());
            }
        }
    }
}

