package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import net.chaolux.vanilladelight.client.model.patternedcabinet.PatternedCabinetControlMap;
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

public class UnbakedModularCuttingBoardModel implements IUnbakedGeometry<UnbakedModularCuttingBoardModel> {
    private final ResourceLocation resourceLocation;
    private final Map<String,String> stringMap;

    public UnbakedModularCuttingBoardModel(ResourceLocation resourceLocation,Map<String,String> stringMap) {
        this.resourceLocation=resourceLocation;
        this.stringMap=Map.copyOf(stringMap);
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> resourceLocationUnbakedModelFunction, IGeometryBakingContext iGeometryBakingContext) {

    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> materialTextureAtlasSpriteFunction, ModelState modelState, ItemOverrides itemOverrides,ResourceLocation resourceLocation) {
        TextureAtlasSprite textureAtlasSprite=materialTextureAtlasSpriteFunction.apply(iGeometryBakingContext.getMaterial("particle"));
        TextureAtlasSprite atlasSprite=materialTextureAtlasSpriteFunction.apply(iGeometryBakingContext.getMaterial("surface_template"));
        TextureAtlasSprite sprite=materialTextureAtlasSpriteFunction.apply(iGeometryBakingContext.getMaterial("surface_pixel"));
        Map<String,ModularCuttingBoardPatternSet> stringModularCuttingBoardPatternSetMap=new LinkedHashMap<>();
        this.stringMap.forEach((style,key) -> {
            TextureAtlasSprite pattern=materialTextureAtlasSpriteFunction.apply(iGeometryBakingContext.getMaterial(key));
            stringModularCuttingBoardPatternSetMap.put(style,new ModularCuttingBoardPatternSet(PatternedCabinetControlMap.from(pattern)));
        });
        return new DynamicModularCuttingBoardBakedModel(this.resourceLocation,textureAtlasSprite,atlasSprite,sprite,stringModularCuttingBoardPatternSetMap,iGeometryBakingContext.getTransforms());
    }
}
