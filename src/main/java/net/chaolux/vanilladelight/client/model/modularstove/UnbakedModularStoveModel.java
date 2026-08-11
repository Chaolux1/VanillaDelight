package net.chaolux.vanilladelight.client.model.modularstove;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class UnbakedModularStoveModel implements IUnbakedGeometry<UnbakedModularStoveModel> {
    private final ResourceLocation resourceLocation;
    private final ResourceLocation location;
    private final ResourceLocation fixedSide;
    private final ResourceLocation topOff;
    private final ResourceLocation arc;
    private final Map<String,StyleTextures> stringStyleTexturesMap;

    public UnbakedModularStoveModel(ResourceLocation resourceLocation,ResourceLocation location,ResourceLocation fixedSide,ResourceLocation topOff,ResourceLocation arc,Map<String,StyleTextures> stringStyleTextureMap) {
        this.resourceLocation=resourceLocation;
        this.location=location;
        this.fixedSide=fixedSide;
        this.topOff=topOff;
        this.arc=arc;
        this.stringStyleTexturesMap=Map.copyOf(stringStyleTextureMap);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resourceLocationUnbakedModelFunction, IGeometryBakingContext iGeometryBakingContext) {

    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> materialTextureAtlasSpriteFunction, ModelState modelState, ItemOverrides itemOverrides) {
        TextureAtlasSprite textureAtlasSprite=sprite(materialTextureAtlasSpriteFunction,this.location);
        TextureAtlasSprite fixedSideSprite=sprite(materialTextureAtlasSpriteFunction,this.fixedSide);
        TextureAtlasSprite topOffSprite=sprite(materialTextureAtlasSpriteFunction,this.topOff);
        TextureAtlasSprite arc=sprite(materialTextureAtlasSpriteFunction,this.arc);
        Map<String,DynamicModularStoveBakedModel.StyleSprite> map=new LinkedHashMap<>();
        this.stringStyleTexturesMap.forEach((style,textures) -> map.put(style,new DynamicModularStoveBakedModel.StyleSprite(sprite(materialTextureAtlasSpriteFunction,textures.resourceLocation),sprite(materialTextureAtlasSpriteFunction,textures.location))));
        return new DynamicModularStoveBakedModel(this.resourceLocation,textureAtlasSprite,fixedSideSprite,topOffSprite,arc,map,iGeometryBakingContext.getTransforms());
    }

    private static TextureAtlasSprite sprite(Function<Material,TextureAtlasSprite> materialTextureAtlasSpriteFunction,ResourceLocation resourceLocation) {
        return materialTextureAtlasSpriteFunction.apply(new Material(InventoryMenu.BLOCK_ATLAS,resourceLocation));
    }

    public record StyleTextures(ResourceLocation resourceLocation,ResourceLocation location) {

    }
}
