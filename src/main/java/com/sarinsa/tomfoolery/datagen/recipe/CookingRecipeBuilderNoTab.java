package com.sarinsa.tomfoolery.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCookingSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Copy-paste of {@link net.minecraft.data.recipes.SimpleCookingRecipeBuilder},
 * modified to not fail when the result item
 * has no item group/creative tab.
 */
public class CookingRecipeBuilderNoTab {

    private final Item result;
    private final Ingredient ingredient;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private String group;
    private final SimpleCookingSerializer<?> serializer;

    private CookingRecipeBuilderNoTab(ItemLike ingredient, Ingredient result, float xp, int cookingTime, SimpleCookingSerializer<?> serializer) {
        this.result = ingredient.asItem();
        this.ingredient = result;
        this.experience = xp;
        this.cookingTime = cookingTime;
        this.serializer = serializer;
    }

    public static CookingRecipeBuilderNoTab cooking(Ingredient ingredient, ItemLike result, float xp, int cookingTime, SimpleCookingSerializer<?> serializer) {
        return new CookingRecipeBuilderNoTab(result, ingredient, xp, cookingTime, serializer);
    }

    public static CookingRecipeBuilderNoTab blasting(Ingredient ingredient, ItemLike result, float xp, int cookingTime) {
        return cooking(ingredient, result, xp, cookingTime, RecipeSerializer.BLASTING_RECIPE);
    }

    public static CookingRecipeBuilderNoTab smelting(Ingredient ingredient, ItemLike result, float xp, int cookingTime) {
        return cooking(ingredient, result, xp, cookingTime, RecipeSerializer.SMELTING_RECIPE);
    }

    public CookingRecipeBuilderNoTab unlockedBy(String criterionName, CriterionTriggerInstance instance) {
        this.advancement.addCriterion(criterionName, instance);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer) {
        this.save(consumer, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void save(Consumer<FinishedRecipe> consumer, String name) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(name);

        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        }
        else {
            save(consumer, resourcelocation1);
        }
    }

    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(RequirementsStrategy.OR);

        String category = result.getItemCategory() == null ? "unspecified" : result.getItemCategory().getRecipeFolderName();

        consumer.accept(new Result(
                id,
                this.group == null ? "" : this.group,
                this.ingredient,
                this.result,
                this.experience,
                this.cookingTime,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + category + "/" + id.getPath()),
                this.serializer)
        );
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation id, String group, Ingredient ingredient, Item result, float experience, int cookingTime, Advancement.Builder advancement, ResourceLocation advancementId, RecipeSerializer<? extends AbstractCookingRecipe> serializer) {
            this.id = id;
            this.group = group;
            this.ingredient = ingredient;
            this.result = result;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.advancement = advancement;
            this.advancementId = advancementId;
            this.serializer = serializer;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            if (!this.group.isEmpty()) {
                jsonObject.addProperty("group", this.group);
            }

            jsonObject.add("ingredient", this.ingredient.toJson());
            jsonObject.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            jsonObject.addProperty("experience", this.experience);
            jsonObject.addProperty("cookingtime", this.cookingTime);
        }

        public RecipeSerializer<?> getType() {
            return this.serializer;
        }

        public ResourceLocation getId() {
            return this.id;
        }

        @Nullable
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
