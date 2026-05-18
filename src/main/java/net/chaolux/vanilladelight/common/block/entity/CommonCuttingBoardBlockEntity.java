package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;
import vectorwing.farmersdelight.common.block.entity.SyncedBlockEntity;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipeInput;
import vectorwing.farmersdelight.common.registry.ModAdvancements;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;
import vectorwing.farmersdelight.common.registry.ModSounds;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.utility.ItemUtils;
import vectorwing.farmersdelight.common.utility.TextUtils;
import net.chaolux.vanilladelight.common.block.CommonCuttingBoard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.MOD)
public class CommonCuttingBoardBlockEntity extends SyncedBlockEntity {
    private final ItemStackHandler inventory = this.createHandler();
    private final RecipeManager.CachedCheck<CuttingBoardRecipeInput, CuttingBoardRecipe> quickCheck;
    private ResourceLocation lastRecipeID;
    private boolean isItemCarvingBoard = false;

    public CommonCuttingBoardBlockEntity (BlockPos pos, BlockState state) {
        super((BlockEntityType) ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), pos, state);
        this.quickCheck = RecipeManager.createCheck((RecipeType)ModRecipeTypes.CUTTING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), (be, context) -> be.getInventory());
    }

    public void loadAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.loadAdditional(compound, registries);
        this.isItemCarvingBoard = compound.getBoolean("IsItemCarved");
        this.inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
    }

    public void saveAdditional(CompoundTag compound, HolderLookup.Provider registries) {
        super.saveAdditional(compound, registries);
        compound.put("Inventory", this.inventory.serializeNBT(registries));
        compound.putBoolean("IsItemCarved", this.isItemCarvingBoard);
    }

    public boolean processStoredItemUsingTool(ItemStack toolStack, @Nullable Player player) {
        if (this.level == null) return false;
        if (this.isItemCarvingBoard) return false;

        Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = this.getMatchingRecipe(toolStack, player);
        matchingRecipe.ifPresent((recipe) -> {
            int fortuneLevel = ItemUtils.getValidatedEnchantmentLevel(Enchantments.FORTUNE, this.level.registryAccess(), toolStack);
            List<ItemStack> results = recipe.value().rollResults(this.level.random, fortuneLevel, new RecipeWrapper(this.inventory));

            if (this.level instanceof ServerLevel serverLevel) {
                Direction direction = this.getBlockState().getValue(CommonCuttingBoard.FACING).getCounterClockWise();
                for (ItemStack result : results) {
                    ItemUtils.spawnItemEntity(serverLevel, result.copy(),
                            this.worldPosition.getX() + 0.5 + (direction.getStepX() * 0.2),
                            this.worldPosition.getY() + 0.2,
                            this.worldPosition.getZ() + 0.5 + (direction.getStepZ() * 0.2),
                            direction.getStepX() * 0.2F, 0.0F, direction.getStepZ() * 0.2F);
                }
                toolStack.hurtAndBreak(1, serverLevel, player, (item) -> {});
            }

            this.playProcessingSound(recipe.value().getSoundEvent().orElse(null), toolStack, this.getStoredItem());
            this.removeItem();
            if (player instanceof ServerPlayer serverPlayer) {
                ((CuttingBoardTrigger) ModAdvancements.USE_CUTTING_BOARD.get()).trigger(serverPlayer);
            }
        });
        return matchingRecipe.isPresent();
    }

    private Optional<RecipeHolder<CuttingBoardRecipe>> getMatchingRecipe(ItemStack toolStack, @Nullable Player player) {
        if (this.level == null) {
            return Optional.empty();
        } else {
            Optional<RecipeHolder<CuttingBoardRecipe>> recipe = this.quickCheck.getRecipeFor(new CuttingBoardRecipeInput(this.getStoredItem(), toolStack), this.level);
            if (recipe.isPresent()) {
                if (((CuttingBoardRecipe)((RecipeHolder)recipe.get()).value()).getTool().test(toolStack)) {
                    return recipe;
                }

                if (player != null) {
                    player.displayClientMessage(TextUtils.block("cutting_board.invalid_item"), true);
                }
            } else if (player != null) {
                player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool"), true);
            }

            return Optional.empty();
        }
    }

    public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
        if (sound != null) {
            this.playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(net.neoforged.neoforge.common.Tags.Items.TOOLS_SHEAR)) {
            this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(CommonTags.Items.TOOLS_KNIFE)) {
            this.playSound((SoundEvent)ModSounds.BLOCK_CUTTING_BOARD_KNIFE.get(), 0.8F, 1.0F);
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

    public boolean addItem(ItemStack itemStack) {
        if (this.isEmpty() && !itemStack.isEmpty()) {
            this.inventory.setStackInSlot(0, itemStack.split(1));
            this.isItemCarvingBoard = false;
            this.inventoryChanged();
            return true;
        } else {
            return false;
        }
    }

    public boolean carveToolOnBoard(ItemStack tool) {
        if (this.addItem(tool)) {
            this.isItemCarvingBoard = true;
            return true;
        } else {
            return false;
        }
    }

    public ItemStack removeItem() {
        if (!this.isEmpty()) {
            this.isItemCarvingBoard = false;
            ItemStack item = this.getStoredItem().split(1);
            this.inventoryChanged();
            return item;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public net.neoforged.neoforge.items.IItemHandler getInventory() {
        return this.inventory;
    }

    public ItemStack getStoredItem() {
        return this.inventory.getStackInSlot(0);
    }

    public boolean isEmpty() {
        return this.inventory.getStackInSlot(0).isEmpty();
    }

    public boolean isItemCarvingBoard() {
        return this.isItemCarvingBoard;
    }

    public void setRemoved() {
        super.setRemoved();
    }

    private net.neoforged.neoforge.items.ItemStackHandler createHandler() {
        return new net.neoforged.neoforge.items.ItemStackHandler() {
            public int getSlotLimit(int slot) {
                return 1;
            }

            protected void onContentsChanged(int slot) {
                CommonCuttingBoardBlockEntity.this.inventoryChanged();
            }
        };
    }
}
