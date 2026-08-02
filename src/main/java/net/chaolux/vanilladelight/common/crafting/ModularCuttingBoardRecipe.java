package net.chaolux.vanilladelight.common.crafting;

import com.google.gson.JsonObject;
import net.chaolux.vanilladelight.common.item.ModularCuttingBoardItem;
import net.chaolux.vanilladelight.common.tag.ModTags;
import net.chaolux.vanilladelight.registry.crafting.ModRecipeSerializers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.IShapedRecipe;
import org.jetbrains.annotations.Nullable;


public class ModularCuttingBoardRecipe implements CraftingRecipe, IShapedRecipe<CraftingContainer> {
    private static final int MATERIAL_COUNT=4;
    private final ShapedRecipe shapedRecipe;

    public ModularCuttingBoardRecipe(ShapedRecipe shapedRecipe) {
        this.shapedRecipe=shapedRecipe;
    }

    @Override
    public boolean matches(CraftingContainer craftingContainer, Level level) {
        if(!this.shapedRecipe.matches(craftingContainer,level)) return false;
        return this.findMaterialBlock(craftingContainer) != null;
    }

    @Override
    public ItemStack assemble(CraftingContainer craftingContainer, RegistryAccess registryAccess) {
        Block block=this.findMaterialBlock(craftingContainer);
        if(block == null) return ItemStack.EMPTY;
        return ModularCuttingBoardItem.itemStack(block.defaultBlockState());
    }

    @Override
    public boolean canCraftInDimensions(int width,int height) {
        return this.shapedRecipe.canCraftInDimensions(width,height);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ModularCuttingBoardItem.itemStack(Blocks.OAK_PLANKS.defaultBlockState());
    }

    @Override
    public ResourceLocation getId() {
        return this.shapedRecipe.getId();
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
    public NonNullList<ItemStack> getRemainingItems(CraftingContainer craftingContainer) {
        return this.shapedRecipe.getRemainingItems(craftingContainer);
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

    @Override
    public int getRecipeWidth() {
        return this.shapedRecipe.getWidth();
    }

    @Override
    public int getRecipeHeight() {
        return this.shapedRecipe.getHeight();
    }

    @Nullable
    private Block findMaterialBlock(CraftingContainer craftingContainer) {
        Block block=null;
        int materialCount=0;
        for(int index=0;index < craftingContainer.getContainerSize();index++) {
            ItemStack itemStack=craftingContainer.getItem(index);
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
        @Override
        public ModularCuttingBoardRecipe fromJson(ResourceLocation resourceLocation, JsonObject jsonObject) {
            ShapedRecipe recipe=RecipeSerializer.SHAPED_RECIPE.fromJson(resourceLocation,jsonObject);
            return new ModularCuttingBoardRecipe(recipe);
        }

        @Override
        @Nullable
        public ModularCuttingBoardRecipe fromNetwork(ResourceLocation resourceLocation, FriendlyByteBuf friendlyByteBuf) {
            ShapedRecipe recipe=RecipeSerializer.SHAPED_RECIPE.fromNetwork(resourceLocation,friendlyByteBuf);
            if(recipe == null) return null;
            return new ModularCuttingBoardRecipe(recipe);
        }

        @Override
        public void toNetwork(FriendlyByteBuf friendlyByteBuf,ModularCuttingBoardRecipe modularCuttingBoardRecipe) {
            RecipeSerializer.SHAPED_RECIPE.toNetwork(friendlyByteBuf,modularCuttingBoardRecipe.shapedRecipe);
        }
    }

}
