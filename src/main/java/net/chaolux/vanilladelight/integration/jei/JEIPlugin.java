package net.chaolux.vanilladelight.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.registration.IVanillaCategoryExtensionRegistration;
import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.common.crafting.ModularCuttingBoardRecipe;
import net.chaolux.vanilladelight.integration.jei.category.ModularCuttingBoardRecipeCategory;
import net.chaolux.vanilladelight.registry.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

    @Override
    public void registerRecipes(IRecipeRegistration iRecipeRegistration) {
        addInfo(iRecipeRegistration,ModItems.SPIRAL_CUTTING_BOARD_PATTERN.get(),"jei.vanilladelight.pattern.spiral");
        addInfo(iRecipeRegistration,ModItems.LINES_CUTTING_BOARD_PATTERN.get(),"jei.vanilladelight.pattern.lines");
        addInfo(iRecipeRegistration,ModItems.TILES_CUTTING_BOARD_PATTERN.get(),"jei.vanilladelight.pattern.tiles");
        addInfo(iRecipeRegistration,ModItems.DIAMOND_CUTTING_BOARD_PATTERN.get(),"jei.vanilladelight.pattern.diamond");
        addInfo(iRecipeRegistration,ModItems.FRAME_CUTTING_BOARD_PATTERN.get(),"jei.vanilladelight.pattern.frame");
        addInfo(iRecipeRegistration,ModItems.MUSIC_DISC_ANEW.get(),"jei.vanilladelight.music_disc.anew");
        addInfo(iRecipeRegistration,ModItems.MODULAR_CABINET.get(),"jei.vanilladelight.modular_cabinet.material","jei.vanilladelight.modular_cabinet.style","jei.vanilladelight.furniture.wax");
        addInfo(iRecipeRegistration,ModItems.PATTERNED_CABINET.get(),"jei.vanilladelight.patterned_cabinet.material","jei.vanilladelight.patterned_cabinet.style","jei.vanilladelight.furniture.wax");
        addInfo(iRecipeRegistration,ModItems.MODULAR_CUTTING_BOARD.get(),"jei.vanilladelight.modular_cutting_board.pattern");
        addInfo(iRecipeRegistration,ModItems.MODULAR_STOVE.get(),"jei.vanilladelight.modular_stove.material","jei.vanilladelight.modular_stove.style","jei.vanilladelight.furniture.wax");

    }

    private static void addInfo(IRecipeRegistration iRecipeRegistration, Item item,String... string) {
        Component[] components=new Component[string.length];
        for(int index=0;index < string.length;index++) {
            components[index]=Component.translatable(string[index]);
        }
        iRecipeRegistration.addIngredientInfo(new ItemStack(item), VanillaTypes.ITEM_STACK, components);
    }
}
