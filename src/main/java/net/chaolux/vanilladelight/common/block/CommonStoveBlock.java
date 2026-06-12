package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.ToolActions;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.StoveBlockEntity;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import javax.annotation.Nullable;
import java.util.Optional;

public class CommonStoveBlock extends StoveBlock {
    public CommonStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COMMON_STOVE.get().create(pos,state);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide && (Boolean)state.getValue(LIT) ? createTickerHelper(blockEntityType, (BlockEntityType) ModBlockEntityTypes.COMMON_STOVE.get(), CommonStoveBlockEntity::particleTick) : createStoveTicker(level, blockEntityType, (BlockEntityType) ModBlockEntityTypes.COMMON_STOVE.get());
    }
}
