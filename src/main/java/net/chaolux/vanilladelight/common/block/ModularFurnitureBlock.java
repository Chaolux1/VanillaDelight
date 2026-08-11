package net.chaolux.vanilladelight.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.furniture.FurnitureInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class ModularFurnitureBlock extends BaseEntityBlock implements FurnitureDefintionProvider {
    public static final MapCodec<ModularFurnitureBlock> CODEC= RecordCodecBuilder.mapCodec(modularFurnitureBlockInstance -> modularFurnitureBlockInstance.group(propertiesCodec(),ResourceLocation.CODEC.fieldOf("definition").forGetter(ModularFurnitureBlock::getFurnitureDefintionId)).apply(modularFurnitureBlockInstance,ModularFurnitureBlock::new));
    public static final DirectionProperty FACING= BlockStateProperties.HORIZONTAL_FACING;
    private final ResourceLocation id;

    public ModularFurnitureBlock(Properties properties, ResourceLocation resourceLocation) {
        super(properties);
        this.id=resourceLocation;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING,Direction.NORTH));
    }

    @Override
    public ResourceLocation getFurnitureDefintionId() {
        return this.id;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING,context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING,rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block,BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return FurnitureRegistry.get(this.id).shape(blockState.getValue(FACING));
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        InteractionResult interactionResult=FurnitureInteractionHandler.use(blockState,level,blockPos,player,hand,blockHitResult);
        return toItemInteractionResult(interactionResult,level.isClientSide);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState blockState,Level level,BlockPos blockPos,Player player,BlockHitResult blockHitResult) {
        return FurnitureInteractionHandler.use(blockState,level,blockPos,player,InteractionHand.MAIN_HAND,blockHitResult);
    }

    protected static ItemInteractionResult toItemInteractionResult(InteractionResult interactionResult,boolean client) {
        if(interactionResult == InteractionResult.PASS) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(interactionResult == InteractionResult.FAIL) return ItemInteractionResult.FAIL;
        if(interactionResult == InteractionResult.CONSUME) return ItemInteractionResult.CONSUME;
        return ItemInteractionResult.sidedSuccess(client);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos,BlockState blockState) {
        return new FurnitureBlockEntity(blockPos,blockState);
    }

    @Override
    public BlockState playerWillDestroy(Level level,BlockPos blockPos,BlockState blockState,Player player) {
        if(!level.isClientSide && !player.getAbilities().instabuild) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof FurnitureBlockEntity furnitureBlockEntity) {
                furnitureBlockEntity.dropInstallMaterial();
            }
        }
       return super.playerWillDestroy(level,blockPos,blockState,player);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
