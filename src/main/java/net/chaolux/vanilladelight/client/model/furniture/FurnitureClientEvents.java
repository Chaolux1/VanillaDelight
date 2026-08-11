package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.client.model.cabinet.ModularCabinetModelLoader;
import net.chaolux.vanilladelight.client.model.modularcuttingboard.ModularCuttingBoardModelLoader;
import net.chaolux.vanilladelight.client.model.modularstove.ModularStoveModelLoader;
import net.chaolux.vanilladelight.client.model.patternedcabinet.PatternedCabinetModelLoader;
import net.chaolux.vanilladelight.client.particle.ModularFlameParticle;
import net.chaolux.vanilladelight.client.renderer.furniture.FurnitureBlockEntityRenderer;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.chaolux.vanilladelight.registry.particle.ModParticleTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.MOD, value = Dist.CLIENT)
public class FurnitureClientEvents {
    private FurnitureClientEvents() {

    }

    @SubscribeEvent
    public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders geometryLoaders) {
        geometryLoaders.register(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID, "furniture"), FurnitureModelLoader.INSTANCE);
        geometryLoaders.register(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID,"modular_cabinet"), ModularCabinetModelLoader.INSTANCE);
        geometryLoaders.register(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID,"patterned_cabinet"), PatternedCabinetModelLoader.INSTANCE);
        geometryLoaders.register(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID,"modular_cutting_board"), ModularCuttingBoardModelLoader.INSTANCE);
        geometryLoaders.register(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID,"modular_stove"), ModularStoveModelLoader.INSTANCE);
    }

    @SubscribeEvent
    public static void registerReloadListener(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(FurnitureClientReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public static void registerParticleProvides(RegisterParticleProvidersEvent registerParticleProvidersEvent) {
        registerParticleProvidersEvent.registerSpriteSet(ModParticleTypes.MODULAR_FLAME.get(), ModularFlameParticle.Provider::new);
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(ModBlockEntityTypes.FURNITURE.get(),FurnitureBlockEntityRenderer::new));
    }
}
