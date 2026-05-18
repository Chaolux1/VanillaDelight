package net.chaolux.vanilladelight.common.block;

import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.MathUtils;

import java.util.Optional;

public class CommonStoveBlock extends StoveBlock {
    public CommonStoveBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntityTypes.COMMON_STOVE.get().create(pos,state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return (Boolean)state.getValue(LIT) ? createTickerHelper(blockEntityType, (BlockEntityType) ModBlockEntityTypes.COMMON_STOVE.get(), level.isClientSide ? CommonStoveBlockEntity::animationTick : net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity::cookingTick) : null;
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack heldStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item heldItem = heldStack.getItem();
        if ((Boolean)state.getValue(LIT)) {
            if (heldStack.canPerformAction(ItemAbilities.SHOVEL_DIG)) {
                this.extinguish(player, level, pos, state);
                heldStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                return ItemInteractionResult.SUCCESS;
            }

            if (heldItem == Items.WATER_BUCKET) {
                if (!level.isClientSide()) {
                    level.playSound((Player)null, pos, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 1.0F, 1.0F);
                }

                this.extinguish(player, level, pos, state);
                if (!player.isCreative()) {
                    player.setItemInHand(hand, new ItemStack(Items.BUCKET));
                }

                return ItemInteractionResult.SUCCESS;
            }
        } else {
            if (heldItem instanceof FlintAndSteelItem) {
                level.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, MathUtils.RAND.nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                heldStack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));
                return ItemInteractionResult.SUCCESS;
            }

            if (heldItem instanceof FireChargeItem) {
                level.playSound((Player)null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, (MathUtils.RAND.nextFloat() - MathUtils.RAND.nextFloat()) * 0.2F + 1.0F);
                level.setBlock(pos, (BlockState)state.setValue(BlockStateProperties.LIT, Boolean.TRUE), 11);
                if (!player.isCreative()) {
                    heldStack.shrink(1);
                }

                return ItemInteractionResult.SUCCESS;
            }
        }

        BlockEntity tileEntity = level.getBlockEntity(pos);
        if (tileEntity instanceof CommonStoveBlockEntity stoveEntity) {
            int stoveSlot = stoveEntity.getNextEmptySlot();
            if (stoveSlot < 0 || stoveEntity.isStoveBlockedAbove()) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }

            Optional<RecipeHolder<CampfireCookingRecipe>> recipe = stoveEntity.getMatchingRecipe(heldStack);
            if (recipe.isPresent()) {
                if (!level.isClientSide && stoveEntity.addItem(player.getAbilities().instabuild ? heldStack.copy() : heldStack, (RecipeHolder)recipe.get(), stoveSlot)) {
                    return ItemInteractionResult.SUCCESS;
                }

                return ItemInteractionResult.CONSUME;
            }
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof CommonStoveBlockEntity stoveBlockEntity) {
                ItemUtils.dropItems(level, pos, stoveBlockEntity.getInventory());
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }

    }
}
