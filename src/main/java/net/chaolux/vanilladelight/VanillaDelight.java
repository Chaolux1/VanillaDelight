package net.chaolux.vanilladelight;

import com.mojang.logging.LogUtils;
import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(VanillaDelight.MOD_ID)
public class VanillaDelight
{
    public static final String MOD_ID = "vanilladelight";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VanillaDelight()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(ModItems.APPLE_SALAD);
            event.accept(ModItems.BERRIES_SALAD);
            event.accept(ModItems.CARAMELIZED_CHICKEN);
            event.accept(ModItems.CARROT_SALAD);
            event.accept(ModItems.COD_STEW);
            event.accept(ModItems.GLOW_TROPICAL_FISH_STEW);
            event.accept(ModItems.GRILLED_BEETROOT);
            event.accept(ModItems.CARAMEL_BOTTLE);
            event.accept(ModItems.GLOW_BERRIES_JAM);
            event.accept(ModItems.GOLDEN_APPLE_CIDER);
            event.accept(ModItems.SWEET_BERRIES_JAM);
            event.accept(ModItems.SWEET_BERRY_CUSTARD);
            event.accept(ModItems.APPLE_SLICE);
            event.accept(ModItems.BUTTER);
            event.accept(ModItems.CARAMEL_CANDY);
            event.accept(ModItems.COOKED_APPLE);
            event.accept(ModItems.COOKED_CARROT);
            event.accept(ModItems.GLOW_BERRIES_CANDY);
            event.accept(ModItems.GOLDEN_APPLE_SLICE);
            event.accept(ModItems.HONEY_CANDY);
            event.accept(ModItems.HONEY_PUDDING);
            event.accept(ModItems.CHORUS_PIE_SLICE);
            event.accept(ModItems.MELON_PIE_SLICE);
            event.accept(ModItems.CARROT_CAKE_SLICE);
            event.accept(ModItems.HONEY_CAKE_SLICE);
            event.accept(ModItems.PUFFERFISH_SLICE);
            event.accept(ModItems.PUMPKIN_PIE_SLICE);
            event.accept(ModItems.MILKY_PUMPKIN);
            event.accept(ModItems.CARROT_CAKE);
            event.accept(ModItems.HONEY_CAKE);
            event.accept(ModItems.MELON_PIE);
            event.accept(ModItems.CHORUS_PIE);
            event.accept(ModItems.MILKY_PUMPKIN_BLOCK);
        }

    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
