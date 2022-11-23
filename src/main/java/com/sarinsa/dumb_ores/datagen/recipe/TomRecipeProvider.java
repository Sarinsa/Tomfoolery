package com.sarinsa.dumb_ores.datagen.recipe;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.core.registry.TomBlocks;
import com.sarinsa.dumb_ores.common.core.registry.TomItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

import java.util.Objects;
import java.util.function.Consumer;

public class TomRecipeProvider extends RecipeProvider {

    public TomRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildShapelessRecipes(Consumer<IFinishedRecipe> consumer) {
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
                .define('W', Items.NETHER_STAR)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy(unlockName(Items.TNT), has(Items.TNT))
                .unlockedBy(unlockName(Items.IRON_INGOT), has(Tags.Items.INGOTS_IRON))
                .unlockedBy(unlockName(Items.IRON_NUGGET), has(Tags.Items.NUGGETS_IRON))
                .group("grenade_ammo")
                .save(consumer);
    }

    protected void smeltingRecipe(IItemProvider ingredient, IItemProvider result, float experience, Consumer<IFinishedRecipe> consumer) {
        String ingredientName = Objects.requireNonNull(itemName(ingredient));
        String resultName = Objects.requireNonNull(itemName(result));

        CookingRecipeBuilderNoTab.smelting(Ingredient.of(ingredient), result, experience, 200)
                .unlockedBy("has_" + ingredientName, has(ingredient))
                .save(consumer, Tomfoolery.resourceLoc(resultName + "_from_" + ingredientName + "_smelting"));
    }

    protected void blastingRecipe(IItemProvider ingredient, IItemProvider result, float experience, Consumer<IFinishedRecipe> consumer) {
        String ingredientName = itemName(ingredient);
        String resultName = itemName(result);

        CookingRecipeBuilderNoTab.blasting(Ingredient.of(ingredient), result, experience, 100)
                .unlockedBy("has_" + ingredientName, has(ingredient))
                .save(consumer, Tomfoolery.resourceLoc(resultName + "_from_" + ingredientName + "_blasting"));
    }

    @SuppressWarnings("ConstantConditions")
    protected void smithingRecipe(IItemProvider ingredient, IItemProvider alloy, Item result, Consumer<IFinishedRecipe> consumer) {
        SmithingRecipeBuilderNoTab.smithing(Ingredient.of(ingredient), Ingredient.of(alloy), result)
                .unlocks("has_" + alloy.asItem().getRegistryName().getPath(), has(alloy))
                .save(consumer, result.getRegistryName());
    }

    protected static String itemName(IItemProvider criterionItem) {
        return Objects.requireNonNull(criterionItem.asItem().getRegistryName()).getPath();
    }

    private static String unlockName(IItemProvider itemProvider) {
        return "has_" + itemName(itemProvider);
    }
}
