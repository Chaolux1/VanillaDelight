package net.chaolux.vanilladelight.client.model.modularstove;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.client.model.cabinet.CabinetQuadBuilder;
import net.chaolux.vanilladelight.client.model.furniture.*;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.chaolux.vanilladelight.common.furniture.ModularStoveFireStyle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
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
import vectorwing.farmersdelight.common.block.AbstractStoveBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DynamicModularStoveBakedModel implements IDynamicBakedModel {
    private static final float FIRE_LAYER= FurnitureRenderLayers.STEP * 7.0f;
    private static final float ARC_LAYER =FurnitureRenderLayers.LAYER_2;
    private static final float GLOW_LAYER=FurnitureRenderLayers.STEP * 6.0f;
    private static final float ARC_LAYER_FRAME=FurnitureRenderLayers.LAYER_4;
    private static final float OFFSET=FurnitureRenderLayers.STEP;
    private final ResourceLocation resourceLocation;
    private final TextureAtlasSprite textureAtlasSprite;
    private final TextureAtlasSprite fixedSideSprite;
    private final TextureAtlasSprite topOffSprite;
    private final TextureAtlasSprite arcSprite;
    private final Map<String,StyleSprite> stringStyleSpriteMap;
    private final FurnitureRenderData furnitureRenderData;
    private final ItemTransforms itemTransforms;
    private final Map<CacheKey, List<BakedQuad>> CACHE=new ConcurrentHashMap<>();

    public DynamicModularStoveBakedModel(ResourceLocation resourceLocation, TextureAtlasSprite textureAtlasSprite, TextureAtlasSprite fixedSideSprite,TextureAtlasSprite topOffSprite,TextureAtlasSprite arcSprite,Map<String,StyleSprite> stringStyleSpriteMap, ItemTransforms itemTransforms) {
        this.resourceLocation=resourceLocation;
        this.textureAtlasSprite=textureAtlasSprite;
        this.fixedSideSprite=fixedSideSprite;
        this.topOffSprite=topOffSprite;
        this.arcSprite=arcSprite;
        this.stringStyleSpriteMap=Map.copyOf(stringStyleSpriteMap);
        this.itemTransforms=itemTransforms;
        FurnitureDefintion furnitureDefintion= FurnitureRegistry.get(resourceLocation);
        this.furnitureRenderData=new FurnitureRenderData(resourceLocation,furnitureDefintion.defaultStyle(),furnitureDefintion.defaultMaterialState(),false);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        FurnitureRenderData renderData=modelData.get(FurnitureBlockEntity.RENDER_DATA);
        FurnitureRenderData data=renderData == null || !renderData.id().equals(this.resourceLocation) ? this.furnitureRenderData : renderData;
        Direction facing=blockState != null && blockState.hasProperty(AbstractStoveBlock.FACING) ? blockState.getValue(AbstractStoveBlock.FACING) : Direction.NORTH;
        boolean lit=blockState == null || !blockState.hasProperty(AbstractStoveBlock.LIT) || blockState.getValue(AbstractStoveBlock.LIT);
        if(blockState == null && direction == null) {
            List<BakedQuad> bakedQuadList=new ArrayList<>();
            for(Direction side : Direction.values()) {
                bakedQuadList.addAll(this.buildQuads(side,renderType,data,Direction.NORTH,true));
            }
            return bakedQuadList;
        }
        if(direction == null) return List.of();
        CacheKey cacheKey=new CacheKey(data,facing,direction,renderType,lit);
        return this.CACHE.computeIfAbsent(cacheKey,unused -> List.copyOf(this.buildQuads(direction,renderType,data,facing,lit)));
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
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get("body");
        MaterialState state=renderData.materialState("body",materialState);
        return FurnitureMaterialResolverRegistry.furnitureMaterial(state,Direction.NORTH).atlasSprite();
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

    private List<BakedQuad> buildQuads(Direction direction,@Nullable RenderType renderType,FurnitureRenderData furnitureRenderData,Direction facing,boolean lit) {
        Direction side= FurnitureQuadBuilder.direction(direction,facing);
        ResolvedFurnitureMaterial resolvedFurnitureMaterial=this.resolve(furnitureRenderData,side);
        List<BakedQuad> bakedQuadList=new ArrayList<>();
        switch (side) {
            case NORTH -> this.buildFront(bakedQuadList,renderType,resolvedFurnitureMaterial,furnitureRenderData.style(),lit);
            case SOUTH,EAST,WEST -> this.buildSide(bakedQuadList,renderType,resolvedFurnitureMaterial,side);
            case UP -> this.buildTop(bakedQuadList,renderType,furnitureRenderData.style(),lit);
            case DOWN -> this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.DOWN,0.0f,0.0f,16.0f,16.0f,0.0f,1.0f,false);
        }
        List<BakedQuad> rotate=new ArrayList<>(bakedQuadList.size());
        for (BakedQuad bakedQuad : bakedQuadList) {
            rotate.add(FurnitureQuadBuilder.rotate(bakedQuad,facing));
        }
        return rotate;
    }

    private void buildFront(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,String string,boolean lit) {
        this.buildSide(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH);
        this.addArc(bakedQuadList,renderType,resolvedFurnitureMaterial,lit);
        if(!lit) return;
        this.addFireArc(bakedQuadList,renderType,string);
        this.addFixedRect(bakedQuadList,renderType,Direction.NORTH,0.0f,0.0f,16.0f,16.0f,FIRE_LAYER,this.styleSprite(string).textureAtlasSprite(),true,false);
    }

    private void addArc(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,boolean lit) {
        float lowShade=lit ? 0.48f : 0.36f;
        float middleShade=lit ? 0.41f : 0.31f;
        float shade=lit ? 0.35f : 0.27f;
        float frameShade=lit ? 0.16f : 0.12f;
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,4.0f,0.0f,12.0f,6.0f, ARC_LAYER,lowShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,5.0f,6.0f,11.0f,8.0f, ARC_LAYER,middleShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,6.0f,8.0f,10.0f,9.0f, ARC_LAYER,shade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,4.0f,0.0f,5.0f,6.0f,ARC_LAYER_FRAME,frameShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,11.0f,0.0f,12.0f,6.0f,ARC_LAYER_FRAME,frameShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,5.0f,6.0f,6.0f,8.0f,ARC_LAYER_FRAME,frameShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,10.0f,6.0f,11.0f,8.0f,ARC_LAYER_FRAME,frameShade,false);
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,Direction.NORTH,6.0f,8.0f,10.0f,9.0f,ARC_LAYER_FRAME,frameShade,false);
    }

    private void addFireArc(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,String string) {
        if(!this.matches(renderType,RenderType.translucent())) return;
        ModularStoveFireStyle modularStoveFireStyle=ModularStoveFireStyle.fromId(string);
        int fireTint;
        if(modularStoveFireStyle == ModularStoveFireStyle.RAINBOW) {
            fireTint=0xFFFFFF;
        } else {
            fireTint=modularStoveFireStyle.firstColor();
        }
        bakedQuadList.add(CabinetQuadBuilder.create(Direction.NORTH,0.0f,0.0f,16.0f,16.0f,GLOW_LAYER,this.arcSprite,fireTint,0.72f,true,true));
    }

    private void buildSide(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,Direction direction) {
        this.addMaterialRect(bakedQuadList,renderType,resolvedFurnitureMaterial,direction,0.0f,0.0f,16.0f,12.0f,0.0f,1.0f,false);
        this.addFixedRect(bakedQuadList,renderType,direction,0.0f,12.0f,16.0f,16.0f,0.0f,this.fixedSideSprite,false,false);
    }

    private void buildTop(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,String string,boolean lit) {
        TextureAtlasSprite topLit = lit ? this.styleSprite(string).atlasSprite : this.topOffSprite;
        this.addFixedRect(bakedQuadList, renderType, Direction.UP, 0.0f, 0.0f, 16.0f, 16.0f, 0.0f, topLit, true,false);
    }

    private StyleSprite styleSprite(String string) {
        StyleSprite sprite=this.stringStyleSpriteMap.get(this.furnitureRenderData.style());
        if(sprite == null) throw new IllegalStateException("Modular stove has no default fire style" + this.furnitureRenderData.style());
        return this.stringStyleSpriteMap.getOrDefault(string,sprite);
    }

    private ResolvedFurnitureMaterial resolve(FurnitureRenderData furnitureRenderData,Direction direction) {
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(this.resourceLocation);
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get("body");
        MaterialState state=furnitureRenderData.materialState("body",materialState);
        return FurnitureMaterialResolverRegistry.furnitureMaterial(state,direction);
    }

    private void addFixedRect(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,Direction direction,float minA,float minB,float maxA,float maxB,float offset,TextureAtlasSprite sprite,boolean stretch,boolean emissive) {
        if(!this.matches(renderType,RenderType.cutout())) return;
        bakedQuadList.add(CabinetQuadBuilder.create(direction,minA,minB,maxA,maxB,offset,sprite,-1,1.0f,stretch,emissive));
    }

    private void addMaterialRect(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,Direction direction,float minA,float minB,float maxA,float maxB,float offset,float shade,boolean stretch) {
        TextureFillPattern textureFillPattern=resolvedFurnitureMaterial.fillPattern();
        boolean isFillPattern=textureFillPattern != null;
        if(isFillPattern && this.matches(renderType,RenderType.cutout())) this.addTileFill(bakedQuadList,resolvedFurnitureMaterial.fillPattern(),direction,minA,minB,maxA,maxB,offset,resolvedFurnitureMaterial.tint(),shade);
        RenderType type=resolvedFurnitureMaterial.renderType();
        RenderType defaultRenderType;
        if(!isFillPattern) {
            defaultRenderType=type;
        } else if (type == RenderType.cutoutMipped()) {
            defaultRenderType=RenderType.cutoutMipped();
        } else {
            defaultRenderType=RenderType.cutout();
        }
        if(!this.matches(renderType,defaultRenderType)) return;
        float offsetFillPattern=isFillPattern ? offset + OFFSET : offset;
        bakedQuadList.add(CabinetQuadBuilder.create(direction,minA,minB,maxA,maxB,offsetFillPattern,resolvedFurnitureMaterial.atlasSprite(),resolvedFurnitureMaterial.tint(),shade,stretch));
    }

    private void addTileFill(List<BakedQuad> bakedQuadList, TextureFillPattern textureFillPattern,Direction direction,float minA,float minB,float maxA,float maxB,float offset,int tint,float shade) {
        float width=Math.max(1.0f,16.0f * textureFillPattern.sourceWidth() / textureFillPattern.spriteWidth());
        float height=Math.max(1.0f,16.0f * textureFillPattern.sourceHeight() / textureFillPattern.spriteHeight());
        for (float tileMinA=minA;tileMinA < maxA - 0.0001f;tileMinA += width) {
            float tileMaxA=Math.min(tileMinA + width,maxA);
            float destinationWidth=tileMaxA - tileMinA;
            float sourceWidth=textureFillPattern.sourceWidth() * destinationWidth / width;
            for (float tileMinB=minB;tileMinB < maxB - 0.0001f;tileMinB += height) {
                float tileMaxB=Math.min(tileMinB + height,maxB);
                float destinationHeight=tileMaxB - tileMinB;
                float sourceHeight=textureFillPattern.sourceHeight() * destinationHeight / height;
                bakedQuadList.add(CabinetQuadBuilder.createRegion(direction,tileMinA,tileMinB,tileMaxA,tileMaxB,offset,textureFillPattern,0.0f,0.0f,sourceWidth,sourceHeight,tint,shade));
            }
        }
    }

    private boolean matches(@Nullable RenderType renderType,RenderType type) {
        return renderType == null || renderType == type;
    }

    private record CacheKey(FurnitureRenderData furnitureRenderData,Direction direction,Direction facing,@Nullable RenderType renderType,boolean lit) {

    }

    public record StyleSprite(TextureAtlasSprite textureAtlasSprite,TextureAtlasSprite atlasSprite) {

    }
}
