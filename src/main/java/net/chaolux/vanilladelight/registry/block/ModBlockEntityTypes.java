package net.chaolux.vanilladelight.registry.block;

import net.chaolux.vanilladelight.common.block.entity.CommonCuttingBoardBlockEntity;
import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final RegistryObject<BlockEntityType<CommonStoveBlockEntity>> COMMON_STOVE;
    public static final RegistryObject<BlockEntityType<CommonCuttingBoardBlockEntity>> COMMON_CUTTING_BOARD;

    static {
        TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "vanilladelight");
        COMMON_STOVE = TILES.register("common_stove", () -> BlockEntityType.Builder.of(CommonStoveBlockEntity::new, ModBlocks.DEEPSLATE_BRICKS_STOVE.get(), ModBlocks.END_STONE_BRICKS_STOVE.get(), ModBlocks.MUD_BRICKS_STOVE.get(), ModBlocks.NETHER_BRICKS_STOVE.get(), ModBlocks.STONE_BRICKS_STOVE.get(),ModBlocks.POLISHED_ANDESITE_STOVE.get(),ModBlocks.POLISHED_BASALT_STOVE.get(),ModBlocks.POLISHED_DEEPSLATE_STOVE.get(),ModBlocks.POLISHED_DIORITE_STOVE.get(),ModBlocks.POLISHED_GRANITE_STOVE.get(),ModBlocks.PURPUR_BLOCK_STOVE.get(),ModBlocks.RED_SANDSTONE_STOVE.get(),ModBlocks.SANDSTONE_STOVE.get()).build(null));
        COMMON_CUTTING_BOARD = TILES.register("common_cutting_board", () -> BlockEntityType.Builder.of(CommonCuttingBoardBlockEntity::new, ModBlocks.ACACIA_CUTTING_BOARD.get(), ModBlocks.BAMBOO_CUTTING_BOARD.get(), ModBlocks.BIRCH_CUTTING_BOARD.get(), ModBlocks.CHERRY_CUTTING_BOARD.get(), ModBlocks.CRIMSON_CUTTING_BOARD.get(), ModBlocks.DARK_OAK_CUTTING_BOARD.get(), ModBlocks.JUNGLE_CUTTING_BOARD.get(), ModBlocks.MANGROVE_CUTTING_BOARD.get(), ModBlocks.OAK_CUTTING_BOARD.get(), ModBlocks.WARPED_CUTTING_BOARD.get()).build(null));
    }
}
