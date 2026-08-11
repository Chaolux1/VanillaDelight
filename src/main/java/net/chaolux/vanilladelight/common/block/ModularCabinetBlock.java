package net.chaolux.vanilladelight.common.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ModularCabinetBlock extends ModularContainerFurnitureBlock{
    public static final MapCodec<ModularCabinetBlock> CODEC= RecordCodecBuilder.mapCodec(modularCabinetBlockInstance -> modularCabinetBlockInstance.group(propertiesCodec(),ResourceLocation.CODEC.fieldOf("definition").forGetter(ModularCabinetBlock::getFurnitureDefintionId)).apply(modularCabinetBlockInstance,ModularCabinetBlock::new));

    public ModularCabinetBlock(Properties properties, ResourceLocation resourceLocation) {
        super(properties,resourceLocation);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos,CollisionContext collisionContext) {
        return Shapes.block();
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return Shapes.empty();
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState blockState) {
        return false;
    }

    @Override
    public boolean isPathfindable(BlockState blockState, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    protected MapCodec<ModularCabinetBlock> codec() {
        return CODEC;
    }
}
