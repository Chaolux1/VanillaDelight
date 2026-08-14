package net.chaolux.vanilladelight.client.model.cabinet;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.client.model.furniture.*;
import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
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

import java.util.ArrayList;
import java.util.List;

public class DynamicModularCabinetBakedModel implements IDynamicBakedModel {
    private static final float LAYER=FurnitureRenderLayers.LAYER_1;
    private static final float DETAIL_LAYER=FurnitureRenderLayers.LAYER_2;
    private final ResourceLocation resourceLocation;
    private final TextureAtlasSprite textureAtlasSprite;
    private final FurnitureRenderData furnitureRenderData;
    private final FurnitureModelCache CACHE=new FurnitureModelCache();
    private final ItemTransforms itemTransforms;

    public DynamicModularCabinetBakedModel(ResourceLocation resourceLocation, TextureAtlasSprite textureAtlasSprite, ItemTransforms itemTransforms) {
        this.resourceLocation=resourceLocation;
        this.textureAtlasSprite=textureAtlasSprite;
        this.itemTransforms=itemTransforms;
        FurnitureDefintion furnitureDefintion= FurnitureRegistry.get(resourceLocation);
        this.furnitureRenderData=new FurnitureRenderData(resourceLocation,furnitureDefintion.defaultStyle(),furnitureDefintion.defaultMaterialState(),false);
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

    private List<BakedQuad> buildQuads(@Nullable Direction direction,@Nullable RenderType renderType,FurnitureRenderData furnitureRenderData,Direction facing) {
        if(direction == null) return List.of();
        Direction side= FurnitureQuadBuilder.direction(direction,facing);
        FurnitureDefintion furnitureDefintion=FurnitureRegistry.get(this.resourceLocation);
        ResolvedFurnitureMaterial body=this.resolve(furnitureRenderData,furnitureDefintion,"body",side);
        ResolvedFurnitureMaterial top=this.resolve(furnitureRenderData,furnitureDefintion,"top",side);
        ResolvedFurnitureMaterial accent=this.resolve(furnitureRenderData,furnitureDefintion,"accent",side);
        List<BakedQuad> bakedQuadList=new ArrayList<>();
        switch (side) {
            case UP -> this.buildTop(bakedQuadList,renderType,top);
            case DOWN -> this.addMaterialRect(bakedQuadList,renderType,body,Direction.DOWN,0.0f,0.0f,16.0f,16.0f,0.0f,1.0f,false);
            case NORTH -> this.buildFront(bakedQuadList,renderType,body,top,accent,furnitureRenderData.style(),furnitureRenderData.open());
            case SOUTH,EAST,WEST -> this.buildPlainVerticalSide(bakedQuadList,renderType,body,top,side);
        }

        List<BakedQuad> result=new ArrayList<>(bakedQuadList.size());
        for (BakedQuad bakedQuad : bakedQuadList) {
            result.add(FurnitureQuadBuilder.rotate(bakedQuad,facing));
        }
        return result;
    }

    private ResolvedFurnitureMaterial resolve(FurnitureRenderData furnitureRenderData,FurnitureDefintion furnitureDefintion,String string,Direction direction) {
        MaterialState materialState=furnitureDefintion.defaultMaterialState().get(string);
        MaterialState state=furnitureRenderData.materialState(string,materialState);
        return FurnitureMaterialResolverRegistry.furnitureMaterial(state,direction);
    }

    private void buildTop(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial furnitureMaterial) {
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,0.0f,0.0f,16.0f,16.0f,0.0f,1.0f,false);
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,1.0f,1.0f,15.0f,15.0f,LAYER * 2.0f,1.03f,false);
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,0.0f,0.0f,16.0f,1.0f,DETAIL_LAYER * 2.0f,0.55f,false);
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,0.0f,15.0f,16.0f,16.0f,DETAIL_LAYER * 2.0f,0.55f,false);
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,0.0f,1.0f,1.0f,15.0f,DETAIL_LAYER * 2.0f,0.55f,false);
        this.addMaterialRect(bakedQuadList,renderType,furnitureMaterial,Direction.UP,15.0f,1.0f,16.0f,15.0f,DETAIL_LAYER * 2.0f,0.55f,false);
    }

    private void buildPlainVerticalSide(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial top,Direction direction) {
        this.addMaterialRect(bakedQuadList,renderType,body,direction,0.0f,0.0f,16.0f,12.0f,0.0f,1.0f,false);
        this.addTopBand(bakedQuadList,renderType,top,direction);
    }

    private void buildFront(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial top,ResolvedFurnitureMaterial accent,String string,boolean open) {
        this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,0.0f,0.0f,16.0f,12.0f,0.0f,1.0f,false);
        this.addTopBand(bakedQuadList,renderType,top,Direction.NORTH);
        switch (string) {
            case "double_door" -> this.buildDoubleDoor(bakedQuadList,renderType,body,accent,open);
            case "three_drawers" -> this.buildThreeDrawers(bakedQuadList,renderType,body,accent,open);
            default -> this.buildSingleDoor(bakedQuadList,renderType,body,accent,open);
        }
    }

    private void addTopBand(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial top,Direction direction) {
        this.addMaterialRect(bakedQuadList,renderType,top,direction,0.0f,12.0f,16.0f,16.0f,0.0f,1.0f,false);
        this.addMaterialRect(bakedQuadList,renderType,top,direction,0.0f,12.0f,16.0f,13.0f,DETAIL_LAYER,0.5f,false);
        this.addMaterialRect(bakedQuadList,renderType,top,direction,0.0f,15.0f,16.0f,16.0f,DETAIL_LAYER,0.5f,false);
        this.addMaterialRect(bakedQuadList,renderType,top,direction,0.0f,13.0f,1.0f,15.0f,DETAIL_LAYER,0.5f,false);
        this.addMaterialRect(bakedQuadList,renderType,top,direction,15.0f,13.0f,16.0f,15.0f,DETAIL_LAYER,0.5f,false);
    }

    private void buildSingleDoor(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial accent,boolean open) {
        if(open) {
            this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,2.0f,2.0f,14.0f,11.0f,LAYER,0.40f,false);
            this.drawPanel(bakedQuadList,renderType,body,1.0f,1.0f,6.0f,11.0f,0.75f,DETAIL_LAYER);
            this.drawHandle(bakedQuadList,renderType,accent,3.75f,5.25f,4.75f,7.75f,DETAIL_LAYER * 2.0f);
            return;
        }
        this.drawPanel(bakedQuadList,renderType,body,1.0f,1.0f,15.0f,11.0f,0.75f,DETAIL_LAYER);
        this.drawHandle(bakedQuadList,renderType,accent,11.75f,5.25f,12.75f,7.75f,DETAIL_LAYER * 2.0f);
    }

    private void buildDoubleDoor(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial accent,boolean open) {
        if(open) {
            this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,3.0f,2.0f,13.0f,11.0f,LAYER,0.4f,false);
            this.drawPanel(bakedQuadList,renderType,body,1.0f,1.0f,4.0f,11.0f,0.75f,DETAIL_LAYER);
            this.drawPanel(bakedQuadList,renderType,body,12.0f,1.0f,15.0f,11.0f,0.75f,DETAIL_LAYER);
            this.drawHandle(bakedQuadList,renderType,accent,3.0f,5.25f,4.0f,7.75f,DETAIL_LAYER * 3.0f);
            this.drawHandle(bakedQuadList,renderType,accent,12.0f,5.25f,13.0f,7.75f,DETAIL_LAYER * 3.0f);
            return;
        }
        this.drawPanel(bakedQuadList,renderType,body,1.0f,1.0f,15.0f,11.0f,0.75f,DETAIL_LAYER);
        this.addMaterialRect(bakedQuadList,renderType,body,Direction.NORTH,7.75f,1.0f,8.5f,11.0f,DETAIL_LAYER * 2.0f,0.62f,false);
        this.drawHandle(bakedQuadList,renderType,accent,6.25f,5.25f,7.25f,7.75f,DETAIL_LAYER * 3.0f);
        this.drawHandle(bakedQuadList,renderType,accent,8.75f,5.25f,9.75f,7.75f,DETAIL_LAYER * 3.0f);
    }

    private void buildThreeDrawers(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,ResolvedFurnitureMaterial accent,boolean open) {
        for(int index=0;index < 3;index++) {
            float minY = index * 4.0f;
            float maxY = minY + 4.0f;
            float panelMinY = minY + 0.2f;
            float panelMaxY = maxY - 0.2f;
            if (open) {
                this.drawPanel(bakedQuadList, renderType, body, 1.0f, panelMinY, 15.0f, panelMaxY, 0.35f, DETAIL_LAYER);
                continue;
            }
            this.drawPanel(bakedQuadList, renderType, body, 1.0f, panelMinY, 15.0f, panelMaxY, 0.75f, DETAIL_LAYER);
            this.drawHandle(bakedQuadList, renderType, accent, 6.5f, minY + 1.65f, 9.5f, minY + 2.35f, DETAIL_LAYER * 3.0f);
        }
    }

    private void drawPanel(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial body,float minX,float minY,float maxX,float maxY,float shade,float offset) {
        this.addMaterialRect(bakedQuadList, renderType, body, Direction.NORTH, minX, minY, maxX, maxY, offset, shade, false);
    }

    private void drawHandle(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial accent,float minX,float minY,float maxX,float maxY,float offset) {
        this.addMaterialRect(bakedQuadList,renderType,accent,Direction.NORTH,minX + 0.25f,minY - 0.2f,maxX + 0.25f,maxY - 0.2f,offset,0.58f,false);
        this.addMaterialRect(bakedQuadList,renderType,accent,Direction.NORTH,minX,minY,maxX,maxY,offset + LAYER,1.0f,false);
    }

    private void addMaterialRect(List<BakedQuad> bakedQuadList,@Nullable RenderType renderType,ResolvedFurnitureMaterial resolvedFurnitureMaterial,Direction direction,float minA,float minB,float maxA,float maxB,float offset,float shade,boolean stretch) {
        if(resolvedFurnitureMaterial.fillPattern() != null && this.matches(renderType,RenderType.solid())) this.addTiledFill(bakedQuadList,resolvedFurnitureMaterial.fillPattern(),direction,minA,minB,maxA,maxB,offset,resolvedFurnitureMaterial.tint(),shade);
        RenderType type=resolvedFurnitureMaterial.fillPattern() != null ? RenderType.cutout() : resolvedFurnitureMaterial.renderType();
        if(!this.matches(renderType,type)) return;
        bakedQuadList.add(CabinetQuadBuilder.create(direction,minA,minB,maxA,maxB,offset + (resolvedFurnitureMaterial.gap() ? LAYER : 0.0f),resolvedFurnitureMaterial.atlasSprite(),resolvedFurnitureMaterial.tint(),shade,stretch));
    }

    private boolean matches(@Nullable RenderType renderType,RenderType type) {
        return renderType == null || renderType == type;
    }

    private void addTiledFill(List<BakedQuad> bakedQuadList, TextureFillPattern textureFillPattern,Direction direction,float minA,float minB,float maxA,float maxB,float offset,int tint,float shade) {
        float width=16.0f * textureFillPattern.sourceWidth() / textureFillPattern.spriteWidth();
        float height=16.0f * textureFillPattern.sourceHeight() / textureFillPattern.spriteHeight();
        float tileWidth=Math.max(1.0f,width);
        float tileHeight=Math.max(1.0f,height);
        for(float tileMinA=minA;tileMinA < maxA - 0.0001f;tileMinA += tileWidth) {
            float tileMaxA=Math.min(tileMinA + tileWidth,maxA);
            float destinationWidth=tileMaxA - tileMinA;
            float sourceWidth=textureFillPattern.sourceWidth() * destinationWidth / tileWidth;
            for(float tileMinB=minB;tileMinB < maxB - 0.0001f;tileMinB += tileHeight) {
                float tileMaxB=Math.min(tileMinB + tileHeight,maxB);
                float destinationHeight=tileMaxB - tileMinB;
                float sourceHeight=textureFillPattern.sourceHeight() * destinationHeight / tileHeight;
                bakedQuadList.add(CabinetQuadBuilder.createRegion(direction,tileMinA,tileMinB,tileMaxA,tileMaxB,offset,textureFillPattern,0.0f,0.0f,sourceWidth,sourceHeight,tint,shade));
            }
        }
    }
}
