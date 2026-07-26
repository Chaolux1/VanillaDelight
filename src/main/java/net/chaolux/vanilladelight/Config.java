package net.chaolux.vanilladelight;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Mod.EventBusSubscriber(modid = VanillaDelight.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue ALLOW_NON_FULL_BLOCK_MATERIAL;
    public static final ForgeConfigSpec.BooleanValue FILL_TRANSPARENT_MATERIAL_GAPS;
    static final ForgeConfigSpec SPEC;

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
