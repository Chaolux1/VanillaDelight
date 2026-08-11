package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.FurnitureContainerBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class ModularContainerFurnitureBlock extends ModularFurnitureBlock {
    public static final BooleanProperty OPEN=BooleanProperty.create("open");

    public ModularContainerFurnitureBlock(Properties properties, ResourceLocation resourceLocation) {
        super(properties,resourceLocation);
        this.registerDefaultState(this.defaultBlockState().setValue(OPEN,false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack,BlockState blockState,Level level,BlockPos blockPos,Player player,InteractionHand interactionHand,BlockHitResult blockHitResult) {
        InteractionResult interactionResult=FurnitureInteractionHandler.use(blockState,level,blockPos,player,interactionHand,blockHitResult);
        if(interactionResult !=InteractionResult.PASS) return toItemInteractionResult(interactionResult,level.isClientSide);
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState,Level level,BlockPos blockPos,Player player,BlockHitResult blockHitResult) {
        InteractionResult interactionResult=FurnitureInteractionHandler.use(blockState,level,blockPos,player,InteractionHand.MAIN_HAND,blockHitResult);
        if(interactionResult.consumesAction()) return interactionResult;
        if(!level.isClientSide) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof MenuProvider menuProvider) player.openMenu(menuProvider);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource randomSource) {
        BlockEntity blockEntity=serverLevel.getBlockEntity(blockPos);
        if(blockEntity instanceof FurnitureContainerBlockEntity furnitureContainerBlockEntity) furnitureContainerBlockEntity.recheckOpen();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity, ItemStack itemStack) {
        super.setPlacedBy(level,blockPos,blockState,livingEntity,itemStack);
        if(itemStack.has(DataComponents.CUSTOM_NAME) && level.getBlockEntity(blockPos) instanceof FurnitureContainerBlockEntity furnitureBlock) furnitureBlock.setCustomName(itemStack.getHoverName());
    }

    @Override
    public void onRemove(BlockState blockState,Level level,BlockPos blockPos,BlockState state,boolean moving) {
        if(!blockState.is(state.getBlock())) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof FurnitureContainerBlockEntity furnitureBlock) {
                Containers.dropContents(level,blockPos,furnitureBlock);
                level.updateNeighbourForOutputSignal(blockPos,this);
            }
            super.onRemove(blockState,level,blockPos,state,moving);
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState,Level level,BlockPos blockPos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(blockPos));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos,BlockState blockState) {
        return new FurnitureContainerBlockEntity(blockPos,blockState);
    }
}
