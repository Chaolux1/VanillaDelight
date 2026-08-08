package net.chaolux.vanilladelight.common.event;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlocks;

import java.util.Map;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.FORGE)
public class PickColorCookingPotEvent {
   private static final Map<Item, String> COLOR=Map.ofEntries(
           Map.entry(Items.WHITE_DYE, "white"),
           Map.entry(Items.LIGHT_GRAY_DYE, "light_gray"),
           Map.entry(Items.GRAY_DYE, "gray"),
           Map.entry(Items.BLACK_DYE, "black"),
           Map.entry(Items.BROWN_DYE, "brown"),
           Map.entry(Items.RED_DYE, "red"),
           Map.entry(Items.ORANGE_DYE, "orange"),
           Map.entry(Items.YELLOW_DYE, "yellow"),
           Map.entry(Items.LIME_DYE, "lime"),
           Map.entry(Items.GREEN_DYE, "green"),
           Map.entry(Items.CYAN_DYE, "cyan"),
           Map.entry(Items.LIGHT_BLUE_DYE, "light_blue"),
           Map.entry(Items.BLUE_DYE, "blue"),
           Map.entry(Items.PURPLE_DYE, "purple"),
           Map.entry(Items.MAGENTA_DYE, "magenta"),
           Map.entry(Items.PINK_DYE, "pink")
           );

   @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
       Level level=event.getLevel();
       if(level.isClientSide) return;
       ItemStack stack=event.getItemStack();
       if(stack.isEmpty()) return;
       String color=COLOR.get(stack.getItem());
       if(color == null) return;
       BlockPos pos=event.getPos();
       BlockState state=level.getBlockState(pos);
       if(!(state.getBlock() instanceof CookingPotBlock)) return;
       BlockEntity blockEntity=level.getBlockEntity(pos);
       if(!(blockEntity instanceof CookingPotBlockEntity pot)) return;
       pot.getPersistentData().putString("color", color);
       pot.setChanged();
       level.sendBlockUpdated(pos,state,state,3);
       if(!event.getEntity().isCreative()) stack.shrink(1);
       event.setCanceled(true);
       event.setCancellationResult(InteractionResult.SUCCESS);
   }
}
