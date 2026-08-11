package net.chaolux.vanilladelight.common.furniture;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.GAME)
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
        boolean wax=itemStack.canPerformAction(ItemAbilities.AXE_WAX_OFF);
        if(!material && !wax) return;
        event.setUseBlock(TriState.TRUE);
        event.setUseItem(TriState.FALSE);
    }

}
