package net.chaolux.vanilladelight.registry.block;

import java.util.function.Supplier;

import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
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


    static {
        BLOCKS = DeferredRegister.create(Registries.BLOCK, "vanilladelight");

        CARROT_CAKE = BLOCKS.register("carrot_cake", () -> new CakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
        HONEY_CAKE = BLOCKS.register("honey_cake", () -> new CakeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE)));
        MELON_PIE = BLOCKS.register("melon_pie", () -> new PieBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), ModItems.MELON_PIE_SLICE));
        CHORUS_PIE = BLOCKS.register("chorus_pie", () -> new PieBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CAKE), ModItems.CHORUS_PIE_SLICE));

        MILKY_PUMPKIN_BLOCK = BLOCKS.register("milky_pumpkin_block", () -> new FeastBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN), ModItems.MILKY_PUMPKIN, false));

    }
}
