package net.chaolux.vanilladelight.common.event;

import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.FORGE)
public class PumpkinPieEvent {
    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if(event.isCanceled()) return;
        ItemStack stack=event.getItemStack();
        if(!stack.is(Items.PUMPKIN_PIE)) return;
        Level level=event.getLevel();
        if(level.isClientSide()) return;
        Player player=event.getEntity();
        BlockPos pos=event.getPos().relative(event.getFace());
        BlockState state=ModBlocks.PUMPKIN_PIE.get().defaultBlockState();
        if(!player.mayUseItemAt(pos,event.getFace(),stack)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        if(!level.getBlockState(pos).canBeReplaced()) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        CollisionContext context=CollisionContext.of(player);
        if(!level.isUnobstructed(state,pos,context)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        if(!state.canSurvive(level,pos)) {
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        level.setBlock(pos, state, 3);
        if(!player.getAbilities().instabuild) stack.shrink(1);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack=event.getItemStack();
        if(!stack.is(Items.PUMPKIN_PIE)) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void onUseItem(LivingEntityUseItemEvent.Start event) {
        if(!(event.getEntity() instanceof Player)) return;
        ItemStack stack=event.getItem();
        if(!stack.is(Items.PUMPKIN_PIE)) return;
        event.setCanceled(true);
    }
}
