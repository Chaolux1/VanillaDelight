package net.chaolux.vanilladelight.common.tag;

import net.chaolux.vanilladelight.VanillaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {
    public static final class Items {
        public static final TagKey<Item> MODULAR_CUTTING_BOARD_MATERIALS;
        public static final TagKey<Item> EXCLUDE_MODULAR_CUTTING_BOARD_MATERIALS;

        static {
            MODULAR_CUTTING_BOARD_MATERIALS=create("modular_cutting_board_materials");
            EXCLUDE_MODULAR_CUTTING_BOARD_MATERIALS=create("exclude_modular_cutting_board_materials");
        }

        private static TagKey<Item> create(String string) {
            return ItemTags.create(new ResourceLocation(VanillaDelight.MOD_ID,string));
        }
    }
}
