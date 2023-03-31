package com.sarinsa.tomfoolery.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.UpgradeRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Copy-paste of {@link UpgradeRecipeBuilder},
 * modified to not fail when the result item
 * has no item group/creative tab.
 */
public class SmithingRecipeBuilderNoTab {

    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final RecipeSerializer<?> type;

    public SmithingRecipeBuilderNoTab(RecipeSerializer<?> recipeSerializer, Ingredient ingredient, Ingredient addition, Item result) {
        this.type = recipeSerializer;
        this.base = ingredient;
        this.addition = addition;
        this.result = result;
    }

    public static SmithingRecipeBuilderNoTab smithing(Ingredient ingredient, Ingredient addition, Item result) {
        return new SmithingRecipeBuilderNoTab(RecipeSerializer.SMITHING, ingredient, addition, result);
    }

    public SmithingRecipeBuilderNoTab unlocks(String conditionName, CriterionTriggerInstance criterionInstance) {
        this.advancement.addCriterion(conditionName, criterionInstance);
        return this;
    }

    public void save(Consumer<FinishedRecipe> consumer, String id) {
        this.save(consumer, new ResourceLocation(id));
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
                this.type,
                this.base,
                this.addition,
                this.result,
                this.advancement,
                new ResourceLocation(id.getNamespace(), "recipes/" + category + "/" + id.getPath())));
    }

    private void ensureValid(ResourceLocation id) {
        if (this.advancement.getCriteria().isEmpty()) {
            throw new IllegalStateException("No way of obtaining recipe " + id);
        }
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient base;
        private final Ingredient addition;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final RecipeSerializer<?> type;

        public Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient base, Ingredient addition, Item result, Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = id;
            this.type = type;
            this.base = base;
            this.addition = addition;
            this.result = result;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        public void serializeRecipeData(JsonObject jsonObject) {
            jsonObject.add("base", this.base.toJson());
            jsonObject.add("addition", this.addition.toJson());
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(result).toString());
            jsonObject.add("result", jsonobject);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public RecipeSerializer<?> getType() {
            return this.type;
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
