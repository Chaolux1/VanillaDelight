package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ResourceBundle;

public class ModularFurnitureBlock extends BaseEntityBlock implements FurnitureDefintionProvider {
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
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        return FurnitureInteractionHandler.use(blockState,level,blockPos,player,hand,blockHitResult);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos,BlockState blockState) {
        return new FurnitureBlockEntity(blockPos,blockState);
    }

    @Override
    public void playerWillDestroy(Level level,BlockPos blockPos,BlockState blockState,Player player) {
        if(!level.isClientSide && !player.getAbilities().instabuild) {
            BlockEntity blockEntity=level.getBlockEntity(blockPos);
            if(blockEntity instanceof FurnitureBlockEntity furnitureBlockEntity) {
                furnitureBlockEntity.dropInstallMaterial();
            }
        }

        super.playerWillDestroy(level,blockPos,blockState,player);
    }
}
