package com.sarinsa.dumb_ores.common.core.registry;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TomPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTION_TYPES, Tomfoolery.MODID);

    public static final RegistryObject<Potion> CACTUS_ATTRACTION = registerPotion("cactus_attraction", TomEffects.CACTUS_ATTRACTION, 600, 0);
    public static final RegistryObject<Potion> CACTUS_ATTRACTION_LONG = registerPotion("cactus_attraction_long", TomEffects.CACTUS_ATTRACTION, 1200, 0);
    public static final RegistryObject<Potion> CACTUS_ATTRACTION_STRONG = registerPotion("cactus_attraction_strong", TomEffects.CACTUS_ATTRACTION, 400, 1);

    public static void registerBrewingRecipes() {
        registerBrewingRecipe(CACTUS_ATTRACTION.get(), Potions.AWKWARD, Ingredient.of(Items.GREEN_DYE));
        registerBrewingRecipe(CACTUS_ATTRACTION_LONG.get(), CACTUS_ATTRACTION.get(), Ingredient.of(Tags.Items.DUSTS_GLOWSTONE));
        registerBrewingRecipe(CACTUS_ATTRACTION_STRONG.get(), CACTUS_ATTRACTION.get(), Ingredient.of(Tags.Items.DUSTS_REDSTONE));
    }

    private static void registerBrewingRecipe(Potion potionResult, Potion potionIngredient, Ingredient itemIngredient) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), potionIngredient)),
                itemIngredient,
                PotionUtils.setPotion(new ItemStack(Items.POTION), potionResult)
        ));
    }

    private static RegistryObject<Potion> registerPotion(String name, Supplier<Effect> effectSupplier, int duration, int amplifier) {
        return POTIONS.register(name, () -> new Potion(new EffectInstance(effectSupplier.get(), duration, amplifier)));
    }
}
