package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.CommonCabinetBlockEntity;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;

public class CommonCabinetBlock extends CabinetBlock {
    public CommonCabinetBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COMMON_CABINET.get().create(pos,state);
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof CommonCabinetBlockEntity) {
                player.openMenu((CommonCabinetBlockEntity)tile);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CommonCabinetBlockEntity) {
            ((CommonCabinetBlockEntity)tileEntity).recheckOpen();
        }

    }
}
