package com.sarinsa.tomfoolery.datagen.recipe;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
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

import java.util.Objects;
import java.util.function.Consumer;

public class TomRecipeProvider extends RecipeProvider {
    
    public TomRecipeProvider( DataGenerator dataGenerator ) {
        super( dataGenerator.getPackOutput() );
    }
    
    @Override
    protected void buildRecipes( Consumer<FinishedRecipe> saver ) {
        smeltingRecipe( TomBlocks.CAKE_ORE.get(), Blocks.STONE, 0.1F, saver );
        smeltingRecipe( TomBlocks.ORE_ORE.get(), TomItems.NETHERAIGHT_INGOT.get(), 0.3F, saver );
        
        smithingRecipe( Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.DIAMOND_HELMET, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_HELMET.get(), saver );
        smithingRecipe( Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.DIAMOND_CHESTPLATE, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_CHESTPLATE.get(), saver );
        smithingRecipe( Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.DIAMOND_LEGGINGS, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_LEGGINGS.get(), saver );
        smithingRecipe( Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, Items.DIAMOND_BOOTS, TomItems.NETHERAIGHT_INGOT.get(), TomItems.NETHERAIGHT_BOOTS.get(), saver );
        
        ShapedRecipeBuilder.shaped( RecipeCategory.COMBAT, TomItems.EXPLOSIVE_GRENADE_ROUND.get(), 1 )
                .pattern( "#T#" )
                .pattern( "#T#" )
                .pattern( "#N#" )
                .define( '#', Tags.Items.INGOTS_IRON )
                .define( 'T', Items.TNT )
                .define( 'N', Tags.Items.NUGGETS_IRON )
                .unlockedBy( unlockName( Items.TNT ), has( Items.TNT ) )
                .unlockedBy( unlockName( Items.IRON_INGOT ), has( Tags.Items.INGOTS_IRON ) )
                .unlockedBy( unlockName( Items.IRON_NUGGET ), has( Tags.Items.NUGGETS_IRON ) )
                .group( "grenade_ammo" )
                .save( saver );
        
        ShapedRecipeBuilder.shaped( RecipeCategory.COMBAT, TomItems.DOOM_GRENADE_ROUND.get(), 2 )
                .pattern( "#W#" )
                .pattern( "#W#" )
                .pattern( "#N#" )
                .define( '#', Tags.Items.INGOTS_IRON )
                .define( 'W', Tags.Items.NETHER_STARS )
                .define( 'N', Tags.Items.NUGGETS_IRON )
                .unlockedBy( unlockName( Items.TNT ), has( Items.TNT ) )
                .unlockedBy( unlockName( Items.IRON_INGOT ), has( Tags.Items.INGOTS_IRON ) )
                .unlockedBy( unlockName( Items.IRON_NUGGET ), has( Tags.Items.NUGGETS_IRON ) )
                .group( "grenade_ammo" )
                .save( saver );
        
        coolGlasses( Items.DIRT, TomItems.COOL_DIRT_GLASSES.get(), saver );
        coolGlasses( Items.STONE, TomItems.COOL_STONE_GLASSES.get(), saver );
    }
    
    protected void smeltingRecipe( ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> saver ) {
        String ingredientName = itemName( ingredient );
        String resultName = itemName( result );
        
        CookingRecipeBuilderNoTab.smelting( Ingredient.of( ingredient ), result, experience, 200 )
                .unlockedBy( "has_" + ingredientName, has( ingredient ) )
                .save( saver, Tomfoolery.rl( resultName + "_from_" + ingredientName + "_smelting" ) );
    }
    
    @SuppressWarnings( "unused" )
    protected void blastingRecipe( ItemLike ingredient, ItemLike result, float experience, Consumer<FinishedRecipe> saver ) {
        String ingredientName = itemName( ingredient );
        String resultName = itemName( result );
        
        CookingRecipeBuilderNoTab.blasting( Ingredient.of( ingredient ), result, experience, 100 )
                .unlockedBy( "has_" + ingredientName, has( ingredient ) )
                .save( saver, Tomfoolery.rl( resultName + "_from_" + ingredientName + "_blasting" ) );
    }
    
    @SuppressWarnings( { "ConstantConditions", "SameParameterValue" } )
    protected void smithingRecipe( ItemLike template, ItemLike base, ItemLike addition, Item result, Consumer<FinishedRecipe> saver ) {
        SmithingTransformRecipeBuilderNoTab.smithing( Ingredient.of( template ), Ingredient.of( base ), Ingredient.of( addition ), result )
                .unlocks( "has_" + itemName( addition ), has( addition ) )
                .unlocks( "has_" + itemName( template ), has( template ) )
                .save( saver, regName( result ) );
    }
    
    protected void coolGlasses( ItemLike base, ItemLike result, Consumer<FinishedRecipe> saver ) {
        ShapedRecipeBuilder.shaped( RecipeCategory.TOOLS, result )
                .pattern( " N " )
                .pattern( "GBG" )
                .define( 'N', Tags.Items.NETHER_STARS )
                .define( 'G', Items.GLASS )
                .define( 'B', base )
                .unlockedBy( unlockName( Items.NETHER_STAR ), has( Tags.Items.NETHER_STARS ) )
                .unlockedBy( unlockName( Items.GLASS ), has( Tags.Items.GLASS ) )
                .unlockedBy( unlockName( base ), has( base ) )
                .save( saver );
    }
    
    protected static String itemName( ItemLike itemLike ) {
        return Objects.requireNonNull( ForgeRegistries.ITEMS.getKey( itemLike.asItem() ) ).getPath();
    }
    
    protected static ResourceLocation regName( ItemLike itemLike ) {
        return Objects.requireNonNull( ForgeRegistries.ITEMS.getKey( itemLike.asItem() ) );
    }
    
    private static String unlockName( ItemLike itemProvider ) {
        return "has_" + itemName( itemProvider );
    }
}
