package net.chaolux.vanilladelight.registry.block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import net.chaolux.vanilladelight.common.block.*;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefinitions;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.level.block.state.BlockBehaviour;
import vectorwing.farmersdelight.common.block.CabinetBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    private static final List<Supplier<Block>> FURNITURE_BLOCKS=new ArrayList<>();

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

    public static final Supplier<Block> ACACIA_CUTTING_BOARD;
    public static final Supplier<Block> BAMBOO_CUTTING_BOARD;
    public static final Supplier<Block> BIRCH_CUTTING_BOARD;
    public static final Supplier<Block> CHERRY_CUTTING_BOARD;
    public static final Supplier<Block> CRIMSON_CUTTING_BOARD;
    public static final Supplier<Block> DARK_OAK_CUTTING_BOARD;
    public static final Supplier<Block> JUNGLE_CUTTING_BOARD;
    public static final Supplier<Block> MANGROVE_CUTTING_BOARD;
    public static final Supplier<Block> OAK_CUTTING_BOARD;
    public static final Supplier<Block> WARPED_CUTTING_BOARD;
    public static final Supplier<Block> SPRUCE_CUTTING_BOARD;

    public static final Supplier<Block> DEEPSLATE_BRICKS_CABINET;
    public static final Supplier<Block> END_STONE_BRICKS_CABINET;
    public static final Supplier<Block> NETHER_BRICKS_CABINET;
    public static final Supplier<Block> POLISHED_ANDESITE_CABINET;
    public static final Supplier<Block> POLISHED_BASALT_CABINET;
    public static final Supplier<Block> POLISHED_DEEPSLATE_CABINET;
    public static final Supplier<Block> POLISHED_DIORITE_CABINET;
    public static final Supplier<Block> POLISHED_GRANITE_CABINET;
    public static final Supplier<Block> PURPUR_BLOCK_CABINET;
    public static final Supplier<Block> RED_SANDSTONE_CABINET;
    public static final Supplier<Block> SANDSTONE_CABINET;
    public static final Supplier<Block> STONE_CABINET;

    public static final Supplier<Block> MODULAR_CABINET;
    public static final Supplier<Block> PATTERNED_CABINET;
    public static final Supplier<Block> MODULAR_CUTTING_BOARD;
    public static final Supplier<Block> MODULAR_STOVE;


    public static Block[] getFurnitureBlocks() {
        return FURNITURE_BLOCKS.stream().map(Supplier::get).toArray(Block[]::new);
    }

    public static Supplier<Block> resisterFurniture(String string, Supplier<? extends Block> supplier) {
        Supplier<Block> blockRegistryObject=BLOCKS.register(string,supplier);
        FURNITURE_BLOCKS.add(blockRegistryObject);
        return blockRegistryObject;
    }

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

        ACACIA_CUTTING_BOARD = BLOCKS.register("acacia_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        BAMBOO_CUTTING_BOARD = BLOCKS.register("bamboo_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        BIRCH_CUTTING_BOARD = BLOCKS.register("birch_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        CHERRY_CUTTING_BOARD = BLOCKS.register("cherry_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        CRIMSON_CUTTING_BOARD = BLOCKS.register("crimson_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.CRIMSON_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        DARK_OAK_CUTTING_BOARD = BLOCKS.register("dark_oak_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        JUNGLE_CUTTING_BOARD = BLOCKS.register("jungle_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        MANGROVE_CUTTING_BOARD = BLOCKS.register("mangrove_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        OAK_CUTTING_BOARD = BLOCKS.register("oak_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        WARPED_CUTTING_BOARD = BLOCKS.register("warped_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.WARPED_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        SPRUCE_CUTTING_BOARD = BLOCKS.register("spruce_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));

        DEEPSLATE_BRICKS_CABINET = BLOCKS.register("deepslate_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        END_STONE_BRICKS_CABINET = BLOCKS.register("end_stone_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        NETHER_BRICKS_CABINET = BLOCKS.register("nether_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        POLISHED_ANDESITE_CABINET = BLOCKS.register("polished_andesite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        POLISHED_BASALT_CABINET = BLOCKS.register("polished_basalt_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        POLISHED_DEEPSLATE_CABINET = BLOCKS.register("polished_deepslate_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        POLISHED_DIORITE_CABINET = BLOCKS.register("polished_diorite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        POLISHED_GRANITE_CABINET = BLOCKS.register("polished_granite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        PURPUR_BLOCK_CABINET = BLOCKS.register("purpur_block_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        RED_SANDSTONE_CABINET = BLOCKS.register("red_sandstone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        SANDSTONE_CABINET = BLOCKS.register("sandstone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));
        STONE_CABINET = BLOCKS.register("stone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL)));

        MODULAR_CABINET=resisterFurniture("modular_cabinet", () -> new ModularCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL), FurnitureDefinitions.MODULAR_CABINET));
        PATTERNED_CABINET=resisterFurniture("patterned_cabinet", () -> new PatternedCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BARREL),FurnitureDefinitions.PATTERNED_CABINET));
        MODULAR_CUTTING_BOARD=BLOCKS.register("modular_cutting_board",() -> new ModularCuttingBoardBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD).noOcclusion(),FurnitureDefinitions.MODULAR_CUTTING_BOARD));
        MODULAR_STOVE=BLOCKS.register("modular_stove",() -> new ModularStoveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).lightLevel(litBlockEmission(13)),FurnitureDefinitions.MODULAR_STOVE));
    }
}
