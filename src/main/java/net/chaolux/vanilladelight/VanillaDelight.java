package net.chaolux.vanilladelight;

import net.chaolux.vanilladelight.client.renderer.CommonCuttingBoardRenderer;
import net.chaolux.vanilladelight.client.renderer.CommonStoveRenderer;
import net.chaolux.vanilladelight.client.renderer.CookingPotRenderer;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
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
        ModBlockEntityTypes.TILES.register(modEventBus);

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
            event.accept(ModItems.PUFFERFISH_STEW.get());
            event.accept(ModItems.SWEET_FISH_SOUP.get());
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
            event.accept(ModItems.PUFFERFISH_SLICE.get());
            event.accept(ModItems.CHARRED_PUMPKIN_SLICE.get());
            event.accept(ModItems.COOKED_BROWN_MUSHROOM.get());
            event.accept(ModItems.COOKED_RED_MUSHROOM.get());
            event.accept(ModItems.COOKED_BROWN_MUSHROOM_COLONY.get());
            event.accept(ModItems.COOKED_RED_MUSHROOM_COLONY.get());
            event.accept(ModItems.COOKED_TROPICAL_FISH.get());
            event.accept(ModItems.ROASTED_BEETS.get());
            event.accept(ModItems.ENCHANTED_GOLDEN_APPLE_SLICE.get());
            event.accept(ModItems.ENCHANTED_GOLDEN_CARROT.get());
            event.accept(ModItems.CHORUS_PIE_SLICE.get());
            event.accept(ModItems.MELON_PIE_SLICE.get());
            event.accept(ModItems.CARROT_CAKE_SLICE.get());
            event.accept(ModItems.HONEY_CAKE_SLICE.get());
            event.accept(ModItems.MILKY_PUMPKIN.get());
            event.accept(ModItems.CARROT_CAKE.get());
            event.accept(ModItems.HONEY_CAKE.get());
            event.accept(ModItems.MELON_PIE.get());
            event.accept(ModItems.CHORUS_PIE.get());
            event.accept(ModItems.MILKY_PUMPKIN_BLOCK.get());
        }

        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.DEEPSLATE_BRICKS_STOVE.get());
            event.accept(ModItems.END_STONE_BRICKS_STOVE.get());
            event.accept(ModItems.MUD_BRICKS_STOVE.get());
            event.accept(ModItems.NETHER_BRICKS_STOVE.get());
            event.accept(ModItems.STONE_BRICKS_STOVE.get());
            event.accept(ModItems.POLISHED_ANDESITE_STOVE.get());
            event.accept(ModItems.POLISHED_BASALT_STOVE.get());
            event.accept(ModItems.POLISHED_DEEPSLATE_STOVE.get());
            event.accept(ModItems.POLISHED_DIORITE_STOVE.get());
            event.accept(ModItems.POLISHED_GRANITE_STOVE.get());
            event.accept(ModItems.PURPUR_BLOCK_STOVE.get());
            event.accept(ModItems.RED_SANDSTONE_STOVE.get());
            event.accept(ModItems.SANDSTONE_STOVE.get());

            event.accept(ModItems.ACACIA_CUTTING_BOARD.get());
            event.accept(ModItems.BAMBOO_CUTTING_BOARD.get());
            event.accept(ModItems.BIRCH_CUTTING_BOARD.get());
            event.accept(ModItems.CHERRY_CUTTING_BOARD.get());
            event.accept(ModItems.CRIMSON_CUTTING_BOARD.get());
            event.accept(ModItems.DARK_OAK_CUTTING_BOARD.get());
            event.accept(ModItems.JUNGLE_CUTTING_BOARD.get());
            event.accept(ModItems.MANGROVE_CUTTING_BOARD.get());
            event.accept(ModItems.OAK_CUTTING_BOARD.get());
            event.accept(ModItems.WARPED_CUTTING_BOARD.get());
            event.accept(ModItems.SPRUCE_CUTTING_BOARD.get());

            event.accept(ModItems.DEEPSLATE_BRICKS_CABINET.get());
            event.accept(ModItems.END_STONE_BRICKS_CABINET.get());
            event.accept(ModItems.NETHER_BRICKS_CABINET.get());
            event.accept(ModItems.POLISHED_ANDESITE_CABINET.get());
            event.accept(ModItems.POLISHED_BASALT_CABINET.get());
            event.accept(ModItems.POLISHED_DEEPSLATE_CABINET.get());
            event.accept(ModItems.POLISHED_DIORITE_CABINET.get());
            event.accept(ModItems.POLISHED_GRANITE_CABINET.get());
            event.accept(ModItems.PURPUR_BLOCK_CABINET.get());
            event.accept(ModItems.RED_SANDSTONE_CABINET.get());
            event.accept(ModItems.SANDSTONE_CABINET.get());
            event.accept(ModItems.STONE_CABINET.get());
        }
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(ModItems.COPPER_KNIFE.get());
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
            event.enqueueWork(() -> {
                BlockEntityRenderers.register(vectorwing.farmersdelight.common.registry.ModBlockEntityTypes.COOKING_POT.get(), CookingPotRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.COMMON_STOVE.get(), CommonStoveRenderer::new);
                BlockEntityRenderers.register(ModBlockEntityTypes.COMMON_CUTTING_BOARD.get(), CommonCuttingBoardRenderer::new);
            });
        }
    }
}
