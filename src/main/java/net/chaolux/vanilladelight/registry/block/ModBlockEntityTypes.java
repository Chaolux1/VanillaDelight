package net.chaolux.vanilladelight.registry.block;

import net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntityTypes {
    public static final DeferredRegister<BlockEntityType<?>> TILES;
    public static final RegistryObject<BlockEntityType<CommonStoveBlockEntity>> COMMON_STOVE;

    static {
        TILES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "vanilladelight");
        COMMON_STOVE = TILES.register("common_stove", () -> BlockEntityType.Builder.of(net.chaolux.vanilladelight.common.block.entity.CommonStoveBlockEntity::new, ModBlocks.DEEPSLATE_BRICKS_STOVE.get(), ModBlocks.END_STONE_BRICKS_STOVE.get(), ModBlocks.MUD_BRICKS_STOVE.get(), ModBlocks.NETHER_BRICKS_STOVE.get(), ModBlocks.STONE_BRICKS_STOVE.get(),ModBlocks.POLISHED_ANDESITE_STOVE.get(),ModBlocks.POLISHED_BASALT_STOVE.get(),ModBlocks.POLISHED_DEEPSLATE_STOVE.get(),ModBlocks.POLISHED_DIORITE_STOVE.get(),ModBlocks.POLISHED_GRANITE_STOVE.get(),ModBlocks.PURPUR_BLOCK_STOVE.get(),ModBlocks.RED_SANDSTONE_STOVE.get(),ModBlocks.SANDSTONE_STOVE.get()).build(null));
    }
}
