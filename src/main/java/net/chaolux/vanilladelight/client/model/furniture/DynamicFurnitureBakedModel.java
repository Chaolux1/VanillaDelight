package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.api.furniture.EffectRule;
import net.chaolux.vanilladelight.api.furniture.FurniturePart;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class DynamicFurnitureBakedModel implements IDynamicBakedModel {
    private final FurnitureModelDefinition definition;
    private final Map<String, UnbakedFurnitureModel.BakedFurnitureStyle> styles;
    private final Map<ResourceLocation, TextureAtlasSprite> overlays;
    private final FurnitureModelCache CACHE = new FurnitureModelCache();
    private final FurnitureRenderData renderData;

    public DynamicFurnitureBakedModel(FurnitureModelDefinition definition, Map<String, UnbakedFurnitureModel.BakedFurnitureStyle> styles, Map<ResourceLocation, TextureAtlasSprite> overlays) {
        this.definition = definition;
        this.styles = Map.copyOf(styles);
        this.overlays = Map.copyOf(overlays);
        this.renderData = new FurnitureRenderData(definition.resourceLocation(), definition.string(), definition.materialStateMap(), false);
        FurnitureBakedModelRegistry.register(definition.resourceLocation(), this);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, @NotNull RandomSource randomSource, @NotNull ModelData modelData, @Nullable RenderType renderType) {
        FurnitureRenderData data = modelData.get(FurnitureBlockEntity.RENDER_DATA);
        FurnitureRenderData furnitureRenderData = data == null || !data.id().equals(this.definition.resourceLocation()) ? this.renderData : data;
        Direction facing = blockState != null && blockState.hasProperty(ModularFurnitureBlock.FACING) ? blockState.getValue(ModularFurnitureBlock.FACING) : Direction.NORTH;
        return this.CACHE.bakedQuadList(furnitureRenderData, facing, direction, renderType, () -> this.buildStatic(blockState, direction, randomSource, furnitureRenderData, renderType, facing));
    }

    public List<AnimatedQuad> getAnimatedQuads(@Nullable BlockState blockState, FurnitureRenderData furnitureRenderData, long tick, float partialTick) {
        FurnitureRenderData data = furnitureRenderData.id().equals(this.definition.resourceLocation()) ? furnitureRenderData : this.renderData;
        BakedModel bakedModel = this.bakedModel(data);
        Direction direction = blockState != null && blockState.hasProperty(ModularFurnitureBlock.FACING) ? blockState.getValue(ModularFurnitureBlock.FACING) : Direction.NORTH;
        Set<BakedQuad> bakedQuads = new LinkedHashSet<>(bakedModel.getQuads(blockState, null, RandomSource.create(42L)));
        for (Direction facing : Direction.values()) {
            bakedQuads.addAll(bakedModel.getQuads(blockState, facing, RandomSource.create(42L + facing.ordinal())));
        }
        List<AnimatedQuad> animatedQuads = new ArrayList<>();
        for (BakedQuad bakedQuad : bakedQuads) {
            FurniturePart furniturePart = this.definition.furniturePartMap().get(bakedQuad.getSprite().contents().name());
            if (furniturePart == null || furniturePart.effectRules().stream().noneMatch(EffectRule::animated)) continue;
            MaterialState materialState = this.definition.materialStateMap().get(furniturePart.material());
            MaterialState material = data.materialState(furniturePart.material(), materialState);
            ResolvedFurnitureMaterial resolvedFurnitureMaterial = FurnitureMaterialResolverRegistry.furnitureMaterial(material, bakedQuad.getDirection());
            RenderType renderType = animatedRenderType(furniturePart, resolvedFurnitureMaterial.renderType());
            for (BakedQuad quad : FurnitureQuadBuilder.buildAnimated(bakedQuad, resolvedFurnitureMaterial, furniturePart, this.overlays, tick, partialTick)) {
                animatedQuads.add(new AnimatedQuad(renderType, FurnitureQuadBuilder.rotate(quad, direction)));
            }
        }
        return animatedQuads;
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState blockState,@NotNull RandomSource randomSource,@NotNull ModelData modelData) {
        return ChunkRenderTypeSet.of(RenderType.solid(),RenderType.cutout(),RenderType.cutoutMipped(),RenderType.translucent());
    }

    @Override
    public boolean useAmbientOcclusion() {
        return this.defaultTemplate().useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return this.defaultTemplate().isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return this.defaultTemplate().usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return this.defaultTemplate().getParticleIcon();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return this.getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return this.defaultTemplate().getOverrides();
    }

    public void clearCache() {
        this.CACHE.clear();
    }

    private List<BakedQuad> buildStatic(@Nullable BlockState blockState,@Nullable Direction direction,RandomSource randomSource,FurnitureRenderData renderData,@Nullable RenderType renderType,Direction facing) {
        BakedModel bakedModel=this.bakedModel(renderData);
        Direction side=direction == null ? null : FurnitureQuadBuilder.direction(direction,facing);
        List<BakedQuad> bakedQuads=new ArrayList<>();
        for (BakedQuad quad : bakedModel.getQuads(blockState,side,randomSource)) {
            FurniturePart furniturePart=this.definition.furniturePartMap().get(quad.getSprite().contents().name());
            if(furniturePart == null) {
                if(renderType == null || renderType == RenderType.solid()) bakedQuads.add(FurnitureQuadBuilder.rotate(quad,facing));
                continue;
            }
            MaterialState materialState=this.definition.materialStateMap().get(furniturePart.material());
            MaterialState selectedMaterial=renderData.materialState(furniturePart.material(),materialState);
            ResolvedFurnitureMaterial furnitureMaterial=FurnitureMaterialResolverRegistry.furnitureMaterial(selectedMaterial,quad.getDirection());
            RenderType type=staticRenderType(furniturePart,furnitureMaterial);
            if(renderType != null && renderType != type) continue;
            for(BakedQuad bakedQuad : FurnitureQuadBuilder.buildStatic(quad,furnitureMaterial,furniturePart,this.overlays)) {
                bakedQuads.add(FurnitureQuadBuilder.rotate(bakedQuad,facing));
            }
        }
        return bakedQuads;
    }

    private BakedModel bakedModel(FurnitureRenderData renderData) {
        UnbakedFurnitureModel.BakedFurnitureStyle style=this.styles.getOrDefault(renderData.style(),this.styles.get(this.definition.string()));
        return style.bakedModel(renderData.open());
    }

    private BakedModel defaultTemplate() {
        return this.styles.get(this.definition.string()).close();
    }

    private static RenderType staticRenderType(FurniturePart furniturePart,ResolvedFurnitureMaterial furnitureMaterial) {
        if(furnitureMaterial.gap()) return RenderType.cutout();
        return furniturePart.effectRules().stream().anyMatch(effectRule -> effectRule.alpha() < 1.0f) ? RenderType.translucent() : furnitureMaterial.renderType();
    }

    private static RenderType animatedRenderType(FurniturePart furniturePart,RenderType renderType) {
        return furniturePart.effectRules().stream().filter(EffectRule::animated).anyMatch(effectRule -> effectRule.alpha() < 1.0f || effectRule.overlay() != null) ? RenderType.translucent() : renderType;
    }

    public record AnimatedQuad(RenderType renderType,BakedQuad bakedQuad) {
        
    }
}
