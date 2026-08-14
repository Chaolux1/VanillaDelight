package net.chaolux.vanilladelight.client.model.patternedcabinet;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.client.model.cabinet.CabinetQuadBuilder;
import net.chaolux.vanilladelight.client.model.furniture.*;
import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicPatternedCabinetBakedModel implements IDynamicBakedModel {
    private static final float MATERIAL_LAYER=FurnitureRenderLayers.LAYER_1;
    private static final float BODY=FurnitureRenderLayers.LAYER_3;
    private static final float ACCENT=FurnitureRenderLayers.LAYER_4;
    private static final float CAVITY=FurnitureRenderLayers.LAYER_2;
    private final FurnitureModelCache CACHE=new FurnitureModelCache();
    private final ResourceLocation resourceLocation;
    private final TextureAtlasSprite textureAtlasSprite;
    private final FurnitureRenderData furnitureRenderData;
    private final Map<String,PatternedCabinetPatternSet> patternedCabinetPatternSetMap;
    private final PatternedCabinetPatternSet patternedCabinetPatternSet;
    private final ItemTransforms itemTransforms;

    public DynamicPatternedCabinetBakedModel(ResourceLocation resourceLocation,TextureAtlasSprite textureAtlasSprite,Map<String,PatternedCabinetPatternSet> patternedCabinetPatternSetMap,ItemTransforms itemTransforms) {
        this.resourceLocation=resourceLocation;
        this.textureAtlasSprite=textureAtlasSprite;
        this.patternedCabinetPatternSetMap=patternedCabinetPatternSetMap;
        this.itemTransforms=itemTransforms;
        FurnitureDefintion furnitureDefintion= FurnitureRegistry.get(resourceLocation);
        this.furnitureRenderData=new FurnitureRenderData(resourceLocation,furnitureDefintion.defaultStyle(),furnitureDefintion.defaultMaterialState(),false);
        PatternedCabinetPatternSet patternSet=this.patternedCabinetPatternSetMap.get(furnitureDefintion.defaultStyle());
        if(patternSet != null) {
            this.patternedCabinetPatternSet=patternSet;
        } else {
            this.patternedCabinetPatternSet=this.patternedCabinetPatternSetMap.values().stream().findFirst().orElse(null);
        }
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        FurnitureRenderData renderData=modelData.get(FurnitureBlockEntity.RENDER_DATA);
        FurnitureRenderData data=renderData == null || !renderData.id().equals(this.resourceLocation) ? this.furnitureRenderData : renderData;
        Direction facing=blockState != null && blockState.hasProperty(ModularFurnitureBlock.FACING) ? blockState.getValue(ModularFurnitureBlock.FACING) : Direction.NORTH;
        if(blockState == null && direction == null) {
            List<BakedQuad> bakedQuadList=new ArrayList<>();
            for(Direction side : Direction.values()) {
                bakedQuadList.addAll(this.buildQuads(side,renderType,data,Direction.NORTH));
            }
            return bakedQuadList;
        }
        return this.CACHE.bakedQuadList(data,facing,direction,renderType,() -> this.buildQuads(direction,renderType,data,facing));
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState blockState,@NotNull RandomSource randomSource,@NotNull ModelData modelData) {
        return ChunkRenderTypeSet.of(RenderType.solid(),RenderType.cutout(),RenderType.cutoutMipped(),RenderType.translucent());
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.textureAtlasSprite;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData modelData) {
        FurnitureRenderData renderData=modelData.get(FurnitureBlockEntity.RENDER_DATA);
        if(renderData == null || !renderData.id().equals(this.resourceLocation)) return this.textureAtlasSprite;
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(this.resourceLocation);
        MaterialState materialState=renderData.materialState("body",furnitureDefintion.defaultMaterialState().get("body"));
        return FurnitureMaterialResolverRegistry.furnitureMaterial(materialState,Direction.NORTH).atlasSprite();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemTransforms getTransforms() {
        return this.itemTransforms;
    }

    private List<BakedQuad> buildQuads(@Nullable Direction direction,@Nullable RenderType renderType,FurnitureRenderData renderData,Direction facing) {
        if(direction == null) return List.of();
        Direction side= FurnitureQuadBuilder.direction(direction,facing);
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(this.resourceLocation);
        ResolvedFurnitureMaterial body=this.resolveMaterial(renderData,furnitureDefintion,"body",side);
        ResolvedFurnitureMaterial accent=this.resolveMaterial(renderData,furnitureDefintion,"accent",side);
        float shade=side == Direction.NORTH ? PatternedCabinetControlMap.FRONT_SHADE : 1.0f;
        List<BakedQuad> bakedQuadList=new ArrayList<>();
        this.addMaterialRect(bakedQuadList,renderType,body,side,0.0f,0.0f,16.0f,16.0f,0.0f,shade,false);
        if(side == Direction.NORTH) this.addFrontPattern(bakedQuadList,renderType,body,accent,renderData.style(),renderData.open());
        List<BakedQuad> bakedQuads=new ArrayList<>(bakedQuadList.size());
        for(BakedQuad bakedQuad : bakedQuadList) {
            bakedQuads.add(FurnitureQuadBuilder.rotate(bakedQuad,facing));
        }
        return bakedQuads;
    }

    public ResolvedFurnitureMaterial resolveMaterial(FurnitureRenderData furnitureRenderData,FurnitureDefintion furnitureDefintion,String string,Direction direction) {
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get(string);
        MaterialState state=furnitureRenderData.materialState(string,materialState);
        return FurnitureMaterialResolverRegistry.furnitureMaterial(state,direction);
    }

    private void addFrontPattern(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial accent,String string,boolean open) {
        PatternedCabinetPatternSet patternSet=this.patternedCabinetPatternSetMap.get(string);
        if(patternSet == null) patternSet=this.patternedCabinetPatternSet;
        if(patternSet == null) return;
        PatternedCabinetControlMap cabinetControlMap=patternSet.cabinetControlMap(open);
        for(PatternedCabinetControlMap.Region region : cabinetControlMap.regions()) {
            if(region.layer() != PatternedCabinetControlMap.Layer.CAVITY) continue;
            this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,region.minX(),region.minY(),region.maxX(),region.maxY(),CAVITY,region.shade(),false);
        }
        for (PatternedCabinetControlMap.Region region : cabinetControlMap.regions()) {
            if(region.layer() != PatternedCabinetControlMap.Layer.BODY_SHADE) continue;
            this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,region.minX(),region.minY(),region.maxX(),region.maxY(),BODY,region.shade(),false);
        }
        for (PatternedCabinetControlMap.Region region : cabinetControlMap.regions()) {
            if(region.layer() != PatternedCabinetControlMap.Layer.ACCENT) continue;
            this.addMaterialRect(bakedQuadList,renderType,accent,Direction.NORTH,region.minX(),region.minY(),region.maxX(),region.maxY(),ACCENT,region.shade(),false);
        }
    }

    private void addMaterialRect(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,Direction direction,float minA,float minB,float maxA,float maxB,float offset,float shade,boolean stretch) {
        if(resolvedFurnitureMaterial.fillPattern() != null && this.matches(renderType,RenderType.solid())) this.addTileFill(bakedQuadList,resolvedFurnitureMaterial.fillPattern(),direction,minA,minB,maxA,maxB,offset,resolvedFurnitureMaterial.tint(),shade);
        RenderType type=resolvedFurnitureMaterial.fillPattern() != null ? RenderType.cutout() : resolvedFurnitureMaterial.renderType();
        if(!this.matches(renderType,type)) return;
        bakedQuadList.add(CabinetQuadBuilder.create(direction,minA,minB,maxA,maxB,offset + (resolvedFurnitureMaterial.fillPattern() != null ? MATERIAL_LAYER * 0.25f : 0.0f),resolvedFurnitureMaterial.atlasSprite(),resolvedFurnitureMaterial.tint(),shade,stretch));
    }

    private void addTileFill(List<BakedQuad> bakedQuadList, TextureFillPattern textureFillPattern,Direction direction,float minA,float minB,float maxA,float maxB,float offset,int tint,float shade) {
        float width=16.0f * textureFillPattern.sourceWidth() / textureFillPattern.spriteWidth();
        float height=16.0f * textureFillPattern.sourceHeight() / textureFillPattern.spriteHeight();
        float tileWidth=Math.max(1.0f,width);
        float tileHeight=Math.max(1.0f,height);
        for (float tileMinA=minA;tileMinA < maxA - 0.0001f;tileMinA += tileWidth) {
            float tileMaxA=Math.min(tileMinA + tileWidth,maxA);
            float destinationWidth=tileMaxA - tileMinA;
            float sourceWidth=textureFillPattern.sourceWidth() * destinationWidth / tileWidth;
            for (float tileMinB=minB;tileMinB < maxB - 0.0001f;tileMinB += tileHeight) {
                float tileMaxB=Math.min(tileMinB + tileHeight,maxB);
                float destinationHeight=tileMaxB - tileMinB;
                float sourceHeight=textureFillPattern.sourceHeight() * destinationHeight / tileHeight;
                bakedQuadList.add(CabinetQuadBuilder.createRegion(direction,tileMinA,tileMinB,tileMaxA,tileMaxB,offset,textureFillPattern,0.0f,0.0f,sourceWidth,sourceHeight,tint,shade));
            }
        }
    }

    private boolean matches(@Nullable RenderType renderType,RenderType type) {
        return renderType == null || renderType == type;
    }
}
