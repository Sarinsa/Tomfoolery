package com.sarinsa.dumb_ores.common.compat.jei;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;

import java.util.List;

@JeiPlugin
public class TomfooleryJEI implements IModPlugin {

    private static final ResourceLocation ID = Tomfoolery.resourceLoc("dumb_ores_jei");


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

    @SuppressWarnings("unchecked")
    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        IRecipeManager recipeManager = jeiRuntime.getRecipeManager();
        IRecipeCategory<ICraftingRecipe> crafting = (IRecipeCategory<ICraftingRecipe>) recipeManager.getRecipeCategory(VanillaRecipeCategoryUid.CRAFTING);
        List<ICraftingRecipe> recipes = recipeManager.getRecipes(crafting);

        for (IRecipe<?> recipe : recipes) {
            if (recipe.getId().equals(new ResourceLocation("netherite_ingot"))) {
                recipeManager.hideRecipe(recipe, VanillaRecipeCategoryUid.CRAFTING);
                break;
            }
        }
    }
}
