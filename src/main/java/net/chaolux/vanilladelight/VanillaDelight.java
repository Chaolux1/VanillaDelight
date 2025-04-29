package net.chaolux.vanilladelight;

import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.chaolux.vanilladelight.registry.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(VanillaDelight.MOD_ID)
public class VanillaDelight
{
    public static final String MOD_ID = "vanilladelight";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VanillaDelight(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.APPLE_SALAD.get());
            event.accept(ModItems.BERRIES_SALAD.get());
            event.accept(ModItems.CARAMELIZED_CHICKEN.get());
            event.accept(ModItems.CARROT_SALAD.get());
            event.accept(ModItems.COD_STEW.get());
            event.accept(ModItems.GLOW_TROPICAL_FISH_STEW.get());
            event.accept(ModItems.GRILLED_BEETROOT.get());
            event.accept(ModItems.CARAMEL_BOTTLE.get());
            event.accept(ModItems.GLOW_BERRIES_JAM.get());
            event.accept(ModItems.GOLDEN_APPLE_CIDER.get());
            event.accept(ModItems.SWEET_BERRIES_JAM.get());
            event.accept(ModItems.SWEET_BERRY_CUSTARD.get());
            event.accept(ModItems.APPLE_SLICE.get());
            event.accept(ModItems.BUTTER.get());
            event.accept(ModItems.CARAMEL_CANDY.get());
            event.accept(ModItems.COOKED_APPLE.get());
            event.accept(ModItems.COOKED_CARROT.get());
            event.accept(ModItems.GLOW_BERRIES_CANDY.get());
            event.accept(ModItems.GOLDEN_APPLE_SLICE.get());
            event.accept(ModItems.HONEY_CANDY.get());
            event.accept(ModItems.HONEY_PUDDING.get());
            event.accept(ModItems.CHORUS_PIE_SLICE.get());
            event.accept(ModItems.MELON_PIE_SLICE.get());
            event.accept(ModItems.CARROT_CAKE_SLICE.get());
            event.accept(ModItems.HONEY_CAKE_SLICE.get());
            event.accept(ModItems.PUFFERFISH_SLICE.get());
            event.accept(ModItems.PUMPKIN_PIE_SLICE.get());
            event.accept(ModItems.MILKY_PUMPKIN.get());
            event.accept(ModItems.CARROT_CAKE.get());
            event.accept(ModItems.HONEY_CAKE.get());
            event.accept(ModItems.MELON_PIE.get());
            event.accept(ModItems.CHORUS_PIE.get());
            event.accept(ModItems.MILKY_PUMPKIN_BLOCK.get());
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
