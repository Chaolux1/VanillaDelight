package net.chaolux.vanilladelight.registry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.CookingPotBlock;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import net.chaolux.vanilladelight.registry.item.ModItems;
import vectorwing.farmersdelight.common.block.StoveBlock;

import java.util.function.ToIntFunction;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

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

        DEEPSLATE_BRICKS_STOVE = BLOCKS.register("deepslate_bricks_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE_BRICKS).lightLevel(litBlockEmission(13))));
        END_STONE_BRICKS_STOVE = BLOCKS.register("end_stone_bricks_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE_BRICKS).lightLevel(litBlockEmission(13))));
        MUD_BRICKS_STOVE = BLOCKS.register("mud_bricks_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.MUD_BRICKS).lightLevel(litBlockEmission(13))));
        NETHER_BRICKS_STOVE = BLOCKS.register("nether_bricks_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_BRICKS).lightLevel(litBlockEmission(13))));
        STONE_BRICKS_STOVE = BLOCKS.register("stone_bricks_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.STONE_BRICKS).lightLevel(litBlockEmission(13))));
        POLISHED_ANDESITE_STOVE = BLOCKS.register("polished_andesite_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_ANDESITE).lightLevel(litBlockEmission(13))));
        POLISHED_BASALT_STOVE = BLOCKS.register("polished_basalt_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_BASALT).lightLevel(litBlockEmission(13))));
        POLISHED_DEEPSLATE_STOVE = BLOCKS.register("polished_deepslate_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_DEEPSLATE).lightLevel(litBlockEmission(13))));
        POLISHED_DIORITE_STOVE = BLOCKS.register("polished_diorite_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_DIORITE).lightLevel(litBlockEmission(13))));
        POLISHED_GRANITE_STOVE = BLOCKS.register("polished_granite_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.POLISHED_GRANITE).lightLevel(litBlockEmission(13))));
        PURPUR_BLOCK_STOVE = BLOCKS.register("purpur_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.PURPUR_BLOCK).lightLevel(litBlockEmission(13))));
        RED_SANDSTONE_STOVE = BLOCKS.register("red_sandstone_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.RED_SANDSTONE).lightLevel(litBlockEmission(13))));
        SANDSTONE_STOVE = BLOCKS.register("sandstone_stove", () -> new StoveBlock(BlockBehaviour.Properties.copy(Blocks.SANDSTONE).lightLevel(litBlockEmission(13))));
    }
}
