package net.chaolux.vanilladelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;

public class CookingPotRenderer implements BlockEntityRenderer<CookingPotBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public CookingPotRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(CookingPotBlockEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var state = blockEntity.getBlockState();
        if (!(state.getBlock() instanceof CookingPotBlock)) return;
        var facing=state.getValue(CookingPotBlock.FACING);
        float y=switch(facing) {
            case NORTH -> 0f;
            case EAST -> 90f;
            case SOUTH -> 180f;
            case WEST -> 270f;
            default -> 0f;
        };
        String color=blockEntity.getPersistentData().getString("color");
        var support=state.getValue(CookingPotBlock.SUPPORT);
        String modelPath = switch (support) {
            case TRAY -> "_tray";
            case HANDLE -> "_handle";
            default -> "";
        };
        String fd="vanilladelight";
        String path="block/cooking_pot_default" + modelPath;
        if(!color.isEmpty()) {
            path="block/cooking_pot_" + color + modelPath;
        }
        var level=blockEntity.getLevel();
        if(level == null) return;
        var model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath(fd,path)));
        pose.pushPose();
        pose.translate(0.5,0.0,0.5);
        pose.mulPose(Axis.YP.rotationDegrees(y));
        pose.translate(-0.5,0.0,-0.5);
        long seed=state.getSeed(blockEntity.getBlockPos());
        dispatcher.getModelRenderer().tesselateBlock(level, model, state, blockEntity.getBlockPos(), pose, buf.getBuffer(RenderType.cutout()), false, level.random, seed, overlay);
        pose.popPose();
    }
}
