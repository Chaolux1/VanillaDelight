package net.chaolux.vanilladelight.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.common.crafting.ModularCuttingBoardRecipe;
import net.chaolux.vanilladelight.integration.jei.category.ModularCuttingBoardRecipeCategory;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIPlugin implements IModPlugin {
    private static final ResourceLocation ID=new ResourceLocation(VanillaDelight.MOD_ID,"jei_plugin");

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration iSubtypeRegistration) {
        iSubtypeRegistration.useNbtForSubtypes(ModItems.MODULAR_CUTTING_BOARD.get());
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration iVanillaCategoryExtensionRegistration) {
        iVanillaCategoryExtensionRegistration.getCraftingCategory().addCategoryExtension(ModularCuttingBoardRecipe.class, ModularCuttingBoardRecipeCategory::new);
    }
}
