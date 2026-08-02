package net.chaolux.vanilladelight.registry.block;

import net.chaolux.vanilladelight.common.block.*;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefinitions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.*;
import net.chaolux.vanilladelight.registry.item.ModItems;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    private static final List<RegistryObject<Block>> FURNITURE_BLOCKS=new ArrayList<>();

    public static final RegistryObject<Block> CARROT_CAKE;
    public static final RegistryObject<Block> HONEY_CAKE;
    public static final RegistryObject<Block> MELON_PIE;
    public static final RegistryObject<Block> CHORUS_PIE;
    public static final RegistryObject<Block> MILKY_PUMPKIN_BLOCK;

    public static final RegistryObject<Block> DEEPSLATE_BRICKS_STOVE;
    public static final RegistryObject<Block> END_STONE_BRICKS_STOVE;
    public static final RegistryObject<Block> MUD_BRICKS_STOVE;
    public static final RegistryObject<Block> NETHER_BRICKS_STOVE;
    public static final RegistryObject<Block> STONE_BRICKS_STOVE;
    public static final RegistryObject<Block> POLISHED_ANDESITE_STOVE;
    public static final RegistryObject<Block> POLISHED_BASALT_STOVE;
    public static final RegistryObject<Block> POLISHED_DEEPSLATE_STOVE;
    public static final RegistryObject<Block> POLISHED_DIORITE_STOVE;
    public static final RegistryObject<Block> POLISHED_GRANITE_STOVE;
    public static final RegistryObject<Block> PURPUR_BLOCK_STOVE;
    public static final RegistryObject<Block> RED_SANDSTONE_STOVE;
    public static final RegistryObject<Block> SANDSTONE_STOVE;

    public static final RegistryObject<Block> ACACIA_CUTTING_BOARD;
    public static final RegistryObject<Block> BAMBOO_CUTTING_BOARD;
    public static final RegistryObject<Block> BIRCH_CUTTING_BOARD;
    public static final RegistryObject<Block> CHERRY_CUTTING_BOARD;
    public static final RegistryObject<Block> CRIMSON_CUTTING_BOARD;
    public static final RegistryObject<Block> DARK_OAK_CUTTING_BOARD;
    public static final RegistryObject<Block> JUNGLE_CUTTING_BOARD;
    public static final RegistryObject<Block> MANGROVE_CUTTING_BOARD;
    public static final RegistryObject<Block> OAK_CUTTING_BOARD;
    public static final RegistryObject<Block> WARPED_CUTTING_BOARD;
    public static final RegistryObject<Block> SPRUCE_CUTTING_BOARD;

    public static final RegistryObject<Block> DEEPSLATE_BRICKS_CABINET;
    public static final RegistryObject<Block> END_STONE_BRICKS_CABINET;
    public static final RegistryObject<Block> NETHER_BRICKS_CABINET;
    public static final RegistryObject<Block> POLISHED_ANDESITE_CABINET;
    public static final RegistryObject<Block> POLISHED_BASALT_CABINET;
    public static final RegistryObject<Block> POLISHED_DEEPSLATE_CABINET;
    public static final RegistryObject<Block> POLISHED_DIORITE_CABINET;
    public static final RegistryObject<Block> POLISHED_GRANITE_CABINET;
    public static final RegistryObject<Block> PURPUR_BLOCK_CABINET;
    public static final RegistryObject<Block> RED_SANDSTONE_CABINET;
    public static final RegistryObject<Block> SANDSTONE_CABINET;
    public static final RegistryObject<Block> STONE_CABINET;

    public static final RegistryObject<Block> MODULAR_CABINET;
    public static final RegistryObject<Block> PATTERNED_CABINET;
    public static final RegistryObject<Block> MODULAR_CUTTING_BOARD;


    public static Block[] getFurnitureBlocks() {
        return FURNITURE_BLOCKS.stream().map(RegistryObject::get).toArray(Block[]::new);
    }

    public static RegistryObject<Block> resisterFurniture(String string, Supplier<? extends Block> supplier) {
        RegistryObject<Block> blockRegistryObject=BLOCKS.register(string,supplier);
        FURNITURE_BLOCKS.add(blockRegistryObject);
        return blockRegistryObject;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int lightValue) {
        return (state) -> (Boolean)state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "vanilladelight");

        CARROT_CAKE = BLOCKS.register("carrot_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        HONEY_CAKE = BLOCKS.register("honey_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        MELON_PIE = BLOCKS.register("melon_pie", () -> new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), ModItems.MELON_PIE_SLICE));
        CHORUS_PIE = BLOCKS.register("chorus_pie", () -> new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), ModItems.CHORUS_PIE_SLICE));

        MILKY_PUMPKIN_BLOCK = BLOCKS.register("milky_pumpkin_block", () -> new FeastBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN), ModItems.MILKY_PUMPKIN, false));

        DEEPSLATE_BRICKS_STOVE = BLOCKS.register("deepslate_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).lightLevel(litBlockEmission(13))));
        END_STONE_BRICKS_STOVE = BLOCKS.register("end_stone_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS).lightLevel(litBlockEmission(13))));
        MUD_BRICKS_STOVE = BLOCKS.register("mud_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).lightLevel(litBlockEmission(13))));
        NETHER_BRICKS_STOVE = BLOCKS.register("nether_bricks_stove", () -> new SoulCommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_BRICKS).lightLevel(litBlockEmission(13))));
        STONE_BRICKS_STOVE = BLOCKS.register("stone_bricks_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).lightLevel(litBlockEmission(13))));
        POLISHED_ANDESITE_STOVE = BLOCKS.register("polished_andesite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE).lightLevel(litBlockEmission(13))));
        POLISHED_BASALT_STOVE = BLOCKS.register("polished_basalt_stove", () -> new SoulCommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_BASALT).lightLevel(litBlockEmission(13))));
        POLISHED_DEEPSLATE_STOVE = BLOCKS.register("polished_deepslate_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_DEEPSLATE).lightLevel(litBlockEmission(13))));
        POLISHED_DIORITE_STOVE = BLOCKS.register("polished_diorite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_DIORITE).lightLevel(litBlockEmission(13))));
        POLISHED_GRANITE_STOVE = BLOCKS.register("polished_granite_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_GRANITE).lightLevel(litBlockEmission(13))));
        PURPUR_BLOCK_STOVE = BLOCKS.register("purpur_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.PURPUR_BLOCK).lightLevel(litBlockEmission(13))));
        RED_SANDSTONE_STOVE = BLOCKS.register("red_sandstone_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).lightLevel(litBlockEmission(13))));
        SANDSTONE_STOVE = BLOCKS.register("sandstone_stove", () -> new CommonStoveBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).lightLevel(litBlockEmission(13))));

        ACACIA_CUTTING_BOARD = BLOCKS.register("acacia_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.ACACIA_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        BAMBOO_CUTTING_BOARD = BLOCKS.register("bamboo_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.BAMBOO_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        BIRCH_CUTTING_BOARD = BLOCKS.register("birch_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.BIRCH_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        CHERRY_CUTTING_BOARD = BLOCKS.register("cherry_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.CHERRY_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        CRIMSON_CUTTING_BOARD = BLOCKS.register("crimson_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.CRIMSON_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        DARK_OAK_CUTTING_BOARD = BLOCKS.register("dark_oak_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.DARK_OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        JUNGLE_CUTTING_BOARD = BLOCKS.register("jungle_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.JUNGLE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        MANGROVE_CUTTING_BOARD = BLOCKS.register("mangrove_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.MANGROVE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        OAK_CUTTING_BOARD = BLOCKS.register("oak_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        WARPED_CUTTING_BOARD = BLOCKS.register("warped_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.WARPED_PLANKS).strength(2.0f).sound(SoundType.WOOD)));
        SPRUCE_CUTTING_BOARD = BLOCKS.register("spruce_cutting_board", () -> new CommonCuttingBoard(BlockBehaviour.Properties.copy(Blocks.SPRUCE_PLANKS).strength(2.0f).sound(SoundType.WOOD)));

        DEEPSLATE_BRICKS_CABINET = BLOCKS.register("deepslate_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        END_STONE_BRICKS_CABINET = BLOCKS.register("end_stone_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        NETHER_BRICKS_CABINET = BLOCKS.register("nether_bricks_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        POLISHED_ANDESITE_CABINET = BLOCKS.register("polished_andesite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        POLISHED_BASALT_CABINET = BLOCKS.register("polished_basalt_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        POLISHED_DEEPSLATE_CABINET = BLOCKS.register("polished_deepslate_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        POLISHED_DIORITE_CABINET = BLOCKS.register("polished_diorite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        POLISHED_GRANITE_CABINET = BLOCKS.register("polished_granite_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        PURPUR_BLOCK_CABINET = BLOCKS.register("purpur_block_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        RED_SANDSTONE_CABINET = BLOCKS.register("red_sandstone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        SANDSTONE_CABINET = BLOCKS.register("sandstone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));
        STONE_CABINET = BLOCKS.register("stone_cabinet", () -> new CommonCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL)));

        MODULAR_CABINET=resisterFurniture("modular_cabinet", () -> new ModularCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL), FurnitureDefinitions.MODULAR_CABINET));
        PATTERNED_CABINET=resisterFurniture("patterned_cabinet", () -> new PatternedCabinetBlock(BlockBehaviour.Properties.copy(Blocks.BARREL),FurnitureDefinitions.PATTERNED_CABINET));
        MODULAR_CUTTING_BOARD=BLOCKS.register("modular_cutting_board",() -> new ModularCuttingBoardBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS).strength(2.0f).sound(SoundType.WOOD).noOcclusion(),FurnitureDefinitions.MODULAR_CUTTING_BOARD));
    }
}
