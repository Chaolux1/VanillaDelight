package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.CuttingBoardPattern;
import net.chaolux.vanilladelight.common.furniture.FurnitureAppearance;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefinitions;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ModularCuttingBoardItem extends BlockItem {
    public static final String FURNITURE_TAG="Furniture";
    public static final String BODY_MATERIAL="body";

    public ModularCuttingBoardItem(Block block,Properties properties) {
        super(block,properties);
    }

    public static ItemStack itemStack(BlockState blockState) {
        ItemStack itemStack=new ItemStack(ModItems.MODULAR_CUTTING_BOARD.get());
        setAppearance(itemStack,blockState, CuttingBoardPattern.PLAIN);
        return itemStack;
    }

    public static void setAppearance(ItemStack itemStack,BlockState blockState,CuttingBoardPattern cuttingBoardPattern) {
        CompoundTag compoundTag=createFurnitureTag(blockState,cuttingBoardPattern);
        CustomData.update(DataComponents.CUSTOM_DATA,itemStack,tag -> tag.put(FURNITURE_TAG,compoundTag));
    }

    public static void setPattern(ItemStack itemStack,CuttingBoardPattern cuttingBoardPattern) {
        CompoundTag compoundTag=getFurnitureTag(itemStack);
        if (compoundTag == null) {
            FurnitureDefintion furnitureDefintion= FurnitureRegistry.get(FurnitureDefinitions.MODULAR_CUTTING_BOARD);
            MaterialState materialState=furnitureDefintion.defaultMaterialState().get(BODY_MATERIAL);
            setAppearance(itemStack,materialState.state(),cuttingBoardPattern);
            return;
        }
        compoundTag.putString("Style",cuttingBoardPattern.id());
        CustomData.update(DataComponents.CUSTOM_DATA,itemStack,tag -> tag.put(FURNITURE_TAG,compoundTag));
    }

    public static FurnitureRenderData readRenderData(ItemStack itemStack) {
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(FurnitureDefinitions.MODULAR_CUTTING_BOARD);
        FurnitureAppearance furnitureAppearance=new FurnitureAppearance(furnitureDefintion);
        CompoundTag compoundTag=getFurnitureTag(itemStack);
        if(compoundTag != null) furnitureAppearance.load(compoundTag,furnitureDefintion);
        return furnitureAppearance.renderData(false);
    }

    @Nullable
    public static CompoundTag getFurnitureTag(ItemStack itemStack) {
        return getFurnitureTag(itemStack.get(DataComponents.CUSTOM_DATA));
    }

    @Nullable
    private static CompoundTag getFurnitureTag(@Nullable CustomData customData) {
        if(customData == null || customData.isEmpty()) return null;
        CompoundTag compoundTag=customData.copyTag();
        if(!compoundTag.contains(FURNITURE_TAG,Tag.TAG_COMPOUND)) return null;
        return compoundTag.getCompound(FURNITURE_TAG).copy();
    }

    public static CompoundTag createFurnitureTag(BlockState blockState,CuttingBoardPattern cuttingBoardPattern) {
        CompoundTag compoundTag=new CompoundTag();
        compoundTag.putString("Definition",FurnitureDefinitions.MODULAR_CUTTING_BOARD.toString());
        compoundTag.putString("Style",cuttingBoardPattern.id());
        compoundTag.putBoolean("Locked",false);
        CompoundTag tag=new CompoundTag();
        tag.put(BODY_MATERIAL,MaterialState.from(blockState).save());
        compoundTag.put("Materials",tag);
        compoundTag.put("InstallMaterial",new CompoundTag());
        return compoundTag;
    }
}
