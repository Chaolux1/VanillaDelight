package net.chaolux.vanilladelight.registry.item;

import net.chaolux.vanilladelight.common.item.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.item.ConsumableItem;
import net.chaolux.vanilladelight.registry.block.ModBlocks;
import vectorwing.farmersdelight.common.item.CookingPotItem;
import vectorwing.farmersdelight.common.item.DrinkableItem;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS;

    public static final RegistryObject<Item> APPLE_SALAD;
    public static final RegistryObject<Item> APPLE_SLICE;
    public static final RegistryObject<Item> BERRIES_SALAD;
    public static final RegistryObject<Item> BUTTER;
    public static final RegistryObject<Item> CARAMEL_BOTTLE;
    public static final RegistryObject<Item> CARAMEL_CANDY;
    public static final RegistryObject<Item> CARAMELIZED_CHICKEN;
    public static final RegistryObject<Item> CARROT_SALAD;
    public static final RegistryObject<Item> COD_STEW;
    public static final RegistryObject<Item> COOKED_APPLE;
    public static final RegistryObject<Item> COOKED_CARROT;
    public static final RegistryObject<Item> GLOW_BERRIES_CANDY;
    public static final RegistryObject<Item> GLOW_BERRIES_JAM;
    public static final RegistryObject<Item> GLOW_TROPICAL_FISH_STEW;
    public static final RegistryObject<Item> GOLDEN_APPLE_CIDER;
    public static final RegistryObject<Item> GOLDEN_APPLE_SLICE;
    public static final RegistryObject<Item> GRILLED_BEETROOT;
    public static final RegistryObject<Item> HONEY_CANDY;
    public static final RegistryObject<Item> HONEY_PUDDING;
    public static final RegistryObject<Item> SWEET_BERRIES_JAM;
    public static final RegistryObject<Item> SWEET_BERRY_CUSTARD;
    public static final RegistryObject<Item> CHORUS_PIE_SLICE;
    public static final RegistryObject<Item> MELON_PIE_SLICE;
    public static final RegistryObject<Item> PUFFERFISH_SLICE;
    public static final RegistryObject<Item> PUMPKIN_PIE_SLICE;
    public static final RegistryObject<Item> MILKY_PUMPKIN;
    public static final RegistryObject<Item> CARROT_CAKE;
    public static final RegistryObject<Item> HONEY_CAKE;
    public static final RegistryObject<Item> MELON_PIE;
    public static final RegistryObject<Item> CHORUS_PIE;
    public static final RegistryObject<Item> CARROT_CAKE_SLICE;
    public static final RegistryObject<Item> HONEY_CAKE_SLICE;
    public static final RegistryObject<Item> MILKY_PUMPKIN_BLOCK;
    public static final RegistryObject<Item> CHARRED_PUMPKIN_SLICE;
    public static final RegistryObject<Item> COOKED_BROWN_MUSHROOM;
    public static final RegistryObject<Item> COOKED_RED_MUSHROOM;
    public static final RegistryObject<Item> COOKED_BROWN_MUSHROOM_COLONY;
    public static final RegistryObject<Item> COOKED_RED_MUSHROOM_COLONY;
    public static final RegistryObject<Item> COOKED_TROPICAL_FISH;
    public static final RegistryObject<Item> PUFFERFISH_STEW;
    public static final RegistryObject<Item> ROASTED_BEETS;
    public static final RegistryObject<Item> SWEET_FISH_SOUP;
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_APPLE_SLICE;
    public static final RegistryObject<Item> ENCHANTED_GOLDEN_CARROT;
    public static final RegistryObject<Item> DEEPSLATE_BRICKS_STOVE;
    public static final RegistryObject<Item> END_STONE_BRICKS_STOVE;
    public static final RegistryObject<Item> MUD_BRICKS_STOVE;
    public static final RegistryObject<Item> NETHER_BRICKS_STOVE;
    public static final RegistryObject<Item> STONE_BRICKS_STOVE;
    public static final RegistryObject<Item> POLISHED_ANDESITE_STOVE;
    public static final RegistryObject<Item> POLISHED_BASALT_STOVE;
    public static final RegistryObject<Item> POLISHED_DEEPSLATE_STOVE;
    public static final RegistryObject<Item> POLISHED_DIORITE_STOVE;
    public static final RegistryObject<Item> POLISHED_GRANITE_STOVE;
    public static final RegistryObject<Item> PURPUR_BLOCK_STOVE;
    public static final RegistryObject<Item> RED_SANDSTONE_STOVE;
    public static final RegistryObject<Item> SANDSTONE_STOVE;



    public static RegistryObject<Item> registerWithTab(String name, Supplier<Item> supplier) {
        RegistryObject<Item> block = ITEMS.register(name, supplier);
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
        return (new Item.Properties()).food(food).craftRemainder(BuiltInRegistries.ITEM.get(new ResourceLocation("farmersdelight","cooking_pot"))).stacksTo(1);
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return (new Item.Properties()).food(food);
    }

    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "vanilladelight");

        APPLE_SALAD = registerWithTab("apple_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.APPLE_SALAD).stacksTo(16), true));
        BERRIES_SALAD = registerWithTab("berries_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.BERRIES_SALAD).stacksTo(16), true));
        CARAMELIZED_CHICKEN = registerWithTab("caramelized_chicken", () -> new ConsumableItem(bowlFoodItem(FoodValues.CARAMELIZED_CHICKEN).stacksTo(16), true));
        CARROT_SALAD = registerWithTab("carrot_salad", () -> new ConsumableItem(bowlFoodItem(FoodValues.CARROT_SALAD).stacksTo(16), true));
        COD_STEW = registerWithTab("cod_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.COD_STEW).stacksTo(16)));
        GLOW_TROPICAL_FISH_STEW = registerWithTab("glow_tropical_fish_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.GLOW_TROPICAL_FISH_STEW).stacksTo(16)));
        GRILLED_BEETROOT = registerWithTab("grilled_beetroot", () -> new ConsumableItem(bowlFoodItem(FoodValues.GRILLED_BEETROOT).stacksTo(16)));
        MILKY_PUMPKIN = registerWithTab("milky_pumpkin", () -> new ConsumableItem(bowlFoodItem(FoodValues.MILKY_PUMPKIN).stacksTo(16), true));
        PUFFERFISH_STEW = registerWithTab("pufferfish_stew", () -> new PufferfishStewItem(bowlFoodItem(FoodValues.PUFFERFISH_STEW).stacksTo(16)));
        SWEET_FISH_SOUP = registerWithTab("sweet_fish_soup", () -> new ConsumableItem(potItem(FoodValues.SWEET_FISH_SOUP).stacksTo(1), true));

        CARAMEL_BOTTLE = registerWithTab("caramel_bottle", () -> new CaramelBottleItem(drinkItem(FoodValues.CARAMEL_BOTTLE).stacksTo(16)));
        GLOW_BERRIES_JAM = registerWithTab("glow_berries_jam", () -> new GlowBerriesJamItem(drinkItem(FoodValues.GLOW_BERRIES_JAM).stacksTo(16)));
        GOLDEN_APPLE_CIDER = registerWithTab("golden_apple_cider", () -> new GoldenAppleCiderItem(drinkItem(FoodValues.GOLDEN_APPLE_CIDER).stacksTo(16)));
        SWEET_BERRIES_JAM = registerWithTab("sweet_berries_jam", () -> new SweetBerriesJamItem(drinkItem(FoodValues.SWEET_BERRIES_JAM).stacksTo(16)));
        SWEET_BERRY_CUSTARD = registerWithTab("sweet_berry_custard", () -> new SweetBerryCustard(drinkItem(FoodValues.SWEET_BERRY_CUSTARD).stacksTo(16)));

        APPLE_SLICE = registerWithTab("apple_slice", () -> new Item(foodItem(FoodValues.APPLE_SLICE)));
        BUTTER = registerWithTab("butter", () -> new Item(foodItem(FoodValues.BUTTER)));
        CARAMEL_CANDY = registerWithTab("caramel_candy", () -> new Item(foodItem(FoodValues.CARAMEL_CANDY)));
        COOKED_APPLE = registerWithTab("cooked_apple", () -> new Item(foodItem(FoodValues.COOKED_APPLE)));
        COOKED_CARROT = registerWithTab("cooked_carrot", () -> new Item(foodItem(FoodValues.COOKED_CARROT)));
        GLOW_BERRIES_CANDY = registerWithTab("glow_berries_candy", () -> new Item(foodItem(FoodValues.GLOW_BERRIES_CANDY)));
        GOLDEN_APPLE_SLICE = registerWithTab("golden_apple_slice", () -> new Item(foodItem(FoodValues.GOLDEN_APPLE_SLICE)));
        HONEY_CANDY = registerWithTab("honey_candy", () -> new Item(foodItem(FoodValues.HONEY_CANDY)));
        HONEY_PUDDING = registerWithTab("honey_pudding", () -> new Item(foodItem(FoodValues.HONEY_PUDDING)));
        CHORUS_PIE_SLICE = registerWithTab("chorus_pie_slice", () -> new Item(foodItem(FoodValues.CHORUS_PIE_SLICE)));
        MELON_PIE_SLICE = registerWithTab("melon_pie_slice", () -> new Item(foodItem(FoodValues.MELON_PIE_SLICE)));
        CARROT_CAKE_SLICE = registerWithTab("carrot_cake_slice", () -> new Item(foodItem(FoodValues.CARROT_CAKE_SLICE)));
        HONEY_CAKE_SLICE = registerWithTab("honey_cake_slice", () -> new Item(foodItem(FoodValues.HONEY_CAKE_SLICE)));
        PUFFERFISH_SLICE = registerWithTab("pufferfish_slice", () -> new Item(foodItem(FoodValues.PUFFERFISH_SLICE)));
        PUMPKIN_PIE_SLICE = registerWithTab("pumpkin_pie_slice", () -> new Item(foodItem(FoodValues.PUMPKIN_PIE_SLICE)));

        CHARRED_PUMPKIN_SLICE = registerWithTab("charred_pumpkin_slice", () -> new Item(foodItem(FoodValues.CHARRED_PUMPKIN_SLICE)));
        COOKED_BROWN_MUSHROOM = registerWithTab("cooked_brown_mushroom", () -> new Item(foodItem(FoodValues.COOKED_BROWN_MUSHROOM)));
        COOKED_RED_MUSHROOM = registerWithTab("cooked_red_mushroom", () -> new CookedRedMushroomItem(foodItem(FoodValues.COOKED_RED_MUSHROOM)));
        COOKED_BROWN_MUSHROOM_COLONY = registerWithTab("cooked_brown_mushroom_colony", () -> new Item(foodItem(FoodValues.COOKED_BROWN_MUSHROOM_COLONY)));
        COOKED_RED_MUSHROOM_COLONY = registerWithTab("cooked_red_mushroom_colony", () -> new CookedRedMushroomItem(foodItem(FoodValues.COOKED_RED_MUSHROOM_COLONY)));
        COOKED_TROPICAL_FISH = registerWithTab("cooked_tropical_fish", () -> new Item(foodItem(FoodValues.COOKED_TROPICAL_FISH)));
        ROASTED_BEETS = registerWithTab("roasted_beets", () -> new Item(foodItem(FoodValues.ROASTED_BEETS)));
        ENCHANTED_GOLDEN_APPLE_SLICE = registerWithTab("enchanted_golden_apple_slice", () -> new EnchantedConsumableItem(foodItem(FoodValues.ENCHANTED_GOLDEN_APPLE_SLICE)));
        ENCHANTED_GOLDEN_CARROT = registerWithTab("enchanted_golden_carrot", () -> new EnchantedConsumableItem(foodItem(FoodValues.ENCHANTED_GOLDEN_CARROT)));

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
    }
}
