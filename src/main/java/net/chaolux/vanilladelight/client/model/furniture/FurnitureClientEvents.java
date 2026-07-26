package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.client.model.cabinet.ModularCabinetModelLoader;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.chaolux.vanilladelight.client.renderer.furniture.FurnitureBlockEntityRenderer;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.MOD, value = Dist.CLIENT)
public class FurnitureClientEvents {
    private FurnitureClientEvents() {

    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders geometryLoaders) {
        geometryLoaders.register("furniture", FurnitureModelLoader.INSTANCE);
        geometryLoaders.register("modular_cabinet", ModularCabinetModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(FurnitureClientReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(ModBlockEntityTypes.FURNITURE.get(),FurnitureBlockEntityRenderer::new));
    }
}
