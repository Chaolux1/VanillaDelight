package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class UnbakedFurnitureModel implements IUnbakedGeometry<UnbakedFurnitureModel> {
    private final FurnitureModelDefinition definition;

    public UnbakedFurnitureModel(FurnitureModelDefinition definition) {
        this.definition=definition;
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelFunction, IGeometryBakingContext iGeometryBakingContext) {
        for(FurnitureStyleModels styleModels : this.definition.styleModelsMap().values()) {
            modelFunction.apply(styleModels.close()).resolveParents(modelFunction);
            if(styleModels.open() != null) modelFunction.apply(styleModels.open()).resolveParents(modelFunction);
        }
    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> atlasHolderFunction, ModelState modelState, ItemOverrides itemOverrides, ResourceLocation resourceLocation) {
        Map<String,BakedFurnitureStyle> styleMap=new LinkedHashMap<>();
        this.definition.styleModelsMap().forEach((id,styleModels) -> {
            BakedModel close=modelBaker.bake(styleModels.close(),modelState);
            BakedModel open=styleModels.open() == null ? close : modelBaker.bake(styleModels.open(),modelState);
            styleMap.put(id,new BakedFurnitureStyle(close,open));
        });
        Map<ResourceLocation,TextureAtlasSprite> overlays=new LinkedHashMap<>();
        this.definition.furniturePartMap().values().forEach(furniturePart -> furniturePart.effectRules().forEach(effectRule -> {
            if(effectRule.overlay() != null) {
                String string=this.definition.locationStringMap().get(effectRule.overlay());
                Material material;
                if(string == null) {
                    material=new Material(InventoryMenu.BLOCK_ATLAS,effectRule.overlay());
                } else {
                    material=iGeometryBakingContext.getMaterial(string);
                }
                overlays.put(effectRule.overlay(),atlasHolderFunction.apply(material));
            }
        }));
        return new DynamicFurnitureBakedModel(this.definition,styleMap,overlays);
    }

    public record BakedFurnitureStyle(BakedModel close,BakedModel open) {
        public BakedModel bakedModel(boolean open) {
            if(open) return this.open;
            return this.close;
        }
    }
}
