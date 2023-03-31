package com.sarinsa.tomfoolery.datagen.recipe;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Consumer;

public class TomRecipeProvider extends RecipeProvider {

    public TomRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        smeltingRecipe(TomBlocks.CAKE_ORE.get(), Blocks.STONE, 0.1F, consumer);
        smeltingRecipe(TomBlocks.ORE_ORE.get(), TomItems.NETHERAIGHT_INGOT.get(), 0.3F, consumer);

        smithingRecipe(Items.DIAMOND_HELMET, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_HELMET.get(), consumer);
        smithingRecipe(Items.DIAMOND_CHESTPLATE, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_CHESTPLATE.get(), consumer);
        smithingRecipe(Items.DIAMOND_LEGGINGS, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_LEGGINGS.get(), consumer);
        smithingRecipe(Items.DIAMOND_BOOTS, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_BOOTS.get(), consumer);


        ShapedRecipeBuilder.shaped(TomItems.EXPLOSIVE_GRENADE_ROUND.get(), 1)
                .pattern("#T#")
                .pattern("#T#")
                .pattern("#N#")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('T', Items.TNT)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy(unlockName(Items.TNT), has(Items.TNT))
                .unlockedBy(unlockName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .unlockedBy(unlockName(Items.IRON_NUGGET), has(Tags.Items.NUGGETS_IRON))
                .group("grenade_ammo")
                .save(consumer);

        ShapedRecipeBuilder.shaped(TomItems.DOOM_GRENADE_ROUND.get(), 2)
                .pattern("#W#")
                .pattern("#W#")
                .pattern("#N#")
                .define('#', Tags.Items.INGOTS_IRON)
                .define('W', Tags.Items.NETHER_STARS)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy(unlockName(Items.TNT), has(Items.TNT))
                .unlockedBy(unlockName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .unlockedBy(unlockName(Items.IRON_NUGGET), has(Tags.Items.NUGGETS_IRON))
                .group("grenade_ammo")
                .save(consumer);

        ShapedRecipeBuilder.shaped(TomItems.COOL_DIRT_GLASSES.get())
                .pattern(" N ")
                .pattern("GDG")
                .define('N', Tags.Items.NETHER_STARS)
                .define('G', Items.GLASS)
                .define('D', Items.DIRT)
                .unlockedBy(unlockName(Items.NETHER_STAR), has(Tags.Items.NETHER_STARS))
                .unlockedBy(unlockName(Items.GLASS), has(Tags.Items.GLASS))
                .unlockedBy(unlockName(Items.DIRT), has(Items.DIRT))
                .save(consumer);
    }

    protected void smeltingRecipe(ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String ingredientName = itemName(ingredient);
        String resultName = itemName(result);

        CookingRecipeBuilderNoTab.smelting(Ingredient.of(ingredient), result, experience, 200)
                .unlockedBy("has_" + ingredientName, has(ingredient))
                .save(consumer, Tomfoolery.resourceLoc(resultName + "_from_" + ingredientName + "_smelting"));
    }

    protected void blastingRecipe(ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> consumer) {
        String ingredientName = itemName(ingredient);
        String resultName = itemName(result);

        CookingRecipeBuilderNoTab.blasting(Ingredient.of(ingredient), result, experience, 100)
                .unlockedBy("has_" + ingredientName, has(ingredient))
                .save(consumer, Tomfoolery.resourceLoc(resultName + "_from_" + ingredientName + "_blasting"));
    }

    @SuppressWarnings("ConstantConditions")
    protected void smithingRecipe(ItemLike ingredient, ItemLike alloy, Item result, Consumer<FinishedRecipe> consumer) {
        SmithingRecipeBuilderNoTab.smithing(Ingredient.of(ingredient), Ingredient.of(alloy), result)
                .unlocks("has_" + itemName(alloy), has(alloy))
                .save(consumer, regName(result));
    }

    @Nonnull
    protected static String itemName(ItemLike itemLike) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemLike.asItem())).getPath();
    }

    @Nonnull
    protected static ResourceLocation regName(ItemLike itemLike) {
        return Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(itemLike.asItem()));
    }

    private static String unlockName(ItemLike itemProvider) {
        return "has_" + itemName(itemProvider);
    }
}
