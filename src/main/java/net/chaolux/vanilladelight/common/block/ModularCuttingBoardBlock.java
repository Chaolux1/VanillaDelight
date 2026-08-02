package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.ModularCuttingBoardBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.item.CuttingBoardPatternItem;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ModularCuttingBoardBlock extends CommonCuttingBoard implements FurnitureDefintionProvider {
    private final ResourceLocation resourceLocation;

    public ModularCuttingBoardBlock(Properties properties,ResourceLocation resourceLocation) {
        super(properties);
        this.resourceLocation=resourceLocation;
    }

    @Override
    public ResourceLocation getFurnitureDefintionId() {
        return this.resourceLocation;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        ItemStack itemStack=player.getItemInHand(interactionHand);
        if(itemStack.getItem() instanceof CuttingBoardPatternItem cuttingBoardPatternItem) return this.applyPattern(level,blockPos,player,itemStack,cuttingBoardPatternItem);
        return super.use(blockState,level,blockPos,player,interactionHand,blockHitResult);
    }

    private InteractionResult applyPattern(Level level,BlockPos blockPos,Player player,ItemStack itemStack,CuttingBoardPatternItem cuttingBoardPatternItem) {
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(!(blockEntity instanceof ModularCuttingBoardBlockEntity modularCuttingBoardBlockEntity)) return InteractionResult.PASS;
        if(level.isClientSide) return InteractionResult.SUCCESS;
        if(modularCuttingBoardBlockEntity.getPattern() == cuttingBoardPatternItem.cuttingBoardPattern()) {
            player.displayClientMessage(Component.translatable("message.vanilladelight.modular_cutting_board.same_pattern"),true);
            return InteractionResult.CONSUME;
        }
        if(!modularCuttingBoardBlockEntity.setPattern(cuttingBoardPatternItem.cuttingBoardPattern())) return InteractionResult.CONSUME;
        if(!player.getAbilities().instabuild) itemStack.shrink(1);
        level.playSound(null,blockPos, SoundEvents.UI_LOOM_SELECT_PATTERN, SoundSource.BLOCKS,0.8f,1.0f);
        player.displayClientMessage(Component.translatable("message.vanilladelight.modular_cutting_board.pattern",Component.translatable(cuttingBoardPatternItem.cuttingBoardPattern().translationKey())),true);
        return InteractionResult.CONSUME;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntityTypes.MODULAR_CUTTING_BOARD.get().create(blockPos,blockState);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, @Nullable LivingEntity livingEntity,ItemStack itemStack) {
        super.setPlacedBy(level,blockPos,blockState,livingEntity,itemStack);
        BlockEntity blockEntity=level.getBlockEntity(blockPos);
        if(blockEntity instanceof ModularCuttingBoardBlockEntity modularCuttingBoardBlockEntity) modularCuttingBoardBlockEntity.applyItemAppearance(itemStack);
    }
}