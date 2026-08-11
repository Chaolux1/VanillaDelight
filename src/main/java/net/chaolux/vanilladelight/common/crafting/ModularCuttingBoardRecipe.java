package net.chaolux.vanilladelight.common.crafting;

import com.google.gson.JsonObject;
import com.mojang.serialization.MapCodec;
import net.chaolux.vanilladelight.common.item.ModularCuttingBoardItem;
import net.chaolux.vanilladelight.common.tag.ModTags;
import net.chaolux.vanilladelight.registry.crafting.ModRecipeSerializers;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

public class ModularCuttingBoardRecipe implements CraftingRecipe {
    private static final int MATERIAL_COUNT=4;
    private final ShapedRecipe shapedRecipe;

    public ModularCuttingBoardRecipe(ShapedRecipe shapedRecipe) {
        this.shapedRecipe=shapedRecipe;
    }

    @Override
    public boolean matches(CraftingInput craftingInput, Level level) {
        return this.shapedRecipe.matches(craftingInput,level) && this.findMaterialBlock(craftingInput) != null;
    }

    @Override
    public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
        Block block=this.findMaterialBlock(craftingInput);
        return block == null ? ItemStack.EMPTY : ModularCuttingBoardItem.itemStack(block.defaultBlockState());
    }

    @Override
    public boolean canCraftInDimensions(int width,int height) {
        return this.shapedRecipe.canCraftInDimensions(width,height);
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ModularCuttingBoardItem.itemStack(Blocks.OAK_PLANKS.defaultBlockState());
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.MODULAR_CUTTING_BOARD.get();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.shapedRecipe.getIngredients();
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(CraftingInput craftingInput) {
        return this.shapedRecipe.getRemainingItems(craftingInput);
    }

    @Override
    public String getGroup() {
        return this.shapedRecipe.getGroup();
    }

    @Override
    public CraftingBookCategory category() {
        return this.shapedRecipe.category();
    }

    @Override
    public boolean showNotification() {
        return this.shapedRecipe.showNotification();
    }

    @Override
    public boolean isSpecial() {
        return false;
    }

    @Nullable
    private Block findMaterialBlock(CraftingInput craftingInput) {
        Block block=null;
        int materialCount=0;
        for(int index=0;index < craftingInput.size();index++) {
            ItemStack itemStack=craftingInput.getItem(index);
            if(itemStack.isEmpty()) continue;
            if(!itemStack.is(ModTags.Items.MODULAR_CUTTING_BOARD_MATERIALS)) continue;
            if(itemStack.is(ModTags.Items.EXCLUDE_MODULAR_CUTTING_BOARD_MATERIALS)) return null;
            if(!(itemStack.getItem() instanceof BlockItem blockItem)) return null;
            Block blocks=blockItem.getBlock();
            if(block == null) {
                block=blocks;
            } else if (block != blocks) {
                return null;
            }
            materialCount++;
        }
        if(materialCount != MATERIAL_COUNT) return null;
        return block;
    }

    public static class Serializer implements RecipeSerializer<ModularCuttingBoardRecipe> {
        private static final MapCodec<ModularCuttingBoardRecipe> CODEC=ShapedRecipe.Serializer.CODEC.xmap(ModularCuttingBoardRecipe::new,modularCuttingBoardRecipe -> modularCuttingBoardRecipe.shapedRecipe);
        private static final StreamCodec<RegistryFriendlyByteBuf,ModularCuttingBoardRecipe> STREAM_CODEC=ShapedRecipe.Serializer.STREAM_CODEC.map(ModularCuttingBoardRecipe::new,modularCuttingBoardRecipe ->  modularCuttingBoardRecipe.shapedRecipe);

        @Override
        public MapCodec<ModularCuttingBoardRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf,ModularCuttingBoardRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
