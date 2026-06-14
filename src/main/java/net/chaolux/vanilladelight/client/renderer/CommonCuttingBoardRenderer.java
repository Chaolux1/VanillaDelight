package net.chaolux.vanilladelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.chaolux.vanilladelight.common.block.entity.CommonCuttingBoardBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import vectorwing.farmersdelight.common.block.CuttingBoardBlock;
import vectorwing.farmersdelight.common.block.entity.CuttingBoardBlockEntity;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Random;

public class CommonCuttingBoardRenderer implements BlockEntityRenderer<CommonCuttingBoardBlockEntity> {
    private final Random random = new Random();

    public CommonCuttingBoardRenderer(BlockEntityRendererProvider.Context pContext) {
    }

    @Override
    public void render(CommonCuttingBoardBlockEntity cuttingBoard, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ItemStack itemStack = cuttingBoard.getStoredItem();
        if (!itemStack.isEmpty()) {
            Direction direction = ((Direction)cuttingBoard.getBlockState().getValue(CuttingBoardBlock.FACING)).getOpposite();
            int posLong = (int)cuttingBoard.getBlockPos().asLong();
            int seed = itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) + itemStack.getDamageValue();
            this.random.setSeed((long)seed);
            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
            int itemRenderCount = this.getModelCount(itemStack);

            for(int i = 0; i < itemRenderCount; ++i) {
                poseStack.pushPose();
                poseStack.pushPose();
                boolean isBlockItem = itemRenderer.getModel(itemStack, cuttingBoard.getLevel(), (LivingEntity)null, 0).applyTransform(ItemDisplayContext.FIXED, poseStack, false).isGui3d();
                poseStack.popPose();
                float xOffset = itemRenderCount == 1 ? 0.0F : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                float zOffset = itemRenderCount == 1 ? 0.0F : (this.random.nextFloat() * 2.0F - 1.0F) * 0.15F * 0.5F;
                if (cuttingBoard.isItemCarvingBoard()) {
                    this.renderItemCarved(poseStack, direction, itemStack);
                } else if (isBlockItem && !itemStack.is(ModTags.Items.FLAT_ON_CUTTING_BOARD)) {
                    this.renderBlock(poseStack, direction, xOffset, i, zOffset);
                } else {
                    this.renderItemLayingDown(poseStack, direction, xOffset, i, zOffset);
                }

                Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, poseStack, buffer, cuttingBoard.getLevel(), posLong);
                poseStack.popPose();
            }

        }
    }

    public void renderItemLayingDown(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
        matrixStackIn.translate((double)0.5F + (double)xOffset, 0.08 + 0.03 * (double)(yIndex + 1), (double)0.5F + (double)zOffset);
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }

    public void renderBlock(PoseStack matrixStackIn, Direction direction, float xOffset, int yIndex, float zOffset) {
        matrixStackIn.translate((double)0.5F + (double)xOffset, 0.27 + 0.03 * (double)(yIndex + 1), (double)0.5F + (double)zOffset);
        float f = -direction.toYRot();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
    }

    public void renderItemCarved(PoseStack matrixStackIn, Direction direction, ItemStack itemStack) {
        matrixStackIn.translate((double)0.5F, 0.23, (double)0.5F);
        float f = -direction.toYRot() + 180.0F;
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(f));
        Item toolItem = itemStack.getItem();
        float poseAngle;
        if (!(toolItem instanceof PickaxeItem) && !(toolItem instanceof HoeItem)) {
            if (toolItem instanceof TridentItem) {
                poseAngle = 135.0F;
            } else {
                poseAngle = 180.0F;
            }
        } else {
            poseAngle = 225.0F;
        }

        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(poseAngle));
        matrixStackIn.scale(0.6F, 0.6F, 0.6F);
    }

    protected int getModelCount(ItemStack stack) {
        int modelCount = 1;
        if (stack.getCount() > 1) {
            modelCount += Mth.ceil((float)stack.getCount() / (float)stack.getMaxStackSize() * 4.0F);
        }

        return modelCount;
    }
}