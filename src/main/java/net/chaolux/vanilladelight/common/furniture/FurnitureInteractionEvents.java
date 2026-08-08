package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.FORGE)
public class FurnitureInteractionEvents {
    private FurnitureInteractionEvents() {

    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if(!event.getEntity().isShiftKeyDown()) return;
        BlockEntity blockEntity=event.getLevel().getBlockEntity(event.getPos());
        if(!(blockEntity instanceof FurnitureAppearanceHolder)) return;
        ItemStack itemStack=event.getItemStack();
        boolean material=itemStack.getItem() instanceof BlockItem;
        boolean wax=itemStack.canPerformAction(ToolActions.AXE_WAX_OFF);
        if(!material && !wax) return;
        event.setUseBlock(Event.Result.ALLOW);
        event.setUseItem(Event.Result.DENY);
    }

}
