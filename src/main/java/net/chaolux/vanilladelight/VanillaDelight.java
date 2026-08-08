package net.chaolux.vanilladelight;

import com.mojang.logging.LogUtils;
import net.chaolux.vanilladelight.client.renderer.CookingPotRenderer;
import net.chaolux.vanilladelight.client.renderer.CommonStoveRenderer;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefinitions;
import net.chaolux.vanilladelight.common.furniture.FurnitureReloadEvents;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.chaolux.vanilladelight.registry.crafting.ModRecipeSerializers;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.chaolux.vanilladelight.registry.particle.ModParticleTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
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
import net.chaolux.vanilladelight.client.renderer.CommonCuttingBoardRenderer;
@Mod(VanillaDelight.MOD_ID)
public class VanillaDelight
{
    public static final String MOD_ID = "vanilladelight";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VanillaDelight()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        FurnitureDefinitions.registered();
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntityTypes.TILES.register(modEventBus);
        ModParticleTypes.PARTICLE_TYPES.register(modEventBus);
        ModRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(FurnitureReloadEvents::addReloadListener);
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
            event.accept(ModItems.PUFFERFISH_STEW);
            event.accept(ModItems.SWEET_FISH_SOUP);
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
            event.accept(ModItems.PUFFERFISH_SLICE);
            event.accept(ModItems.CHARRED_PUMPKIN_SLICE);
            event.accept(ModItems.COOKED_BROWN_MUSHROOM);
            event.accept(ModItems.COOKED_RED_MUSHROOM);
            event.accept(ModItems.COOKED_BROWN_MUSHROOM_COLONY);
            event.accept(ModItems.COOKED_RED_MUSHROOM_COLONY);
            event.accept(ModItems.COOKED_TROPICAL_FISH);
            event.accept(ModItems.ROASTED_BEETS);
            event.accept(ModItems.ENCHANTED_GOLDEN_APPLE_SLICE);
            event.accept(ModItems.ENCHANTED_GOLDEN_CARROT);
            event.accept(ModItems.CHORUS_PIE_SLICE);
            event.accept(ModItems.MELON_PIE_SLICE);
            event.accept(ModItems.CARROT_CAKE_SLICE);
            event.accept(ModItems.HONEY_CAKE_SLICE);
            event.accept(ModItems.MILKY_PUMPKIN);
            event.accept(ModItems.CARROT_CAKE);
            event.accept(ModItems.HONEY_CAKE);
            event.accept(ModItems.MELON_PIE);
            event.accept(ModItems.CHORUS_PIE);
            event.accept(ModItems.MILKY_PUMPKIN_BLOCK);
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.DEEPSLATE_BRICKS_STOVE);
            event.accept(ModItems.END_STONE_BRICKS_STOVE);
            event.accept(ModItems.MUD_BRICKS_STOVE);
            event.accept(ModItems.NETHER_BRICKS_STOVE);
            event.accept(ModItems.STONE_BRICKS_STOVE);
            event.accept(ModItems.POLISHED_ANDESITE_STOVE);
            event.accept(ModItems.POLISHED_BASALT_STOVE);
            event.accept(ModItems.POLISHED_DEEPSLATE_STOVE);
            event.accept(ModItems.POLISHED_DIORITE_STOVE);
            event.accept(ModItems.POLISHED_GRANITE_STOVE);
            event.accept(ModItems.PURPUR_BLOCK_STOVE);
            event.accept(ModItems.RED_SANDSTONE_STOVE);
            event.accept(ModItems.SANDSTONE_STOVE);

            event.accept(ModItems.ACACIA_CUTTING_BOARD);
            event.accept(ModItems.BAMBOO_CUTTING_BOARD);
            event.accept(ModItems.BIRCH_CUTTING_BOARD);
            event.accept(ModItems.CHERRY_CUTTING_BOARD);
            event.accept(ModItems.CRIMSON_CUTTING_BOARD);
            event.accept(ModItems.DARK_OAK_CUTTING_BOARD);
            event.accept(ModItems.JUNGLE_CUTTING_BOARD);
            event.accept(ModItems.MANGROVE_CUTTING_BOARD);
            event.accept(ModItems.OAK_CUTTING_BOARD);
            event.accept(ModItems.WARPED_CUTTING_BOARD);
            event.accept(ModItems.SPRUCE_CUTTING_BOARD);

            event.accept(ModItems.DEEPSLATE_BRICKS_CABINET);
            event.accept(ModItems.END_STONE_BRICKS_CABINET);
            event.accept(ModItems.NETHER_BRICKS_CABINET);
            event.accept(ModItems.POLISHED_ANDESITE_CABINET);
            event.accept(ModItems.POLISHED_BASALT_CABINET);
            event.accept(ModItems.POLISHED_DEEPSLATE_CABINET);
            event.accept(ModItems.POLISHED_DIORITE_CABINET);
            event.accept(ModItems.POLISHED_GRANITE_CABINET);
            event.accept(ModItems.PURPUR_BLOCK_CABINET);
            event.accept(ModItems.RED_SANDSTONE_CABINET);
            event.accept(ModItems.SANDSTONE_CABINET);
            event.accept(ModItems.STONE_CABINET);

            event.accept(ModItems.MODULAR_CABINET);
            event.accept(ModItems.PATTERNED_CABINET);
            event.accept(ModItems.MODULAR_CUTTING_BOARD);
            event.accept(ModItems.SPIRAL_CUTTING_BOARD_PATTERN);
            event.accept(ModItems.FRAME_CUTTING_BOARD_PATTERN);
            event.accept(ModItems.TILES_CUTTING_BOARD_PATTERN);
            event.accept(ModItems.LINES_CUTTING_BOARD_PATTERN);
            event.accept(ModItems.DIAMOND_CUTTING_BOARD_PATTERN);
            event.accept(ModItems.MODULAR_STOVE);
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.COPPER_KNIFE);
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
            event.enqueueWork(() -> {
                BlockEntityRenderers.register(vectorwing.farmersdelight.common.registry.ModBlockEntityTypes.COOKING_POT.get(), CookingPotRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.COMMON_STOVE.get(), CommonStoveRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), CommonCuttingBoardRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.MODULAR_CUTTING_BOARD.get(), CommonCuttingBoardRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.MODULAR_STOVE.get(),CommonStoveRenderer::new);
            });
        }
    }
}
