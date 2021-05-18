package com.sarinsa.dumbores.common.compat.jei;

import com.sarinsa.dumbores.common.core.DumbOres;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class DumbOresJEI implements IModPlugin {

    private static final ResourceLocation ID = DumbOres.resourceLoc("dumb_ores_jei");


    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
    }

    @Override
    public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration registration) {
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
    }
}
