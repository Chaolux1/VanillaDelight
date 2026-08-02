package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.client.model.cabinet.ModularCabinetModelLoader;
import net.chaolux.vanilladelight.client.model.modularcuttingboard.ModularCuttingBoardModelLoader;
import net.chaolux.vanilladelight.client.model.patternedcabinet.PatternedCabinetControlMap;
import net.chaolux.vanilladelight.client.model.patternedcabinet.PatternedCabinetModelLoader;
import net.chaolux.vanilladelight.common.block.entity.FurnitureBlockEntity;
import net.chaolux.vanilladelight.common.block.entity.ModularCuttingBoardBlockEntity;
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
        geometryLoaders.register("patterned_cabinet", PatternedCabinetModelLoader.INSTANCE);
        geometryLoaders.register("modular_cutting_board", ModularCuttingBoardModelLoader.INSTANCE);
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
