package net.chaolux.vanilladelight.registry.crafting;

import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.common.crafting.ModularCuttingBoardRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS=DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, VanillaDelight.MOD_ID);
    public static final RegistryObject<RecipeSerializer<ModularCuttingBoardRecipe>> MODULAR_CUTTING_BOARD;

    static {
        MODULAR_CUTTING_BOARD=RECIPE_SERIALIZERS.register("modular_cutting_board",ModularCuttingBoardRecipe.Serializer::new);
    }
}
