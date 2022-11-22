package com.sarinsa.dumb_ores.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.CookingRecipeSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Copy-paste of {@link CookingRecipeBuilder},
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
    private final CookingRecipeSerializer<?> serializer;

    private CookingRecipeBuilderNoTab(IItemProvider ingredient, Ingredient result, float xp, int cookingTime, CookingRecipeSerializer<?> serializer) {
        this.result = ingredient.asItem();
        this.ingredient = result;
        this.experience = xp;
        this.cookingTime = cookingTime;
        this.serializer = serializer;
    }

    public static CookingRecipeBuilderNoTab cooking(Ingredient ingredient, IItemProvider result, float xp, int cookingTime, CookingRecipeSerializer<?> serializer) {
        return new CookingRecipeBuilderNoTab(result, ingredient, xp, cookingTime, serializer);
    }

    public static CookingRecipeBuilderNoTab blasting(Ingredient ingredient, IItemProvider result, float xp, int cookingTime) {
        return cooking(ingredient, result, xp, cookingTime, IRecipeSerializer.BLASTING_RECIPE);
    }

    public static CookingRecipeBuilderNoTab smelting(Ingredient ingredient, IItemProvider result, float xp, int cookingTime) {
        return cooking(ingredient, result, xp, cookingTime, IRecipeSerializer.SMELTING_RECIPE);
    }

    public CookingRecipeBuilderNoTab unlockedBy(String criterionName, ICriterionInstance instance) {
        this.advancement.addCriterion(criterionName, instance);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer) {
        this.save(consumer, ForgeRegistries.ITEMS.getKey(this.result));
    }

    public void save(Consumer<IFinishedRecipe> consumer, String name) {
        ResourceLocation resourcelocation = ForgeRegistries.ITEMS.getKey(this.result);
        ResourceLocation resourcelocation1 = new ResourceLocation(name);

        if (resourcelocation1.equals(resourcelocation)) {
            throw new IllegalStateException("Recipe " + resourcelocation1 + " should remove its 'save' argument");
        }
        else {
            this.save(consumer, resourcelocation1);
        }
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(IRequirementsStrategy.OR);

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

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final String group;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<? extends AbstractCookingRecipe> serializer;

        public Result(ResourceLocation id, String group, Ingredient ingredient, Item result, float experience, int cookingTime, Advancement.Builder advancement, ResourceLocation advancementId, IRecipeSerializer<? extends AbstractCookingRecipe> serializer) {
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

        public IRecipeSerializer<?> getType() {
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
