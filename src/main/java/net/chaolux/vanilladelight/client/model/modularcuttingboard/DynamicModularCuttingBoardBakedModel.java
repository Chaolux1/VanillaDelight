package net.chaolux.vanilladelight.client.model.modularcuttingboard;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.client.model.furniture.*;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DynamicModularCuttingBoardBakedModel implements IDynamicBakedModel {
    private static final float BOTTOM=0.0f;
    private static final float TOP=1.0f;
    private static final float MATERIAL_LAYER=FurnitureRenderLayers.LAYER_1;
    private static final float EPSILON=0.0001f;
    private final FurnitureModelCache CACHE=new FurnitureModelCache();
    private final ResourceLocation resourceLocation;
    private final TextureAtlasSprite textureAtlasSprite;
    private final Map<String,ModularCuttingBoardPatternSet> stringModularCuttingBoardPatternSetMap;
    private final FurnitureRenderData furnitureRenderData;
    private final ItemTransforms itemTransforms;
    private final @Nullable FurnitureRenderData renderData;
    private final ItemOverrides itemOverrides;
    private final TextureAtlasSprite surfacePixel;
    private final ModularCuttingBoardSurfaceBaker surfaceBaker;

    public DynamicModularCuttingBoardBakedModel(ResourceLocation resourceLocation,TextureAtlasSprite textureAtlasSprite,TextureAtlasSprite surfaceTemplate,TextureAtlasSprite surfacePixel,Map<String,ModularCuttingBoardPatternSet> stringModularCuttingBoardPatternSetMap,ItemTransforms itemTransforms) {
        this(resourceLocation,textureAtlasSprite,surfaceTemplate,surfacePixel,stringModularCuttingBoardPatternSetMap,itemTransforms,null,null);
    }

    private DynamicModularCuttingBoardBakedModel(ResourceLocation resourceLocation,TextureAtlasSprite textureAtlasSprite,TextureAtlasSprite surfaceTemplate,TextureAtlasSprite surfacePixel,Map<String,ModularCuttingBoardPatternSet> stringModularCuttingBoardPatternSetMap,ItemTransforms itemTransforms,@Nullable FurnitureRenderData furnitureRenderData,@Nullable ItemOverrides itemOverrides) {
        this.resourceLocation=resourceLocation;
        this.textureAtlasSprite=textureAtlasSprite;
        this.surfacePixel=surfacePixel;
        this.surfaceBaker=new ModularCuttingBoardSurfaceBaker(surfaceTemplate);
        this.stringModularCuttingBoardPatternSetMap=Map.copyOf(stringModularCuttingBoardPatternSetMap);
        this.itemTransforms=itemTransforms;
        this.renderData=furnitureRenderData;
        FurnitureDefintion furnitureDefintion= FurnitureRegistry.get(resourceLocation);
        this.furnitureRenderData=new FurnitureRenderData(resourceLocation,furnitureDefintion.defaultStyle(),furnitureDefintion.defaultMaterialState(),false);
        this.itemOverrides=itemOverrides != null ? itemOverrides : new ModularCuttingBoardItemOverrides(this);
    }

    BakedModel createItemModel(FurnitureRenderData furnitureRenderData) {
        return new DynamicModularCuttingBoardBakedModel(this.resourceLocation,this.textureAtlasSprite,this.surfaceBaker.surfaceTemplate(),this.surfacePixel,this.stringModularCuttingBoardPatternSetMap,this.itemTransforms,furnitureRenderData,ItemOverrides.EMPTY);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        if(direction != null) return List.of();
        FurnitureRenderData data=this.resolveRenderData(modelData);
        Direction facing=blockState != null && blockState.hasProperty(CuttingBoardBlock.FACING) ? blockState.getValue(CuttingBoardBlock.FACING) : Direction.NORTH;
        return this.CACHE.bakedQuadList(data,facing,null,renderType,() -> this.buildQuads(data,facing,renderType));
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState blockState, @NotNull RandomSource randomSource, @NotNull ModelData modelData) {
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
        FurnitureRenderData data=this.resolveRenderData(modelData);
        return this.resolveBodyMaterial(data,Direction.UP).atlasSprite();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return this.itemOverrides;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemTransforms getTransforms() {
        return this.itemTransforms;
    }

    private FurnitureRenderData resolveRenderData(ModelData modelData) {
        if(this.renderData != null) return this.renderData;
        FurnitureRenderData data=modelData.get(FurnitureBlockEntity.RENDER_DATA);
        if(data == null || !data.id().equals(this.resourceLocation)) return this.furnitureRenderData;
        return data;
    }

    private List<BakedQuad> buildQuads(FurnitureRenderData furnitureRenderData,Direction direction,@Nullable RenderType renderType) {
        List<BakedQuad> bakedQuadList=new ArrayList<>();
        this.addTopSurface(bakedQuadList,renderType,furnitureRenderData);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.DOWN,1.0f,1.0f,12.0f,15.0f,BOTTOM,1.0f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.DOWN,12.0f,1.0f,13.0f,6.0f,BOTTOM,1.0f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.DOWN,13.0f,1.0f,15.0f,15.0f,BOTTOM,1.0f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.DOWN,12.0f,10.0f,13.0f,15.0f,BOTTOM,1.0f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.NORTH,1.0f,BOTTOM,15.0f,TOP,1.0f,0.82f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.SOUTH,1.0f,BOTTOM,15.0f,TOP,15.0f,0.82f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.WEST,1.0f,BOTTOM,15.0f,TOP,1.0f,0.76f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.EAST,1.0f,BOTTOM,15.0f,TOP,15.0f,0.76f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.EAST,6.0f,BOTTOM,10.0f,TOP,12.0f,0.68f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.WEST,6.0f,BOTTOM,10.0f,TOP,13.0f,0.68f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.SOUTH,12.0f,BOTTOM,13.0f,TOP,6.0f,0.72f);
        this.addFace(bakedQuadList,renderType,furnitureRenderData,Direction.NORTH,12.0f,BOTTOM,13.0f,TOP,10.0f,0.72f);
        List<BakedQuad> rotate=new ArrayList<>(bakedQuadList.size());
        for (BakedQuad bakedQuad : bakedQuadList) {
            rotate.add(FurnitureQuadBuilder.rotate(bakedQuad,direction));
        }
        return List.copyOf(rotate);
    }

    private void addTopSurface(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,FurnitureRenderData furnitureRenderData) {
        ResolvedFurnitureMaterial resolvedFurnitureMaterial=this.resolveBodyMaterial(furnitureRenderData,Direction.UP);
        RenderType type=resolvedFurnitureMaterial.renderType();
        if(!this.matches(renderType,type)) return;
        ModularCuttingBoardPatternSet modularCuttingBoardPatternSet=this.stringModularCuttingBoardPatternSetMap.get(furnitureRenderData.style());
        ModularCuttingBoardSurfaceBaker.Surface surface=this.surfaceBaker.bake(resolvedFurnitureMaterial.atlasSprite(),resolvedFurnitureMaterial.tint(),modularCuttingBoardPatternSet == null ? null : modularCuttingBoardPatternSet.modularCuttingBoardControlMap());
        for (ModularCuttingBoardSurfaceBaker.Region region : surface.regions()) {
            this.addSurfaceRegion(bakedQuadList,region,1.0f,1.0f,12.0f,15.0f);
            this.addSurfaceRegion(bakedQuadList,region,13.0f,1.0f,15.0f,15.0f);
            this.addSurfaceRegion(bakedQuadList,region,12.0f,1.0f,13.0f,6.0f);
            this.addSurfaceRegion(bakedQuadList,region,12.0f,10.0f,13.0f,15.0f);
        }
    }

    private void addSurfaceRegion(List<BakedQuad> bakedQuadList,ModularCuttingBoardSurfaceBaker.Region region,float minX,float minZ,float maxX,float maxZ) {
        float sourceMinX=Math.max(region.minX(),minX);
        float sourceMinZ=Math.max(region.minZ(),minZ);
        float sourceMaxX=Math.min(region.maxX(),maxX);
        float sourceMaxZ=Math.min(region.maxZ(),maxZ);
        if(sourceMaxX <= sourceMinX || sourceMaxZ <= sourceMinZ) return;
        bakedQuadList.add(CuttingBoardQuadBuilder.create(Direction.UP,sourceMinX,sourceMinZ,sourceMaxX,sourceMaxZ,TOP,0.0f,this.surfacePixel,region.color(),1.0f,true));
    }

    private void addFace(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,FurnitureRenderData renderData,Direction direction,float minA,float minB,float maxA,float maxB,float plane,float shade) {
        ResolvedFurnitureMaterial resolvedFurnitureMaterial=this.resolveBodyMaterial(renderData,direction);
        this.addMaterialFace(bakedQuadList,renderType,resolvedFurnitureMaterial,direction,minA,minB,maxA,maxB,plane,0.0f,shade);
    }

    private ResolvedFurnitureMaterial resolveBodyMaterial(FurnitureRenderData furnitureRenderData,Direction direction) {
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(this.resourceLocation);
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get("body");
        MaterialState state=furnitureRenderData.materialState("body",materialState);
        return FurnitureMaterialResolverRegistry.furnitureMaterial(state,direction);
    }

    private void addMaterialFace(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,Direction direction,float minA,float minB,float maxA,float maxB,float plane,float offset,float shade) {
        if(resolvedFurnitureMaterial.fillPattern() != null && this.matches(renderType,RenderType.solid())) this.addTileFill(bakedQuadList,resolvedFurnitureMaterial.fillPattern(),direction,minA,minB,maxA,maxB,plane,offset,resolvedFurnitureMaterial.tint(),shade);
        RenderType type=resolvedFurnitureMaterial.fillPattern() != null ? RenderType.cutout() : resolvedFurnitureMaterial.renderType();
        if(!this.matches(renderType,type)) return;
        bakedQuadList.add(CuttingBoardQuadBuilder.create(direction,minA,minB,maxA,maxB,plane,offset + (resolvedFurnitureMaterial.fillPattern() != null ? MATERIAL_LAYER : 0.0f),resolvedFurnitureMaterial.atlasSprite(),resolvedFurnitureMaterial.tint(),shade,false));
    }

    private void addTileFill(List<BakedQuad> bakedQuadList, TextureFillPattern textureFillPattern,Direction direction,float minA,float minB,float maxA,float maxB,float plane,float offset,int tint,float shade) {
        float width=16.0f * textureFillPattern.sourceWidth() / textureFillPattern.spriteWidth();
        float height=16.0f * textureFillPattern.sourceHeight() / textureFillPattern.spriteHeight();
        float tileWidth=Math.max(1.0f,width);
        float tileHeight=Math.max(1.0f,height);
        for (float tileMinA=minA;tileMinA < maxA - EPSILON;tileMinA += tileWidth) {
            float tileMaxA=Math.min(tileMinA + tileWidth,maxA);
            float destinationWidth=tileMaxA - tileMinA;
            float sourceWidth=textureFillPattern.sourceWidth() * destinationWidth / tileWidth;
            for (float tileMinB=minB;tileMinB < maxB - EPSILON;tileMinB += tileHeight) {
                float tileMaxB=Math.min(tileMinB + tileHeight,maxB);
                float destinationHeight=tileMaxB - tileMinB;
                float sourceHeight=textureFillPattern.sourceHeight() * destinationHeight / tileHeight;
                bakedQuadList.add(CuttingBoardQuadBuilder.createRegion(direction,tileMinA,tileMinB,tileMaxA,tileMaxB,plane,offset,textureFillPattern,0.0f,0.0f,sourceWidth,sourceHeight,tint,shade));
            }
        }
    }

    private boolean matches(@Nullable RenderType renderType,RenderType type) {
        return renderType == null || renderType == type;
    }
}
