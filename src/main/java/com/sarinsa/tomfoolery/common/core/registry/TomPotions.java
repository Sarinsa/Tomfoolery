package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TomPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Tomfoolery.MODID);

    public static final RegistryObject<Potion> CACTUS_ATTRACTION = registerPotion("cactus_attraction", TomEffects.CACTUS_ATTRACTION, 900, 0);
    public static final RegistryObject<Potion> CACTUS_ATTRACTION_LONG = registerPotion("cactus_attraction_long", TomEffects.CACTUS_ATTRACTION, 1800, 0);
    public static final RegistryObject<Potion> CACTUS_ATTRACTION_STRONG = registerPotion("cactus_attraction_strong", TomEffects.CACTUS_ATTRACTION, 700, 1);

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

    private static RegistryObject<Potion> registerPotion(String name, Supplier<MobEffect> effectSupplier, int duration, int amplifier) {
        return POTIONS.register(name, () -> new Potion(new MobEffectInstance(effectSupplier.get(), duration, amplifier)));
    }
}
