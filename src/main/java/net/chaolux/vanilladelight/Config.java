package net.chaolux.vanilladelight;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = VanillaDelight.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.BooleanValue ALLOW_NON_FULL_BLOCK_MATERIAL;
    public static final ModConfigSpec.BooleanValue FILL_TRANSPARENT_MATERIAL_GAPS;
    static final ModConfigSpec SPEC;

    static {
        BUILDER.push("furniture");
        ALLOW_NON_FULL_BLOCK_MATERIAL=BUILDER.comment("Allows slabs, stairs, rods, grindstones, fences and other non full blocks to be use as furniture materials.").define("allowNonFullBlockMaterial",true);
        FILL_TRANSPARENT_MATERIAL_GAPS=BUILDER.comment("Adds opaque underlay below textures of non full blocks. Glass blocks are exclude.").define("fillTransparentMaterialGaps",true);
        BUILDER.pop();
        SPEC=BUILDER.build();
    }

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {

    }
}
