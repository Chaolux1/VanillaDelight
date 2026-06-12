package net.chaolux.vanilladelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.StoveBlock;

@OnlyIn(Dist.CLIENT)
public class CommonStoveRenderer implements BlockEntityRenderer<CommonStoveBlockEntity> {
    public CommonStoveRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(CommonStoveBlockEntity stoveEntity, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        Direction direction = ((Direction)stoveEntity.getBlockState().getValue(StoveBlock.FACING)).getOpposite();
        ItemStackHandler inventory = stoveEntity.getItems();
        int posLong = (int)stoveEntity.getBlockPos().asLong();

        for(int i = 0; i < inventory.getSlots(); ++i) {
            ItemStack stoveStack = inventory.getStackInSlot(i);
            if (!stoveStack.isEmpty()) {
                poseStack.pushPose();
                poseStack.translate((double)0.5F, 1.02, (double)0.5F);
                float f = -direction.toYRot();
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                Vec2 itemOffset = stoveEntity.getStoveItemOffset(i);
                poseStack.translate((double)itemOffset.x, (double)itemOffset.y, (double)0.0F);
                poseStack.scale(0.375F, 0.375F, 0.375F);
                if (stoveEntity.getLevel() != null) {
                    Minecraft.getInstance().getItemRenderer().renderStatic(stoveStack, ItemDisplayContext.FIXED, LevelRenderer.getLightColor(stoveEntity.getLevel(), stoveEntity.getBlockPos().above()), combinedOverlayIn, poseStack, buffer, stoveEntity.getLevel(), posLong + i);
                }

                poseStack.popPose();
            }
        }

    }}
