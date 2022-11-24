package com.sarinsa.tomfoolery.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.SmithingRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Copy-paste of {@link net.minecraft.data.SmithingRecipeBuilder},
 * modified to not fail when the result item
 * has no item group/creative tab.
 */
public class SmithingRecipeBuilderNoTab {

    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();
    private final IRecipeSerializer<?> type;

    public SmithingRecipeBuilderNoTab(IRecipeSerializer<?> recipeSerializer, Ingredient ingredient, Ingredient addition, Item result) {
        this.type = recipeSerializer;
        this.base = ingredient;
        this.addition = addition;
        this.result = result;
    }

    public static SmithingRecipeBuilderNoTab smithing(Ingredient ingredient, Ingredient addition, Item result) {
        return new SmithingRecipeBuilderNoTab(IRecipeSerializer.SMITHING, ingredient, addition, result);
    }

    public SmithingRecipeBuilderNoTab unlocks(String conditionName, ICriterionInstance criterionInstance) {
        this.advancement.addCriterion(conditionName, criterionInstance);
        return this;
    }

    public void save(Consumer<IFinishedRecipe> consumer, String id) {
        this.save(consumer, new ResourceLocation(id));
    }

    public void save(Consumer<IFinishedRecipe> consumer, ResourceLocation id) {
        this.ensureValid(id);
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
                .rewards(AdvancementRewards.Builder.recipe(id))
                .requirements(IRequirementsStrategy.OR);

        String category = result.getItemCategory() == null ? "unspecified" : result.getItemCategory().getRecipeFolderName();

        consumer.accept(new SmithingRecipeBuilder.Result(
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

    public static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient base;
        private final Ingredient addition;
        private final Item result;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;
        private final IRecipeSerializer<?> type;

        public Result(ResourceLocation id, IRecipeSerializer<?> type, Ingredient base, Ingredient addition, Item result, Advancement.Builder advancement, ResourceLocation advancementId) {
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
            jsonobject.addProperty("item", Registry.ITEM.getKey(this.result).toString());
            jsonObject.add("result", jsonobject);
        }

        public ResourceLocation getId() {
            return this.id;
        }

        public IRecipeSerializer<?> getType() {
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
