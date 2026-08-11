package net.chaolux.vanilladelight.common.loot;

import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.LootTableLoadEvent;

import java.util.function.Supplier;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.GAME)
public class VanillaDelightLootEvents {
    private static final float PATTERN=0.25f;

    @SubscribeEvent
    public static void onLootTable(LootTableLoadEvent event) {
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","chests/simple_dungeon"),"vanilladelight_spiral_pattern", ModItems.SPIRAL_CUTTING_BOARD_PATTERN,PATTERN);
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","chests/abandoned_mineshaft"),"vanilladelight_lines_pattern", ModItems.LINES_CUTTING_BOARD_PATTERN,PATTERN);
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","chests/desert_pyramid"),"vanilladelight_tiles_pattern", ModItems.TILES_CUTTING_BOARD_PATTERN,PATTERN);
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","chests/stronghold_library"),"vanilladelight_diamond_pattern", ModItems.DIAMOND_CUTTING_BOARD_PATTERN,PATTERN);
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","chests/woodland_mansion"),"vanilladelight_frame_pattern", ModItems.FRAME_CUTTING_BOARD_PATTERN,PATTERN);
        addItem(event,ResourceLocation.fromNamespaceAndPath("minecraft","entities/panda"),"vanilladelight_music_disc", ModItems.MUSIC_DISC_ANEW,0.025f);
    }

    private static void addItem(LootTableLoadEvent lootTableLoadEvent, ResourceLocation resourceLocation, String string, Supplier<Item> itemRegistryObject, float value) {
        if(!lootTableLoadEvent.getName().equals(resourceLocation)) return;
        LootPool lootPool=LootPool.lootPool().name(string).setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(itemRegistryObject.get()).when(LootItemRandomChanceCondition.randomChance(value))).build();
        lootTableLoadEvent.getTable().addPool(lootPool);
    }
}
