package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.advancement.CuttingBoardTrigger;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
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
        this(ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), pos, state);
    }

    protected CommonCuttingBoardBlockEntity(BlockEntityType<?> blockEntityType,BlockPos blockPos,BlockState blockState) {
        super((BlockEntityType) blockEntityType,blockPos,blockState);
        this.quickCheck=RecipeManager.createCheck((RecipeType) ModRecipeTypes.CUTTING.get());
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), (be, context) -> be.getInventory());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK,ModBlockEntityTypes.MODULAR_CUTTING_BOARD.get(),(be,context) -> be.getInventory());
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
        if (this.level == null) {
            return false;
        } else if (this.isItemCarvingBoard) {
            return false;
        } else {
            Optional<RecipeHolder<CuttingBoardRecipe>> matchingRecipe = this.getMatchingRecipe(toolStack, player);
            matchingRecipe.ifPresent((recipe) -> {
                for(ItemStack resultStack : ((CuttingBoardRecipe)recipe.value()).rollResults(this.level.random, ItemUtils.getValidatedEnchantmentLevel(Enchantments.FORTUNE, this.level.registryAccess(), toolStack), new RecipeWrapper(this.inventory))) {
                    Direction direction = ((Direction)this.getBlockState().getValue(CuttingBoardBlock.FACING)).getCounterClockWise();
                    ItemUtils.spawnItemEntity(this.level, resultStack.copy(), (double)this.worldPosition.getX() + (double)0.5F + (double)direction.getStepX() * 0.2, (double)this.worldPosition.getY() + 0.2, (double)this.worldPosition.getZ() + (double)0.5F + (double)direction.getStepZ() * 0.2, (double)((float)direction.getStepX() * 0.2F), (double)0.0F, (double)((float)direction.getStepZ() * 0.2F));
                }

                if (!this.level.isClientSide) {
                    toolStack.hurtAndBreak(1, (ServerLevel)this.level, player, (item) -> {
                    });
                    if (player != null) {
                        player.awardStat(Stats.ITEM_USED.get(toolStack.getItem()));
                    }
                }

                Level patt0$temp = this.level;
                if (patt0$temp instanceof ServerLevel serverLevel) {
                    this.spawnCuttingParticles(serverLevel, this.getBlockPos(), this.getStoredItem());
                }

                this.playProcessingSound((SoundEvent)((CuttingBoardRecipe)recipe.value()).getSoundEvent().orElse((SoundEvent) null), toolStack, this.getStoredItem());
                this.inventory.extractItem(0, 1, false);
                if (player instanceof ServerPlayer) {
                    ((CuttingBoardTrigger)ModAdvancements.USE_CUTTING_BOARD.get()).trigger((ServerPlayer)player);
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
                    player.displayClientMessage(TextUtils.block("cutting_board.invalid_item", new Object[0]), true);
                }
            } else if (player != null) {
                player.displayClientMessage(TextUtils.block("cutting_board.invalid_tool", new Object[0]), true);
            }

            return Optional.empty();
        }
    }

    public void spawnCuttingParticles(ServerLevel level, BlockPos pos, ItemStack stack) {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, stack), (double)pos.getX() + (double)0.5F, (double)pos.getY() + 0.2, (double)pos.getZ() + (double)0.5F, 5, 0.1, 0.1, 0.1, 0.05);
    }

    public void playProcessingSound(@Nullable SoundEvent sound, ItemStack tool, ItemStack boardItem) {
        if (sound != null) {
            this.playSound(sound, 1.0F, 1.0F);
        } else if (tool.is(Tags.Items.TOOLS_SHEAR)) {
            this.playSound(SoundEvents.SHEEP_SHEAR, 1.0F, 1.0F);
        } else if (tool.is(vectorwing.farmersdelight.common.tag.CommonTags.Items.TOOLS_KNIFE)) {
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


    public net.neoforged.neoforge.items.IItemHandler getInventory() {
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

    public void setRemoved() {
        super.setRemoved();
    }

    private ItemStackHandler createHandler() {
        return new ItemStackHandler() {
            protected void onContentsChanged(int slot) {
                CommonCuttingBoardBlockEntity.this.inventoryChanged();
            }
        };
    }
}
