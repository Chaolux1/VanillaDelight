package net.chaolux.vanilladelight.registry.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import vectorwing.farmersdelight.common.block.FeastBlock;
import vectorwing.farmersdelight.common.block.PieBlock;
import net.chaolux.vanilladelight.registry.item.ModItems;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS;

    public static final RegistryObject<Block> CARROT_CAKE;
    public static final RegistryObject<Block> HONEY_CAKE;
    public static final RegistryObject<Block> MELON_PIE;
    public static final RegistryObject<Block> CHORUS_PIE;
    public static final RegistryObject<Block> MILKY_PUMPKIN_BLOCK;


    static {
        BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "vanilladelight");

        CARROT_CAKE = BLOCKS.register("carrot_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        HONEY_CAKE = BLOCKS.register("honey_cake", () -> new CakeBlock(BlockBehaviour.Properties.copy(Blocks.CAKE)));
        MELON_PIE = BLOCKS.register("melon_pie", () -> new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), ModItems.MELON_PIE_SLICE));
        CHORUS_PIE = BLOCKS.register("chorus_pie", () -> new PieBlock(BlockBehaviour.Properties.copy(Blocks.CAKE), ModItems.CHORUS_PIE_SLICE));

        MILKY_PUMPKIN_BLOCK = BLOCKS.register("milky_pumpkin_block", () -> new FeastBlock(BlockBehaviour.Properties.copy(Blocks.PUMPKIN), ModItems.MILKY_PUMPKIN, false));

    }
}
