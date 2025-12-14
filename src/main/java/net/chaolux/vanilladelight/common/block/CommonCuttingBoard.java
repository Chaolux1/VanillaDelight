package net.chaolux.vanilladelight.common.block;


import net.chaolux.vanilladelight.common.block.entity.CommonCuttingBoardBlockEntity;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

public class CommonCuttingBoard extends CuttingBoardBlock {
    public CommonCuttingBoard(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COMMON_CUTTING_BOARD.get().create(pos,state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CommonCuttingBoardBlockEntity cuttingBoardEntity) {
            ItemStack heldStack = player.getItemInHand(hand);
            ItemStack offhandStack = player.getOffhandItem();
            if (cuttingBoardEntity.isEmpty()) {
                if (!offhandStack.isEmpty()) {
                    if (hand.equals(InteractionHand.MAIN_HAND) && !offhandStack.is(ModTags.OFFHAND_EQUIPMENT) && !(heldStack.getItem() instanceof BlockItem)) {
                        return InteractionResult.PASS;
                    }

                    if (hand.equals(InteractionHand.OFF_HAND) && offhandStack.is(ModTags.OFFHAND_EQUIPMENT)) {
                        return InteractionResult.PASS;
                    }
                }

                if (heldStack.isEmpty()) {
                    return InteractionResult.PASS;
                }

                if (cuttingBoardEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack)) {
                    level.playSound((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    return InteractionResult.SUCCESS;
                }
            } else {
                if (!heldStack.isEmpty()) {
                    ItemStack boardStack = cuttingBoardEntity.getStoredItem().copy();
                    if (cuttingBoardEntity.processStoredItemUsingTool(heldStack, player)) {
                        spawnCuttingParticles(level, pos, boardStack, 5);
                        return InteractionResult.SUCCESS;
                    }

                    return InteractionResult.CONSUME;
                }

                if (hand.equals(InteractionHand.MAIN_HAND)) {
                    if (!player.isCreative()) {
                        if (!player.getInventory().add(cuttingBoardEntity.removeItem())) {
                            Containers.dropItemStack(level, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), cuttingBoardEntity.removeItem());
                        }
                    } else {
                        cuttingBoardEntity.removeItem();
                    }

                    level.playSound((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.WOOD_HIT, SoundSource.BLOCKS, 0.25F, 0.5F);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return InteractionResult.PASS;
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
        if (blockEntity instanceof CommonCuttingBoardBlockEntity) {
            return !((CommonCuttingBoardBlockEntity)blockEntity).isEmpty() ? 15 : 0;
        } else {
            return 0;
        }
    }

    @Mod.EventBusSubscriber(modid = "vanilladelight", bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ToolCarvingEvent {
        public ToolCarvingEvent() {
        }

        @SubscribeEvent
        public static void onSneakPlaceTool(PlayerInteractEvent.RightClickBlock event) {
            Level level = event.getLevel();
            BlockPos pos = event.getPos();
            Player player = event.getEntity();
            ItemStack heldStack = player.getMainHandItem();
            BlockEntity tileEntity = level.getBlockEntity(event.getPos());
            if (player.isSecondaryUseActive() && !heldStack.isEmpty() && tileEntity instanceof CommonCuttingBoardBlockEntity && (heldStack.getItem() instanceof TieredItem || heldStack.getItem() instanceof TridentItem || heldStack.getItem() instanceof ShearsItem)) {
                boolean success = ((CommonCuttingBoardBlockEntity)tileEntity).carveToolOnBoard(player.getAbilities().instabuild ? heldStack.copy() : heldStack);
                if (success) {
                    level.playSound((Player)null, (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.8F);
                    event.setCanceled(true);
                    event.setCancellationResult(InteractionResult.SUCCESS);
                }
            }

        }
    }
}
