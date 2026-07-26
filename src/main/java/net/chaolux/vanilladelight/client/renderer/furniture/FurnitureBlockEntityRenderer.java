package net.chaolux.vanilladelight.client.renderer.furniture;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.chaolux.vanilladelight.client.model.furniture.DynamicFurnitureBakedModel;
import net.chaolux.vanilladelight.client.model.furniture.FurnitureBakedModelRegistry;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;

public class FurnitureBlockEntityRenderer implements BlockEntityRenderer<FurnitureBlockEntity> {
    public FurnitureBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(FurnitureBlockEntity furnitureBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource,int packLight,int packOverlay) {
        Level level=furnitureBlockEntity.getLevel();
        if(level == null) return;
        FurnitureRenderData renderData=furnitureBlockEntity.getModelData().get(FurnitureBlockEntity.RENDER_DATA);
        if(renderData == null) return;
        DynamicFurnitureBakedModel furnitureBakedModel= FurnitureBakedModelRegistry.dynamicFurnitureBakedModel(renderData.id());
        if(furnitureBakedModel == null) return;
        for(DynamicFurnitureBakedModel.AnimatedQuad aniamtedQuad : furnitureBakedModel.getAnimatedQuads(furnitureBlockEntity.getBlockState(),renderData,level.getGameTime(),partialTick)) {
            VertexConsumer vertexConsumer=bufferSource.getBuffer(aniamtedQuad.renderType());
            vertexConsumer.putBulkData(poseStack.last(),aniamtedQuad.bakedQuad(),1.0f,1.0f,1.0f,1.0f,packLight,packOverlay,true);
        }
    }
}
