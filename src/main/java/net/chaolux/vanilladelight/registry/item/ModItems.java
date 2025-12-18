package net.chaolux.vanilladelight.registry.item;

import net.chaolux.vanilladelight.common.item.*;
import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import vectorwing.farmersdelight.common.item.FuelBlockItem;
import vectorwing.farmersdelight.common.item.KnifeItem;

import java.util.function.Supplier;

import static vectorwing.farmersdelight.common.registry.ModItems.knifeItem;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS;

    public static final Supplier<Item> APPLE_SALAD;
    public static final Supplier<Item> APPLE_SLICE;
    public static final Supplier<Item> BERRIES_SALAD;
    public static final Supplier<Item> BUTTER;
    public static final Supplier<Item> CARAMEL_BOTTLE;
    public static final Supplier<Item> CARAMEL_CANDY;
    public static final Supplier<Item> CARAMELIZED_CHICKEN;
    public static final Supplier<Item> CARROT_SALAD;
    public static final Supplier<Item> COD_STEW;
    public static final Supplier<Item> COOKED_APPLE;
    public static final Supplier<Item> COOKED_CARROT;
    public static final Supplier<Item> GLOW_BERRIES_CANDY;
    public static final Supplier<Item> GLOW_BERRIES_JAM;
    public static final Supplier<Item> GLOW_TROPICAL_FISH_STEW;
    public static final Supplier<Item> GOLDEN_APPLE_CIDER;
    public static final Supplier<Item> GOLDEN_APPLE_SLICE;
    public static final Supplier<Item> GRILLED_BEETROOT;
    public static final Supplier<Item> HONEY_CANDY;
    public static final Supplier<Item> HONEY_PUDDING;
    public static final Supplier<Item> SWEET_BERRIES_JAM;
    public static final Supplier<Item> SWEET_BERRY_CUSTARD;
    public static final Supplier<Item> CHORUS_PIE_SLICE;
    public static final Supplier<Item> MELON_PIE_SLICE;
    public static final Supplier<Item> PUFFERFISH_SLICE;
    public static final Supplier<Item> PUMPKIN_PIE_SLICE;
    public static final Supplier<Item> MILKY_PUMPKIN;
    public static final Supplier<Item> CARROT_CAKE;
    public static final Supplier<Item> HONEY_CAKE;
    public static final Supplier<Item> MELON_PIE;
    public static final Supplier<Item> CHORUS_PIE;
    public static final Supplier<Item> CARROT_CAKE_SLICE;
    public static final Supplier<Item> HONEY_CAKE_SLICE;
    public static final Supplier<Item> MILKY_PUMPKIN_BLOCK;

    public static final Supplier<Item> CHARRED_PUMPKIN_SLICE;
    public static final Supplier<Item> COOKED_BROWN_MUSHROOM;
    public static final Supplier<Item> COOKED_RED_MUSHROOM;
    public static final Supplier<Item> COOKED_BROWN_MUSHROOM_COLONY;
    public static final Supplier<Item> COOKED_RED_MUSHROOM_COLONY;
    public static final Supplier<Item> COOKED_TROPICAL_FISH;
    public static final Supplier<Item> PUFFERFISH_STEW;
    public static final Supplier<Item> ROASTED_BEETS;
    public static final Supplier<Item> SWEET_FISH_SOUP;
    public static final Supplier<Item> ENCHANTED_GOLDEN_APPLE_SLICE;
    public static final Supplier<Item> ENCHANTED_GOLDEN_CARROT;
    public static final Supplier<Item> DEEPSLATE_BRICKS_STOVE;
    public static final Supplier<Item> END_STONE_BRICKS_STOVE;
    public static final Supplier<Item> MUD_BRICKS_STOVE;
    public static final Supplier<Item> NETHER_BRICKS_STOVE;
    public static final Supplier<Item> STONE_BRICKS_STOVE;
    public static final Supplier<Item> POLISHED_ANDESITE_STOVE;
    public static final Supplier<Item> POLISHED_BASALT_STOVE;
    public static final Supplier<Item> POLISHED_DEEPSLATE_STOVE;
    public static final Supplier<Item> POLISHED_DIORITE_STOVE;
    public static final Supplier<Item> POLISHED_GRANITE_STOVE;
    public static final Supplier<Item> PURPUR_BLOCK_STOVE;
    public static final Supplier<Item> RED_SANDSTONE_STOVE;
    public static final Supplier<Item> SANDSTONE_STOVE;
    public static final Supplier<Item> COPPER_KNIFE;
    public static final Supplier<Item> ACACIA_CUTTING_BOARD;
    public static final Supplier<Item> BAMBOO_CUTTING_BOARD;
    public static final Supplier<Item> BIRCH_CUTTING_BOARD;
    public static final Supplier<Item> CHERRY_CUTTING_BOARD;
    public static final Supplier<Item> CRIMSON_CUTTING_BOARD;
    public static final Supplier<Item> DARK_OAK_CUTTING_BOARD;
    public static final Supplier<Item> JUNGLE_CUTTING_BOARD;
    public static final Supplier<Item> MANGROVE_CUTTING_BOARD;
    public static final Supplier<Item> OAK_CUTTING_BOARD;
    public static final Supplier<Item> WARPED_CUTTING_BOARD;
    public static final Supplier<Item> DEEPSLATE_BRICKS_CABINET;
    public static final Supplier<Item> END_STONE_BRICKS_CABINET;
    public static final Supplier<Item> NETHER_BRICKS_CABINET;
    public static final Supplier<Item> POLISHED_ANDESITE_CABINET;
    public static final Supplier<Item> POLISHED_BASALT_CABINET;
    public static final Supplier<Item> POLISHED_DEEPSLATE_CABINET;
    public static final Supplier<Item> POLISHED_DIORITE_CABINET;
    public static final Supplier<Item> POLISHED_GRANITE_CABINET;
    public static final Supplier<Item> PURPUR_BLOCK_CABINET;
    public static final Supplier<Item> RED_SANDSTONE_CABINET;
    public static final Supplier<Item> SANDSTONE_CABINET;
    public static final Supplier<Item> STONE_CABINET;

    public static Supplier<Item> registerWithTab(String name, Supplier<Item> supplier) {
        Supplier<Item> block = ITEMS.register(name, supplier);
        return block;
    }

    public static Item.Properties basicItem() {
        return new Item.Properties();
    }

    public static Item.Properties bowlFoodItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.BOWL).stacksTo(16);
    }

    public static Item.Properties drinkItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(Items.GLASS_BOTTLE).stacksTo(16);
    }

    public static Item.Properties potItem(FoodProperties food) {
        return (new Item.Properties()).food(food).craftRemainder(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("farmersdelight","cooking_pot"))).stacksTo(1);
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    static {
        ITEMS = DeferredRegister.create(Registries.ITEM, "vanilladelight");

        APPLE_SALAD = registerWithTab("apple_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.APPLE_SALAD), true));
        BERRIES_SALAD = registerWithTab("berries_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.BERRIES_SALAD), true));
        CARAMELIZED_CHICKEN = registerWithTab("caramelized_chicken", () -> new ConsumableItem(bowlFoodItem(FoodValues.CARAMELIZED_CHICKEN), true));
        CARROT_SALAD = registerWithTab("carrot_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.CARROT_SALAD), true));
        COD_STEW = registerWithTab("cod_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.COD_STEW)));
        GLOW_TROPICAL_FISH_STEW = registerWithTab("glow_tropical_fish_stew", () -> new GlowTropicalFishStewItem(bowlFoodItem(FoodValues.GLOW_TROPICAL_FISH_STEW)));
        GRILLED_BEETROOT = registerWithTab("grilled_beetroot", () -> new GrilledBeetrootItem(bowlFoodItem(FoodValues.GRILLED_BEETROOT)));
        MILKY_PUMPKIN = registerWithTab("milky_pumpkin", () -> new ConsumableItem(bowlFoodItem(FoodValues.MILKY_PUMPKIN), true));
        PUFFERFISH_STEW = registerWithTab("pufferfish_stew", () -> new PufferfishStewItem(bowlFoodItem(FoodValues.PUFFERFISH_STEW).stacksTo(16)));
        SWEET_FISH_SOUP = registerWithTab("sweet_fish_soup", () -> new SweetFishSoupItem(potItem(FoodValues.SWEET_FISH_SOUP).stacksTo(1)));

        CARAMEL_BOTTLE = registerWithTab("caramel_bottle", () -> new CaramelBottleItem(drinkItem(FoodValues.CARAMEL_BOTTLE)));
        GLOW_BERRIES_JAM = registerWithTab("glow_berries_jam", () -> new GlowBerriesJamItem(drinkItem(FoodValues.GLOW_BERRIES_JAM)));
        GOLDEN_APPLE_CIDER = registerWithTab("golden_apple_cider", () -> new GoldenAppleCiderItem(drinkItem(FoodValues.GOLDEN_APPLE_CIDER)));
        SWEET_BERRIES_JAM = registerWithTab("sweet_berries_jam", () -> new SweetBerriesJamItem(drinkItem(FoodValues.SWEET_BERRIES_JAM)));
        SWEET_BERRY_CUSTARD = registerWithTab("sweet_berry_custard", () -> new SweetBerryCustard(drinkItem(FoodValues.SWEET_BERRY_CUSTARD)));

        APPLE_SLICE = registerWithTab("apple_slice", () -> new Item(foodItem(FoodValues.APPLE_SLICE)));
        BUTTER = registerWithTab("butter", () -> new Item(foodItem(FoodValues.BUTTER)));
        CARAMEL_CANDY = registerWithTab("caramel_candy", () -> new ConsumableItem(foodItem(FoodValues.CARAMEL_CANDY),true));
        COOKED_APPLE = registerWithTab("cooked_apple", () -> new Item(foodItem(FoodValues.COOKED_APPLE)));
        COOKED_CARROT = registerWithTab("cooked_carrot", () -> new Item(foodItem(FoodValues.COOKED_CARROT)));
        GLOW_BERRIES_CANDY = registerWithTab("glow_berries_candy", () -> new ConsumableItem(foodItem(FoodValues.GLOW_BERRIES_CANDY),true));
        GOLDEN_APPLE_SLICE = registerWithTab("golden_apple_slice", () -> new ConsumableItem(foodItem(FoodValues.GOLDEN_APPLE_SLICE),true));
        HONEY_CANDY = registerWithTab("honey_candy", () -> new ConsumableItem(foodItem(FoodValues.HONEY_CANDY),true));
        HONEY_PUDDING = registerWithTab("honey_pudding", () -> new ConsumableItem(foodItem(FoodValues.HONEY_PUDDING),true));
        CHORUS_PIE_SLICE = registerWithTab("chorus_pie_slice", () -> new ConsumableItem(foodItem(FoodValues.CHORUS_PIE_SLICE),true));
        MELON_PIE_SLICE = registerWithTab("melon_pie_slice", () -> new ConsumableItem(foodItem(FoodValues.MELON_PIE_SLICE),true));
        CARROT_CAKE_SLICE = registerWithTab("carrot_cake_slice", () -> new ConsumableItem(foodItem(FoodValues.CARROT_CAKE_SLICE),true));
        HONEY_CAKE_SLICE = registerWithTab("honey_cake_slice", () -> new ConsumableItem(foodItem(FoodValues.HONEY_CAKE_SLICE),true));
        PUFFERFISH_SLICE = registerWithTab("pufferfish_slice", () -> new ConsumableItem(foodItem(FoodValues.PUFFERFISH_SLICE),true));
        PUMPKIN_PIE_SLICE = registerWithTab("pumpkin_pie_slice", () -> new ConsumableItem(foodItem(FoodValues.PUMPKIN_PIE_SLICE),true));

        CHARRED_PUMPKIN_SLICE = registerWithTab("charred_pumpkin_slice", () -> new Item(foodItem(FoodValues.CHARRED_PUMPKIN_SLICE)));
        COOKED_BROWN_MUSHROOM = registerWithTab("cooked_brown_mushroom", () -> new Item(foodItem(FoodValues.COOKED_BROWN_MUSHROOM)));
        COOKED_RED_MUSHROOM = registerWithTab("cooked_red_mushroom", () -> new CookedRedMushroomItem(foodItem(FoodValues.COOKED_RED_MUSHROOM)));
        COOKED_BROWN_MUSHROOM_COLONY = registerWithTab("cooked_brown_mushroom_colony", () -> new Item(foodItem(FoodValues.COOKED_BROWN_MUSHROOM_COLONY)));
        COOKED_RED_MUSHROOM_COLONY = registerWithTab("cooked_red_mushroom_colony", () -> new CookedRedMushroomItem(foodItem(FoodValues.COOKED_RED_MUSHROOM_COLONY)));
        COOKED_TROPICAL_FISH = registerWithTab("cooked_tropical_fish", () -> new Item(foodItem(FoodValues.COOKED_TROPICAL_FISH)));
        ROASTED_BEETS = registerWithTab("roasted_beets", () -> new Item(foodItem(FoodValues.ROASTED_BEETS)));
        ENCHANTED_GOLDEN_APPLE_SLICE = registerWithTab("enchanted_golden_apple_slice", () -> new EnchantedConsumableItem(foodItem(FoodValues.ENCHANTED_GOLDEN_APPLE_SLICE)));
        ENCHANTED_GOLDEN_CARROT = registerWithTab("enchanted_golden_carrot", () -> new EnchantedConsumableItem(foodItem(FoodValues.ENCHANTED_GOLDEN_CARROT)));

        COPPER_KNIFE = registerWithTab("copper_knife", () -> new KnifeItem(Tiers.IRON, knifeItem(Tiers.IRON)));

        CARROT_CAKE = registerWithTab("carrot_cake", () -> new BlockItem((Block)ModBlocks.CARROT_CAKE.get(), basicItem()));
        HONEY_CAKE = registerWithTab("honey_cake", () -> new BlockItem((Block)ModBlocks.HONEY_CAKE.get(), basicItem()));
        MELON_PIE = registerWithTab("melon_pie", () -> new BlockItem((Block)ModBlocks.MELON_PIE.get(), basicItem()));
        CHORUS_PIE = registerWithTab("chorus_pie", () -> new BlockItem((Block)ModBlocks.CHORUS_PIE.get(), basicItem()));
        MILKY_PUMPKIN_BLOCK = registerWithTab("milky_pumpkin_block", () -> new BlockItem((Block) ModBlocks.MILKY_PUMPKIN_BLOCK.get(), basicItem().stacksTo(1)));

        DEEPSLATE_BRICKS_STOVE = registerWithTab("deepslate_bricks_stove", () -> new BlockItem((Block) ModBlocks.DEEPSLATE_BRICKS_STOVE.get(), basicItem()));
        END_STONE_BRICKS_STOVE = registerWithTab("end_stone_bricks_stove", () -> new BlockItem((Block) ModBlocks.END_STONE_BRICKS_STOVE.get(), basicItem()));
        MUD_BRICKS_STOVE = registerWithTab("mud_bricks_stove", () -> new BlockItem((Block) ModBlocks.MUD_BRICKS_STOVE.get(), basicItem()));
        NETHER_BRICKS_STOVE = registerWithTab("nether_bricks_stove", () -> new BlockItem((Block) ModBlocks.NETHER_BRICKS_STOVE.get(), basicItem()));
        STONE_BRICKS_STOVE = registerWithTab("stone_bricks_stove", () -> new BlockItem((Block) ModBlocks.STONE_BRICKS_STOVE.get(), basicItem()));
        POLISHED_ANDESITE_STOVE = registerWithTab("polished_andesite_stove", () -> new BlockItem((Block) ModBlocks.POLISHED_ANDESITE_STOVE.get(), basicItem()));
        POLISHED_BASALT_STOVE = registerWithTab("polished_basalt_stove", () -> new BlockItem((Block) ModBlocks.POLISHED_BASALT_STOVE.get(), basicItem()));
        POLISHED_DEEPSLATE_STOVE = registerWithTab("polished_deepslate_stove", () -> new BlockItem((Block) ModBlocks.POLISHED_DEEPSLATE_STOVE.get(), basicItem()));
        POLISHED_DIORITE_STOVE = registerWithTab("polished_diorite_stove", () -> new BlockItem((Block) ModBlocks.POLISHED_DIORITE_STOVE.get(), basicItem()));
        POLISHED_GRANITE_STOVE = registerWithTab("polished_granite_stove", () -> new BlockItem((Block) ModBlocks.POLISHED_GRANITE_STOVE.get(), basicItem()));
        PURPUR_BLOCK_STOVE = registerWithTab("purpur_stove", () -> new BlockItem((Block) ModBlocks.PURPUR_BLOCK_STOVE.get(), basicItem()));
        RED_SANDSTONE_STOVE = registerWithTab("red_sandstone_stove", () -> new BlockItem((Block) ModBlocks.RED_SANDSTONE_STOVE.get(), basicItem()));
        SANDSTONE_STOVE = registerWithTab("sandstone_stove", () -> new BlockItem((Block) ModBlocks.SANDSTONE_STOVE.get(), basicItem()));

        ACACIA_CUTTING_BOARD = registerWithTab("acacia_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.ACACIA_CUTTING_BOARD.get(), basicItem(), 200));
        BAMBOO_CUTTING_BOARD = registerWithTab("bamboo_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.BAMBOO_CUTTING_BOARD.get(), basicItem(), 200));
        BIRCH_CUTTING_BOARD = registerWithTab("birch_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.BIRCH_CUTTING_BOARD.get(), basicItem(), 200));
        CHERRY_CUTTING_BOARD = registerWithTab("cherry_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.CHERRY_CUTTING_BOARD.get(), basicItem(), 200));
        CRIMSON_CUTTING_BOARD = registerWithTab("crimson_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.CRIMSON_CUTTING_BOARD.get(), basicItem(), 200));
        DARK_OAK_CUTTING_BOARD = registerWithTab("dark_oak_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.DARK_OAK_CUTTING_BOARD.get(), basicItem(), 200));
        JUNGLE_CUTTING_BOARD = registerWithTab("jungle_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.JUNGLE_CUTTING_BOARD.get(), basicItem(), 200));
        MANGROVE_CUTTING_BOARD = registerWithTab("mangrove_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.MANGROVE_CUTTING_BOARD.get(), basicItem(), 200));
        OAK_CUTTING_BOARD = registerWithTab("oak_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.OAK_CUTTING_BOARD.get(), basicItem(), 200));
        WARPED_CUTTING_BOARD = registerWithTab("warped_cutting_board", () -> new FuelBlockItem((Block) ModBlocks.WARPED_CUTTING_BOARD.get(), basicItem(), 200));

        DEEPSLATE_BRICKS_CABINET = registerWithTab("deepslate_bricks_cabinet", () -> new FuelBlockItem((Block) ModBlocks.DEEPSLATE_BRICKS_CABINET.get(), basicItem(), 300));
        END_STONE_BRICKS_CABINET = registerWithTab("end_stone_bricks_cabinet", () -> new FuelBlockItem((Block) ModBlocks.END_STONE_BRICKS_CABINET.get(), basicItem(), 300));
        NETHER_BRICKS_CABINET = registerWithTab("nether_bricks_cabinet", () -> new FuelBlockItem((Block) ModBlocks.NETHER_BRICKS_CABINET.get(), basicItem(), 300));
        POLISHED_ANDESITE_CABINET = registerWithTab("polished_andesite_cabinet", () -> new FuelBlockItem((Block) ModBlocks.POLISHED_ANDESITE_CABINET.get(), basicItem(), 300));
        POLISHED_BASALT_CABINET = registerWithTab("polished_basalt_cabinet", () -> new FuelBlockItem((Block) ModBlocks.POLISHED_BASALT_CABINET.get(), basicItem(), 300));
        POLISHED_DEEPSLATE_CABINET = registerWithTab("polished_deepslate_cabinet", () -> new FuelBlockItem((Block) ModBlocks.POLISHED_DEEPSLATE_CABINET.get(), basicItem(), 300));
        POLISHED_DIORITE_CABINET = registerWithTab("polished_diorite_cabinet", () -> new FuelBlockItem((Block) ModBlocks.POLISHED_DIORITE_CABINET.get(), basicItem(), 300));
        POLISHED_GRANITE_CABINET = registerWithTab("polished_granite_cabinet", () -> new FuelBlockItem((Block) ModBlocks.POLISHED_GRANITE_CABINET.get(), basicItem(), 300));
        PURPUR_BLOCK_CABINET = registerWithTab("purpur_block_cabinet", () -> new FuelBlockItem((Block) ModBlocks.PURPUR_BLOCK_CABINET.get(), basicItem(), 300));
        RED_SANDSTONE_CABINET = registerWithTab("red_sandstone_cabinet", () -> new FuelBlockItem((Block) ModBlocks.RED_SANDSTONE_CABINET.get(), basicItem(), 300));
        SANDSTONE_CABINET = registerWithTab("sandstone_cabinet", () -> new FuelBlockItem((Block) ModBlocks.SANDSTONE_CABINET.get(), basicItem(), 300));
        STONE_CABINET = registerWithTab("stone_cabinet", () -> new FuelBlockItem((Block) ModBlocks.STONE_CABINET.get(), basicItem(), 300));
    }
}
