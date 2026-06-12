package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.CampfireCookingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistries;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.StoveBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.mixin.accessor.RecipeManagerAccessor;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class CommonCuttingBoardBlockEntity extends SyncedBlockEntity {
    private final ItemStackHandler inventory = this.createHandler();
    private final LazyOptional<IItemHandler> inputHandler = LazyOptional.of(() -> this.inventory);
    private ResourceLocation lastRecipeID;
    private boolean isItemCarvingBoard = false;

    public CommonCuttingBoardBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), pos, state);
    }

    public void load(CompoundTag compound) {
        super.load(compound);
        this.isItemCarvingBoard = compound.getBoolean("IsItemCarved");
        this.inventory.deserializeNBT(compound.getCompound("Inventory"));
    }

    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.put("Inventory", this.inventory.serializeNBT());
        compound.putBoolean("IsItemCarved", this.isItemCarvingBoard);
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (this.level == null) {
            return false;
        } else if (this.isItemCarvingBoard) {
            return false;
        } else {
            Optional<CuttingBoardRecipe> matchingRecipe = this.getMatchingRecipe(new RecipeWrapper(this.inventory), toolStack, player);
            matchingRecipe.ifPresent((recipe) -> {
                for(ItemStack resultStack : recipe.rollResults(this.level.random, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, toolStack), new RecipeWrapper(this.inventory))) {
                    Direction direction = ((Direction)this.getBlockState().getValue(CuttingBoardBlock.FACING)).getCounterClockWise();
                    ItemUtils.spawnItemEntity(this.level, resultStack.copy(), (double)this.worldPosition.getX() + (double)0.5F + (double)direction.getStepX() * 0.2, (double)this.worldPosition.getY() + 0.2, (double)this.worldPosition.getZ() + (double)0.5F + (double)direction.getStepZ() * 0.2, (double)((float)direction.getStepX() * 0.2F), (double)0.0F, (double)((float)direction.getStepZ() * 0.2F));
                }

                if (player != null) {
                    toolStack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(EquipmentSlot.MAINHAND));
                    player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
                } else if (toolStack.hurt(1, this.level.random, (ServerPlayer)null)) {
                    toolStack.setCount(0);
                }

                Level patt4553$temp = this.level;
                if (patt4553$temp instanceof ServerLevel serverLevel) {
                    this.spawnCuttingParticles(serverLevel, this.getBlockPos(), this.getStoredItem());
                }

                this.playProcessingSound(recipe.getSoundEventID(), toolStack, this.getStoredItem());
                this.inventory.extractItem(0, 1, false);
                if (player instanceof ServerPlayer) {
                    ModAdvancements.CUTTING_BOARD.trigger((ServerPlayer)player);
                    if (!this.getStoredItem().isEmpty()) {
                        player.displayClientMessage(TextUtils.block("cutting_board.remaining_items", new Object[]{this.getStoredItem().getCount()}), true);
                    } else {
                        player.displayClientMessage(Component.empty(), true);
                    }
                }

            });
            return matchingRecipe.isPresent();
        }
    }

    private Optional<CuttingBoardRecipe> getMatchingRecipe(RecipeWrapper recipeWrapper, ItemStack toolStack, @Nullable Player player) {
        if (this.level == null) {
            return Optional.empty();
        } else {
            if (this.lastRecipeID != null) {
                Recipe<RecipeWrapper> recipe = (Recipe)((RecipeManagerAccessor)this.level.getRecipeManager()).getRecipeMap((RecipeType) ModRecipeTypes.CUTTING.get()).get(this.lastRecipeID);
                if (recipe instanceof CuttingBoardRecipe && recipe.matches(recipeWrapper, this.level) && ((CuttingBoardRecipe)recipe).getTool().test(toolStack)) {
                    return Optional.of((CuttingBoardRecipe)recipe);
                }
            }

            List<CuttingBoardRecipe> recipeList = this.level.getRecipeManager().getRecipesFor((RecipeType)ModRecipeTypes.CUTTING.get(), recipeWrapper, this.level);
            if (recipeList.isEmpty()) {
                if (player != null) {
                    player.displayClientMessage(TextUtils.block("cutting_board.invalid_item", new Object[0]), true);
                }

                return Optional.empty();
            } else {
                Optional<CuttingBoardRecipe> recipe = recipeList.stream().filter((cuttingRecipe) -> cuttingRecipe.getTool().test(toolStack)).findFirst();
                if (recipe.isEmpty()) {
                    if (player != null) {
                        player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool", new Object[0]), true);
                    }

                    return Optional.empty();
                } else {
                    this.lastRecipeID = ((CuttingBoardRecipe)recipe.get()).getId();
                    return recipe;
                }
            }
        }
    }
    public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), (double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.2, (double)pos.getZ() + (double)0.5F, 5, 0.1, 0.1, 0.1, 0.05);
    }

    public void playProcessingSound(String soundEventID, ItemStack tool, ItemStack boardItem) {
        SoundEvent sound = (SoundEvent) ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(soundEventID));
        if (sound != null) {
            this.playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(Items.SHEARS)) {
            this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(CommonTags.Items.TOOLS_KNIVES)) {
            this.playSound((SoundEvent) ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
        } else {
            Item item = boardItem.getItem();
            if (item instanceof BlockItem) {
                BlockItem blockItem = (BlockItem)item;
                Block block = blockItem.getBlock();
                SoundType soundType = block.defaultBlockState().getSoundType();
                this.playSound(soundType.getBreakSound(), 1.0F, 0.8F);
            } else {
                this.playSound(SoundEvents.WOOD_BREAK, 1.0F, 0.8F);
            }
        }

    }

    public void playSound(SoundEvent sound, float volume, float pitch) {
        if (this.level != null) {
            this.level.playSound((Player)null, (double)((float)this.worldPosition.getX() + 0.5F), (double)((float)this.worldPosition.getY() + 0.5F), (double)((float)this.worldPosition.getZ() + 0.5F), sound, SoundSource.BLOCKS, volume, pitch);
        }

    }

    public boolean canAddItem(ItemStack addedStack) {
        if (!this.isItemCarvingBoard && !addedStack.isEmpty()) {
            return this.inventory.insertItem(0, addedStack.copy(), true).getCount() != addedStack.getCount();
        } else {
            return false;
        }
    }

    public ItemStack addItem(ItemStack addedStack) {
        return !this.isItemCarvingBoard ? this.inventory.insertItem(0, addedStack.copy(), false) : addedStack;
    }

    public ItemStack removeItem() {
        this.isItemCarvingBoard = false;
        return this.inventory.extractItem(0, this.getMaxStackSize(), false);
    }

    public boolean carveToolOnBoard(ItemStack toolStack) {
        if ((toolStack.getItem() instanceof TieredItem || toolStack.getItem() instanceof TridentItem || toolStack.getItem() instanceof ShearsItem) && this.addItem(toolStack) == ItemStack.EMPTY) {
            this.isItemCarvingBoard = true;
            return true;
        } else {
            return false;
        }
    }

    public IItemHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getStoredItem() {
        return this.inventory.getStackInSlot(0);
    }

    public int getMaxStackSize() {
        return this.inventory.getSlotLimit(0);
    }

    public boolean isEmpty() {
        return this.inventory.getStackInSlot(0).isEmpty();
    }

    public boolean isItemCarvingBoard() {
        return this.isItemCarvingBoard;
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return cap.equals(ForgeCapabilities.ITEM_HANDLER) ? this.inputHandler.cast() : super.getCapability(cap, side);
    }

    public void setRemoved() {
        super.setRemoved();
        this.inputHandler.invalidate();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler() {
            protected void onContentsChanged(int slot) {
                CommonCuttingBoardBlockEntity.this.inventoryChanged();
            }
        };
    }

    public void clearContent() {
        ItemUtils.clearItems(this.inventory);
    }
}
