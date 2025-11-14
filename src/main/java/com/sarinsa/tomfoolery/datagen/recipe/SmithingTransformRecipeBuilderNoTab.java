package com.sarinsa.tomfoolery.datagen.recipe;

import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;

public class SmithingTransformRecipeBuilderNoTab {
    
    private final Ingredient template;
    private final Ingredient base;
    private final Ingredient addition;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();
    private final RecipeSerializer<?> type;
    
    public SmithingTransformRecipeBuilderNoTab( RecipeSerializer<?> type, Ingredient template, Ingredient base, Ingredient addition, Item result ) {
        this.type = type;
        this.template = template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }
    
    public static SmithingTransformRecipeBuilderNoTab smithing( Ingredient template, Ingredient base, Ingredient addition, Item result ) {
        return new SmithingTransformRecipeBuilderNoTab( RecipeSerializer.SMITHING_TRANSFORM, template, base, addition, result );
    }
    
    public SmithingTransformRecipeBuilderNoTab unlocks( String unlockName, CriterionTriggerInstance trigger ) {
        advancement.addCriterion( unlockName, trigger );
        return this;
    }
    
    public void save( Consumer<FinishedRecipe> saver, ResourceLocation recipeId ) {
        ensureValid( recipeId );
        advancement.parent( RecipeBuilder.ROOT_RECIPE_ADVANCEMENT )
                .addCriterion( "has_the_recipe", RecipeUnlockedTrigger.unlocked( recipeId ) )
                .rewards( AdvancementRewards.Builder.recipe( recipeId ) )
                .requirements( RequirementsStrategy.OR );
        saver.accept( new SmithingTransformRecipeBuilderNoTab.Result( recipeId, type, template, base, addition, result, advancement, recipeId.withPrefix( "recipes/" ) ) );
    }
    
    private void ensureValid( ResourceLocation recipeId ) {
        if( advancement.getCriteria().isEmpty() ) {
            throw new IllegalStateException( "No way of obtaining recipe " + recipeId );
        }
    }
    
    public record Result(ResourceLocation id, RecipeSerializer<?> type, Ingredient template, Ingredient base,
                         Ingredient addition, Item result, Advancement.Builder advancement,
                         ResourceLocation advancementId) implements FinishedRecipe {
        
        public void serializeRecipeData( JsonObject jsonObject ) {
            jsonObject.add( "template", template.toJson() );
            jsonObject.add( "base", base.toJson() );
            jsonObject.add( "addition", addition.toJson() );
            
            JsonObject item = new JsonObject();
            item.addProperty( "item", Objects.requireNonNull( ForgeRegistries.ITEMS.getKey( result ) ).toString() );
            jsonObject.add( "result", item );
        }
        
        public ResourceLocation getId() {
            return id;
        }
        
        public RecipeSerializer<?> getType() {
            return type;
        }
        
        public JsonObject serializeAdvancement() {
            return advancement.serializeToJson();
        }
        
        @Nullable
        public ResourceLocation getAdvancementId() {
            return advancementId;
        }
    }
}
