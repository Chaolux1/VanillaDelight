package net.chaolux.vanilladelight.registry.item;

import net.chaolux.vanilladelight.registry.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.item.ConsumableItem;

import java.util.function.Supplier;

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
        GLOW_TROPICAL_FISH_STEW = registerWithTab("glow_tropical_fish_stew", () -> new ConsumableItem(bowlFoodItem(FoodValues.GLOW_TROPICAL_FISH_STEW)));
        GRILLED_BEETROOT = registerWithTab("grilled_beetroot", () -> new ConsumableItem(bowlFoodItem(FoodValues.GRILLED_BEETROOT)));
        MILKY_PUMPKIN = registerWithTab("milky_pumpkin", () -> new ConsumableItem(bowlFoodItem(FoodValues.MILKY_PUMPKIN), true));

        CARAMEL_BOTTLE = registerWithTab("caramel_bottle", () -> new ConsumableItem(drinkItem(FoodValues.CARAMEL_BOTTLE)));
        GLOW_BERRIES_JAM = registerWithTab("glow_berries_jam", () -> new ConsumableItem(drinkItem(FoodValues.GLOW_BERRIES_JAM)));
        GOLDEN_APPLE_CIDER = registerWithTab("golden_apple_cider", () -> new ConsumableItem(drinkItem(FoodValues.GOLDEN_APPLE_CIDER), true));
        SWEET_BERRIES_JAM = registerWithTab("sweet_berries_jam", () -> new ConsumableItem(drinkItem(FoodValues.SWEET_BERRIES_JAM)));
        SWEET_BERRY_CUSTARD = registerWithTab("sweet_berry_custard", () -> new ConsumableItem(drinkItem(FoodValues.SWEET_BERRY_CUSTARD), true));

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

        CARROT_CAKE = registerWithTab("carrot_cake", () -> new BlockItem((Block)ModBlocks.CARROT_CAKE.get(), basicItem()));
        HONEY_CAKE = registerWithTab("honey_cake", () -> new BlockItem((Block)ModBlocks.HONEY_CAKE.get(), basicItem()));
        MELON_PIE = registerWithTab("melon_pie", () -> new BlockItem((Block)ModBlocks.MELON_PIE.get(), basicItem()));
        CHORUS_PIE = registerWithTab("chorus_pie", () -> new BlockItem((Block)ModBlocks.CHORUS_PIE.get(), basicItem()));
        MILKY_PUMPKIN_BLOCK = registerWithTab("milky_pumpkin_block", () -> new BlockItem((Block) ModBlocks.MILKY_PUMPKIN_BLOCK.get(), basicItem().stacksTo(1)));

    }
}
