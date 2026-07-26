package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.Config;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.AbstractBannerBlock;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultMaterialResolver implements MaterialResolver {
    private final Map<Key,ResolvedFurnitureMaterial> CACHE=new ConcurrentHashMap<>();

    @Override
    public boolean supports(MaterialState materialState) {
        return true;
    }

    @Override
    public ResolvedFurnitureMaterial resolve(MaterialState materialState, Direction direction) {
        boolean fillTransparentGaps=Config.FILL_TRANSPARENT_MATERIAL_GAPS.get();
        return this.CACHE.computeIfAbsent(new Key(materialState,direction,fillTransparentGaps),this::resolveUncahed);
    }

    @Override
    public void clear() {
        this.CACHE.clear();
    }

    private ResolvedFurnitureMaterial resolveUncahed(Key key) {
        BlockRenderDispatcher renderDispatcher=Minecraft.getInstance().getBlockRenderer();
        BlockColors blockColors=Minecraft.getInstance().getBlockColors();
        BlockState blockState=key.materialState.state();
        BakedModel bakedModel=renderDispatcher.getBlockModel(blockState);
        BakedQuad bakedQuad=blockState.getRenderShape() == RenderShape.MODEL ? dominant(bakedModel,blockState,key.direction) : null;
        TextureAtlasSprite atlasSprite=bakedQuad == null ? bakedModel.getParticleIcon() : bakedQuad.getSprite();
        int tint= -1;
        if(bakedQuad != null && bakedQuad.isTinted()) {
            try {
                tint=blockColors.getColor(blockState,null,null,bakedQuad.getTintIndex());
            } catch (RuntimeException exception) {
                tint= -1;
            }
        }
        RenderType renderType= ItemBlockRenderTypes.getChunkRenderType(blockState);
        boolean fullBlock= Block.isShapeFullBlock(blockState.getCollisionShape(EmptyBlockGetter.INSTANCE, BlockPos.ZERO));
        boolean preserveTransparency=blockState.getBlock() instanceof AbstractGlassBlock || renderType == RenderType.translucent();
        TextureFillPattern textureFillPattern=null;
        if(key.fillTransparentGaps() && !fullBlock && !preserveTransparency) textureFillPattern=TextureGapFiller.textureFillPattern(atlasSprite);
        return new ResolvedFurnitureMaterial(atlasSprite,textureFillPattern,tint,renderType);
    }

    private static BakedQuad dominant(BakedModel bakedModel,BlockState blockState,Direction direction) {
        long seed=31L + direction.ordinal();
        List<BakedQuad> bakedQuadList=new ArrayList<>(bakedModel.getQuads(blockState,direction, RandomSource.create(seed), ModelData.EMPTY,null));
        if(bakedQuadList.isEmpty()) bakedQuadList.addAll(bakedModel.getQuads(blockState,null,RandomSource.create(seed),ModelData.EMPTY,null));
        return bakedQuadList.stream().filter(bakedQuad -> bakedQuad.getDirection() == direction || bakedQuadList.size() == 1).max(Comparator.comparingDouble(DefaultMaterialResolver::area)).orElse(bakedQuadList.isEmpty() ? null:bakedQuadList.get(0));
    }

    private static double area(BakedQuad bakedQuad) {
        int[] vertices=bakedQuad.getVertices();
        int stride=vertices.length / 4;
        double[] a=position(vertices,stride,0);
        double[] b=position(vertices,stride,1);
        double[] c=position(vertices,stride,2);
        double abX=b[0] - a[0];
        double abY=b[1] - a[1];
        double abZ=b[2] - a[2];
        double acX=c[0] - a[0];
        double acY=c[1] - a[1];
        double acZ=c[2] - a[2];
        double crossX=abY * acZ - abZ * acY;
        double crossY=abZ * acX - abX * acZ;
        double crossZ=abX * acY - abY * acX;
        return Math.sqrt(crossX * crossX + crossY * crossY + crossZ * crossZ);
    }

    private static double[] position(int [] vertices,int stride,int vertex) {
        int offset=vertex * stride;
        return new double[]{
                Float.intBitsToFloat(vertices[offset]),Float.intBitsToFloat(vertices[offset + 1]),Float.intBitsToFloat(vertices[offset + 2])
        };
    }

    private record Key(MaterialState materialState,Direction direction,boolean fillTransparentGaps) {
        
    }
}
