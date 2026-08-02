package net.chaolux.vanilladelight.integration.jei.category;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.chaolux.vanilladelight.common.crafting.ModularCuttingBoardRecipe;
import net.chaolux.vanilladelight.common.item.ModularCuttingBoardItem;
import net.chaolux.vanilladelight.common.tag.ModTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ModularCuttingBoardRecipeCategory implements ICraftingCategoryExtension {
//    private static final String PRIMARY_MATERIAL_SLOT="vanilladelight_primary_material";
//    private static final String MATERIAL_SLOT_2="vanilladelight_material_2";
//    private static final String MATERIAL_SLOT_3="vanilladelight_material_3";
//    private static final String MATERIAL_SLOT_4="vanilladelight_material_4";
//    private static final String OUTPUT_SLOT="vanilladelight_modular_cutting_board_output";
    private final ModularCuttingBoardRecipe modularCuttingBoardRecipe;
    private final List<ItemStack> itemStackList;
    private final List<ItemStack> itemStacks;

    public ModularCuttingBoardRecipeCategory(ModularCuttingBoardRecipe modularCuttingBoardRecipe) {
        this.modularCuttingBoardRecipe=modularCuttingBoardRecipe;
        this.itemStackList=createMaterial();
        this.itemStacks=createOutput(this.itemStackList);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, ICraftingGridHelper iCraftingGridHelper, IFocusGroup iFocusGroup) {
        List<List<ItemStack>> list=new ArrayList<>();
        list.add(List.of(new ItemStack(Items.STICK)));
        list.add(this.itemStackList);
        list.add(this.itemStackList);
        list.add(List.of(new ItemStack(Items.STICK)));
        list.add(this.itemStackList);
        list.add(this.itemStackList);
        List<IRecipeSlotBuilder> recipeSlotBuilderList=iCraftingGridHelper.createAndSetInputs(iRecipeLayoutBuilder,list,3,2);
        IRecipeSlotBuilder iRecipeSlotBuilder=iCraftingGridHelper.createAndSetOutputs(iRecipeLayoutBuilder,this.itemStacks);
        if(recipeSlotBuilderList.size() >= 9) {
            iRecipeLayoutBuilder.createFocusLink(recipeSlotBuilderList.get(4),recipeSlotBuilderList.get(5),recipeSlotBuilderList.get(7),recipeSlotBuilderList.get(8),iRecipeSlotBuilder);
        }
    }

//    @Override
//    public void onDisplayedIngredientsUpdate(List<IRecipeSlotDrawable> iRecipeSlotDrawableList,IFocusGroup iFocusGroup) {
//        IRecipeSlotDrawable iRecipeSlotDrawable=findSlot(iRecipeSlotDrawableList,PRIMARY_MATERIAL_SLOT);
//        if(iRecipeSlotDrawable == null) return;
//        Optional<ItemStack> optionalItemStack=iRecipeSlotDrawable.getDisplayedItemStack();
//        if(optionalItemStack.isEmpty()) return;
//        ItemStack itemStack=optionalItemStack.get();
//        if(!(itemStack.getItem() instanceof BlockItem blockItem)) return;
//        overrideSlot(iRecipeSlotDrawableList,MATERIAL_SLOT_2,itemStack);
//        overrideSlot(iRecipeSlotDrawableList,MATERIAL_SLOT_3,itemStack);
//        overrideSlot(iRecipeSlotDrawableList,MATERIAL_SLOT_4,itemStack);
//        ItemStack result= ModularCuttingBoardItem.itemStack(blockItem.getBlock().defaultBlockState());
//        overrideSlot(iRecipeSlotDrawableList,OUTPUT_SLOT,result);
//    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.modularCuttingBoardRecipe.getId();
    }

    private static List<ItemStack> createMaterial() {
        List<ItemStack> stackList=new ArrayList<>();
        Optional<HolderSet.Named<Item>> optionalHolders= BuiltInRegistries.ITEM.getTag(ModTags.Items.MODULAR_CUTTING_BOARD_MATERIALS);
        optionalHolders.ifPresent(holders -> {
            for (Holder<Item> holder : holders) {
                Item item=holder.value();
                ItemStack stack=new ItemStack(item);
                if(stack.is(ModTags.Items.EXCLUDE_MODULAR_CUTTING_BOARD_MATERIALS)) continue;
                if(!(item instanceof BlockItem)) continue;
                stackList.add(stack);
            }
        });
        stackList.sort(Comparator.comparing(itemStack -> BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString()));
        if(stackList.isEmpty()) stackList.add(new ItemStack(Blocks.OAK_PLANKS));
        return List.copyOf(stackList);
    }

    private static List<ItemStack> createOutput(List<ItemStack> stackList) {
        List<ItemStack> list=new ArrayList<>();
        for(ItemStack itemStack : stackList) {
            if(itemStack.getItem() instanceof BlockItem blockItem) list.add(ModularCuttingBoardItem.itemStack(blockItem.getBlock().defaultBlockState()));
        }
        if(list.isEmpty()) list.add(ModularCuttingBoardItem.itemStack(Blocks.OAK_PLANKS.defaultBlockState()));
        return List.copyOf(list);
    }

//    private static void overrideSlot(List<IRecipeSlotDrawable> iRecipeSlotDrawableList,String string,ItemStack itemStack) {
//        IRecipeSlotDrawable iRecipeSlotDrawable=findSlot(iRecipeSlotDrawableList,string);
//        if(iRecipeSlotDrawable == null) return;
//        iRecipeSlotDrawable.clearDisplayOverrides();
//        iRecipeSlotDrawable.createDisplayOverrides().addItemStack(itemStack.copy());
//    }
//
//    @Nullable
//    private static IRecipeSlotDrawable findSlot(List<IRecipeSlotDrawable> iRecipeSlotDrawableList,String string) {
//        for (IRecipeSlotDrawable iRecipeSlotDrawable : iRecipeSlotDrawableList) {
//            if(iRecipeSlotDrawable.getSlotName().filter(string::equals).isPresent()) return iRecipeSlotDrawable;
//        }
//        return null;
//    }
}
