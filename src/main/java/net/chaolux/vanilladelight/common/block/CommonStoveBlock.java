package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.chaolux.vanilladelight.common.utility.LegacyBlockInfo;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.level.BlockGetter;
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
import java.util.List;
import java.util.Optional;

public class CommonStoveBlock extends StoveBlock {
    public CommonStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return this.getStoveBlockEntity().create(pos,state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        BlockEntityType<CommonStoveBlockEntity> commonStoveBlockEntityBlockEntityType=extendStoveBlockEntity(this.getStoveBlockEntity());
        return level.isClientSide && state.getValue(LIT) ? createTickerHelper(blockEntityType, commonStoveBlockEntityBlockEntityType, CommonStoveBlockEntity::particleTick) : createStoveTicker(level, blockEntityType, commonStoveBlockEntityBlockEntityType);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack,blockGetter,componentList,tooltipFlag);
       if(this.showLegacy()) LegacyBlockInfo.appendLegacy(componentList);
    }

    protected boolean showLegacy() {
        return true;
    }

    protected BlockEntityType<? extends CommonStoveBlockEntity> getStoveBlockEntity() {
        return ModBlockEntityTypes.COMMON_STOVE.get();
    }

    @SuppressWarnings("unchecked")
    private static BlockEntityType<CommonStoveBlockEntity> extendStoveBlockEntity(BlockEntityType<? extends CommonStoveBlockEntity> blockEntityType) {
        return (BlockEntityType<CommonStoveBlockEntity>) blockEntityType;
    }
}
