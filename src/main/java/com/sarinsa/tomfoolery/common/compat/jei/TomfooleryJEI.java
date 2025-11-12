package com.sarinsa.tomfoolery.common.compat.jei;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.util.TranslationReferences;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

@JeiPlugin
public class TomfooleryJEI implements IModPlugin {
    
    private static final ResourceLocation ID = Tomfoolery.rl( "dumb_ores_jei" );
    
    
    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return ID;
    }
    
    @Override
    public void registerRecipes( IRecipeRegistration registration ) {
        registration.addIngredientInfo( new ItemStack( TomItems.GRENADE_LAUNCHER.get() ), VanillaTypes.ITEM_STACK, TranslationReferences.LAUNCHER_JEI_DESC );
    }
}
