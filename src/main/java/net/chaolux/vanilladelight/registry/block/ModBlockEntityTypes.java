package net.chaolux.vanilladelight.registry.block;

import net.chaolux.vanilladelight.common.block.ModularContainerFurnitureBlock;
import net.chaolux.vanilladelight.common.block.entity.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import vectorwing.farmersdelight.common.block.entity.CabinetBlockEntity;

import java.util.function.Supplier;


public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final Supplier<BlockEntityType<CommonStoveBlockEntity>> COMMON_STOVE;
    public static final Supplier<BlockEntityType<CommonCuttingBoardBlockEntity>> COMMON_CUTTING_BOARD;
    public static final Supplier<BlockEntityType<CommonCabinetBlockEntity>> COMMON_CABINET;
    public static final Supplier<BlockEntityType<FurnitureBlockEntity>> FURNITURE;
    public static final Supplier<BlockEntityType<ModularCuttingBoardBlockEntity>> MODULAR_CUTTING_BOARD;
    public static final Supplier<BlockEntityType<ModularStoveBlockEntity>> MODULAR_STOVE;

    static {
        TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "vanilladelight");
        COMMON_STOVE = TILES.register("common_stove", () -> BlockEntityType.Builder.of(net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity::new, ModBlocks.DEEPSLATE_BRICKS_STOVE.get(), ModBlocks.END_STONE_BRICKS_STOVE.get(), ModBlocks.MUD_BRICKS_STOVE.get(), ModBlocks.NETHER_BRICKS_STOVE.get(), ModBlocks.STONE_BRICKS_STOVE.get(),ModBlocks.POLISHED_ANDESITE_STOVE.get(),ModBlocks.POLISHED_BASALT_STOVE.get(),ModBlocks.POLISHED_DEEPSLATE_STOVE.get(),ModBlocks.POLISHED_DIORITE_STOVE.get(),ModBlocks.POLISHED_GRANITE_STOVE.get(),ModBlocks.PURPUR_BLOCK_STOVE.get(),ModBlocks.RED_SANDSTONE_STOVE.get(),ModBlocks.SANDSTONE_STOVE.get()).build(null));
        COMMON_CUTTING_BOARD = TILES.register("common_cutting_board", () -> BlockEntityType.Builder.of(CommonCuttingBoardBlockEntity::new, ModBlocks.ACACIA_CUTTING_BOARD.get(), ModBlocks.BAMBOO_CUTTING_BOARD.get(), ModBlocks.BIRCH_CUTTING_BOARD.get(), ModBlocks.CHERRY_CUTTING_BOARD.get(), ModBlocks.CRIMSON_CUTTING_BOARD.get(), ModBlocks.DARK_OAK_CUTTING_BOARD.get(), ModBlocks.JUNGLE_CUTTING_BOARD.get(), ModBlocks.MANGROVE_CUTTING_BOARD.get(), ModBlocks.OAK_CUTTING_BOARD.get(), ModBlocks.WARPED_CUTTING_BOARD.get(), ModBlocks.SPRUCE_CUTTING_BOARD.get()).build(null));
        COMMON_CABINET = TILES.register("common_cabinet", () -> BlockEntityType.Builder.of(CommonCabinetBlockEntity::new, ModBlocks.DEEPSLATE_BRICKS_CABINET.get(), ModBlocks.END_STONE_BRICKS_CABINET.get(), ModBlocks.NETHER_BRICKS_CABINET.get(), ModBlocks.POLISHED_ANDESITE_CABINET.get(), ModBlocks.POLISHED_BASALT_CABINET.get(), ModBlocks.POLISHED_DEEPSLATE_CABINET.get(), ModBlocks.POLISHED_DIORITE_CABINET.get(), ModBlocks.POLISHED_GRANITE_CABINET.get(), ModBlocks.PURPUR_BLOCK_CABINET.get(), ModBlocks.RED_SANDSTONE_CABINET.get(), ModBlocks.SANDSTONE_CABINET.get(), ModBlocks.STONE_CABINET.get()).build(null));
        FURNITURE=TILES.register("furniture", () -> BlockEntityType.Builder.<FurnitureBlockEntity>of((blockPos, blockState) -> blockState.getBlock() instanceof ModularContainerFurnitureBlock ? new FurnitureContainerBlockEntity(blockPos,blockState) : new FurnitureBlockEntity(blockPos,blockState),ModBlocks.getFurnitureBlocks()).build(null));
        MODULAR_CUTTING_BOARD=TILES.register("modular_cutting_board",() -> BlockEntityType.Builder.of(ModularCuttingBoardBlockEntity::new,ModBlocks.MODULAR_CUTTING_BOARD.get()).build(null));
        MODULAR_STOVE=TILES.register("modular_stove",() -> BlockEntityType.Builder.of(ModularStoveBlockEntity::new,ModBlocks.MODULAR_STOVE.get()).build(null));
    }
}
