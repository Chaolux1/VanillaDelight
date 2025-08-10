package net.chaolux.vanilladelight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.block.state.CookingPotSupport;

public class CookingPotRenderer implements BlockEntityRenderer<CookingPotBlockEntity> {
    private final BlockRenderDispatcher dispatcher;

    public CookingPotRenderer(BlockEntityRendererProvider.Context context) {
        this.dispatcher = Minecraft.getInstance().getBlockRenderer();
    }

    @Override
    public void render(CookingPotBlockEntity blockEntity, float partialTick, PoseStack pose, MultiBufferSource buf, int light, int overlay) {
        var state = blockEntity.getBlockState();
        if (!(state.getBlock() instanceof CookingPotBlock)) return;
        String color=blockEntity.getPersistentData().getString("color");
        CookingPotSupport support=state.getValue(CookingPotBlock.SUPPORT);
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
        var model = Minecraft.getInstance().getModelManager().getModel(new ResourceLocation(fd,path));
        long seed=state.getSeed(blockEntity.getBlockPos());
        dispatcher.getModelRenderer().tesselateBlock(level, model, state, blockEntity.getBlockPos(), pose, buf.getBuffer(RenderType.cutout()), false, blockEntity.getLevel().random, seed, overlay);
    }
}
