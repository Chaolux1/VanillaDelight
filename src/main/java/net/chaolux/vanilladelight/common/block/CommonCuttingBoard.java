package net.chaolux.vanilladelight.common.block;


import net.chaolux.vanilladelight.common.block.entity.CommonCuttingBoardBlockEntity;
import net.chaolux.vanilladelight.common.utility.LegacyBlockInfo;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;
import java.util.List;

public class CommonCuttingBoard extends CuttingBoardBlock {
    public CommonCuttingBoard(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COMMON_CUTTING_BOARD.get().create(pos,state);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CommonCuttingBoardBlockEntity cuttingBoard) {
            ItemStack mainHandStack = player.getMainHandItem();
            if (mainHandStack.isEmpty()) {
                if (!cuttingBoard.isEmpty() && !level.isClientSide) {
                    ItemStack removedStack = cuttingBoard.removeItem();
                    if (!player.isCreative()) {
                        player.getInventory().add(removedStack);
                    }

                    Vec3 centerPos = pos.getCenter();
                    level.playSound((Player)null, centerPos.x(), centerPos.y(), centerPos.z(), (SoundEvent) ModSounds.BLOCK_CUTTING_BOARD_REMOVE.get(), SoundSource.BLOCKS, 0.25F, 0.5F);
                    return ItemInteractionResult.SUCCESS;
                } else {
                    return ItemInteractionResult.CONSUME;
                }
            } else if (cuttingBoard.canAddItem(mainHandStack)) {
                if (level.isClientSide) {
                    return ItemInteractionResult.CONSUME;
                } else {
                    ItemStack remainderStack = cuttingBoard.addItem(player.getAbilities().instabuild ? mainHandStack.copy() : mainHandStack);
                    if (!player.isCreative()) {
                        player.setItemSlot(EquipmentSlot.MAINHAND, remainderStack);
                    }

                    Vec3 centerPos = pos.getCenter();
                    level.playSound((Player)null, centerPos.x(), centerPos.y(), centerPos.z(), (SoundEvent)ModSounds.BLOCK_CUTTING_BOARD_PLACE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
                    return ItemInteractionResult.SUCCESS;
                }
            } else {
                return cuttingBoard.processStoredItemUsingTool(mainHandStack, player) ? ItemInteractionResult.SUCCESS : ItemInteractionResult.CONSUME;
            }
        } else {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CommonCuttingBoardBlockEntity) {
                CommonCuttingBoardBlockEntity cuttingBoard = (CommonCuttingBoardBlockEntity)tileEntity;
                Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), cuttingBoard.getStoredItem());
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CommonCuttingBoardBlockEntity cuttingBoard) {
            ItemStack storedStack = cuttingBoard.getStoredItem();
            if (!storedStack.isEmpty()) {
                float proportions = (float)storedStack.getCount() / (float)Math.min(cuttingBoard.getMaxStackSize(), storedStack.getMaxStackSize());
                return Mth.floor(proportions * 14.0F) + 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack,tooltipContext,componentList,tooltipFlag);
        if(this.showLegacy()) LegacyBlockInfo.appendLegacy(componentList);
    }

    protected boolean showLegacy() {
        return true;
    }

    @EventBusSubscriber(modid = "vanilladelight", bus = Bus.GAME)
    public static class ToolCarvingEvent {
        public ToolCarvingEvent() {
        }

        @SubscribeEvent
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof CommonCuttingBoardBlockEntity cuttingBoard) {
                Player player = event.getEntity();
                ItemStack heldStack = player.getMainHandItem();
                if (player.isSecondaryUseActive() && !heldStack.isEmpty()) {
                    if (cuttingBoard.carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                        if (!player.isCreative()) {
                            player.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        }

                        Vec3 centerPos = pos.getCenter();
                        level.playSound((Player)null, centerPos.x(), centerPos.y(), centerPos.z(), (SoundEvent)ModSounds.BLOCK_CUTTING_BOARD_CARVE.get(), SoundSource.BLOCKS, 1.0F, 0.8F);
                        event.setCanceled(true);
                        event.setCancellationResult(InteractionResult.SUCCESS);
                    }

                }
            }
        }
    }
}
