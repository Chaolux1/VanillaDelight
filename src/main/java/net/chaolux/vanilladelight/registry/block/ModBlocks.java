package net.chaolux.vanilladelight.registry.block;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import net.chaolux.vanilladelight.common.block.CommonStoveBlock;
import net.chaolux.vanilladelight.common.block.SoulCommonStoveBlock;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    public static final Supplier<Block> CARROT_CAKE;
    public static final Supplier<Block> HONEY_CAKE;
    public static final Supplier<Block> MELON_PIE;
    public static final Supplier<Block> CHORUS_PIE;
    public static final Supplier<Block> MILKY_PUMPKIN_BLOCK;

    public static final Supplier<Block> DEEPSLATE_BRICKS_STOVE;
    public static final Supplier<Block> END_STONE_BRICKS_STOVE;
    public static final Supplier<Block> MUD_BRICKS_STOVE;
    public static final Supplier<Block> NETHER_BRICKS_STOVE;
    public static final Supplier<Block> STONE_BRICKS_STOVE;
    public static final Supplier<Block> POLISHED_ANDESITE_STOVE;
    public static final Supplier<Block> POLISHED_BASALT_STOVE;
    public static final Supplier<Block> POLISHED_DEEPSLATE_STOVE;
    public static final Supplier<Block> POLISHED_DIORITE_STOVE;
    public static final Supplier<Block> POLISHED_GRANITE_STOVE;
    public static final Supplier<Block> PURPUR_BLOCK_STOVE;
    public static final Supplier<Block> RED_SANDSTONE_STOVE;
    public static final Supplier<Block> SANDSTONE_STOVE;

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    static {
        BLOCKS = DeferredRegister.create(Registries.BLOCK, "vanilladelight");

        CARROT_CAKE = BLOCKS.register("carrot_cake", () -> new CakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
        HONEY_CAKE = BLOCKS.register("honey_cake", () -> new CakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
        MELON_PIE = BLOCKS.register("melon_pie", () -> new PieBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), ModItems.MELON_PIE_SLICE));
        CHORUS_PIE = BLOCKS.register("chorus_pie", () -> new PieBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), ModItems.CHORUS_PIE_SLICE));

        MILKY_PUMPKIN_BLOCK = BLOCKS.register("milky_pumpkin_block", () -> new FeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN), ModItems.MILKY_PUMPKIN, false));

        DEEPSLATE_BRICKS_STOVE = BLOCKS.register("deepslate_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE_BRICKS).lightLevel(litBlockEmission(13))));
        END_STONE_BRICKS_STOVE = BLOCKS.register("end_stone_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.END_STONE_BRICKS).lightLevel(litBlockEmission(13))));
        MUD_BRICKS_STOVE = BLOCKS.register("mud_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MUD_BRICKS).lightLevel(litBlockEmission(13))));
        NETHER_BRICKS_STOVE = BLOCKS.register("nether_bricks_stove", () -> new SoulCommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHER_BRICKS).lightLevel(litBlockEmission(13))));
        STONE_BRICKS_STOVE = BLOCKS.register("stone_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).lightLevel(litBlockEmission(13))));
        POLISHED_ANDESITE_STOVE = BLOCKS.register("polished_andesite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE).lightLevel(litBlockEmission(13))));
        POLISHED_BASALT_STOVE = BLOCKS.register("polished_basalt_stove", () -> new SoulCommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BASALT).lightLevel(litBlockEmission(13))));
        POLISHED_DEEPSLATE_STOVE = BLOCKS.register("polished_deepslate_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DEEPSLATE).lightLevel(litBlockEmission(13))));
        POLISHED_DIORITE_STOVE = BLOCKS.register("polished_diorite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE).lightLevel(litBlockEmission(13))));
        POLISHED_GRANITE_STOVE = BLOCKS.register("polished_granite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE).lightLevel(litBlockEmission(13))));
        PURPUR_BLOCK_STOVE = BLOCKS.register("purpur_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK).lightLevel(litBlockEmission(13))));
        RED_SANDSTONE_STOVE = BLOCKS.register("red_sandstone_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE).lightLevel(litBlockEmission(13))));
        SANDSTONE_STOVE = BLOCKS.register("sandstone_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE).lightLevel(litBlockEmission(13))));
    }
}
