package net.chaolux.vanilladelight.client.model.patternedcabinet;

import net.chaolux.vanilladelight.client.model.furniture.DynamicFurnitureBakedModel;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class UnbakedPatternedCabinetModel implements IUnbakedGeometry<UnbakedPatternedCabinetModel> {
    private final ResourceLocation resourceLocation;
    private final Map<String,TextureKey> textureKeysMap;

    public UnbakedPatternedCabinetModel(ResourceLocation resourceLocation,Map<String,TextureKey> textureKeysMap) {
        this.resourceLocation=resourceLocation;
        this.textureKeysMap=Map.copyOf(textureKeysMap);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelFunction, IGeometryBakingContext iGeometryBakingContext) {

    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> atlasSpriteFunction, ModelState modelState, ItemOverrides itemOverrides, ResourceLocation resourceLocation) {
        TextureAtlasSprite atlasSprite=atlasSpriteFunction.apply(iGeometryBakingContext.getMaterial("particle"));
        Map<String,PatternedCabinetPatternSet> patternedCabinetPatternSetMap=new LinkedHashMap<>();
        this.textureKeysMap.forEach((style,key) -> {
            TextureAtlasSprite closeSprite=atlasSpriteFunction.apply(iGeometryBakingContext.getMaterial(key.close()));
            TextureAtlasSprite openSprite=atlasSpriteFunction.apply(iGeometryBakingContext.getMaterial(key.open()));
            patternedCabinetPatternSetMap.put(style,new PatternedCabinetPatternSet(PatternedCabinetControlMap.from(closeSprite),PatternedCabinetControlMap.from(openSprite)));
        });
        return new DynamicPatternedCabinetBakedModel(this.resourceLocation,atlasSprite,patternedCabinetPatternSetMap,iGeometryBakingContext.getTransforms());
    }

    public record TextureKey(String close,String open) {

    }
}
