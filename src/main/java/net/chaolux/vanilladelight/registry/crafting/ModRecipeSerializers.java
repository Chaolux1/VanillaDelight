package net.chaolux.vanilladelight.registry.crafting;

import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.common.crafting.ModularCuttingBoardRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipeSerializers {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS;
    public static final Supplier<RecipeSerializer<ModularCuttingBoardRecipe>> MODULAR_CUTTING_BOARD;

    static {
        RECIPE_SERIALIZERS=DeferredRegister.create(Registries.RECIPE_SERIALIZER, VanillaDelight.MOD_ID);
        MODULAR_CUTTING_BOARD=RECIPE_SERIALIZERS.register("modular_cutting_board",ModularCuttingBoardRecipe.Serializer::new);
    }
}
