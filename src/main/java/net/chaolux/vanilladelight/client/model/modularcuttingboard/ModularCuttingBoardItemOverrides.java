package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.item.ModularCuttingBoardItem;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModularCuttingBoardItemOverrides extends ItemOverrides {
    private final DynamicModularCuttingBoardBakedModel dynamicModularCuttingBoardBakedModel;
    private final Map<FurnitureRenderData, BakedModel> furnitureRenderDataBakedModelMap=new ConcurrentHashMap<>();

    public ModularCuttingBoardItemOverrides(DynamicModularCuttingBoardBakedModel dynamicModularCuttingBoardBakedModel) {
        this.dynamicModularCuttingBoardBakedModel=dynamicModularCuttingBoardBakedModel;
    }

    @Override
    public BakedModel resolve(BakedModel bakedModel, ItemStack itemStack, @Nullable ClientLevel clientLevel, @Nullable LivingEntity livingEntity,int seed) {
        if(!(itemStack.getItem() instanceof ModularCuttingBoardItem)) return bakedModel;
        FurnitureRenderData furnitureRenderData=ModularCuttingBoardItem.readRenderData(itemStack);
        return this.furnitureRenderDataBakedModelMap.computeIfAbsent(furnitureRenderData,this.dynamicModularCuttingBoardBakedModel::createItemModel);
    }
}
