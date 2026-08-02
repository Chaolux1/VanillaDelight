package net.chaolux.vanilladelight.common.item;

import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.CuttingBoardPattern;
import net.chaolux.vanilladelight.common.furniture.FurnitureAppearance;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefinitions;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;

import java.util.List;

public class ModularCuttingBoardItem extends FuelBlockItem {
    public static final String BLOCK_ENTITY_TAG="BlockEntityTag";
    public static final String FURNITURE_TAG="Furniture";
    public static final String BODY_MATERIAL="body";

    public ModularCuttingBoardItem(Block block,Properties properties,int burnTime) {
        super(block,properties,burnTime);
    }

    public static ItemStack itemStack(BlockState blockState) {
        ItemStack itemStack=new ItemStack(ModItems.MODULAR_CUTTING_BOARD.get());
        setAppearance(itemStack,blockState, CuttingBoardPattern.PLAIN);
        return itemStack;
    }

    public static void setAppearance(ItemStack itemStack,BlockState blockState,CuttingBoardPattern cuttingBoardPattern) {
        CompoundTag compoundTag=createFurnitureTag(blockState,cuttingBoardPattern);
        CompoundTag tag=itemStack.getOrCreateTagElement(BLOCK_ENTITY_TAG);
        tag.put(FURNITURE_TAG,compoundTag);
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
        CompoundTag compoundTag=itemStack.getTagElement(BLOCK_ENTITY_TAG);
        if(compoundTag == null || !compoundTag.contains(FURNITURE_TAG, Tag.TAG_COMPOUND)) return null;
        return compoundTag.getCompound(FURNITURE_TAG);
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

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> componentList, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack,level,componentList,tooltipFlag);
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(FurnitureDefinitions.MODULAR_CUTTING_BOARD);
        FurnitureRenderData furnitureRenderData=readRenderData(itemStack);
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get(BODY_MATERIAL);
        MaterialState state=furnitureRenderData.materialState(BODY_MATERIAL,materialState);
        CuttingBoardPattern cuttingBoardPattern=CuttingBoardPattern.boardPattern(furnitureRenderData.style());
        componentList.add(Component.translatable("tooltip.vanilladelight.modular_cutting_board.material",state.state().getBlock().getName()).withStyle(ChatFormatting.GRAY));
        componentList.add(Component.translatable("tooltip.vanilladelight.modular_cutting_board.pattern",Component.translatable(cuttingBoardPattern.translationKey())).withStyle(ChatFormatting.GRAY));
    }
}
