package com.sarinsa.tomfoolery.common.tags;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class TomTags {
    
    public static class Items {
        
        public static final TagKey<Item> CAKES = forgeTag( "cakes" );
        public static final TagKey<Item> ORES = modTag( "ores" );
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<Item> modTag( String path ) {
            return ItemTags.create( Tomfoolery.rl( path ) );
        }
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<Item> forgeTag( String path ) {
            return ItemTags.create( ResourceLocation.fromNamespaceAndPath( "forge", path ) );
        }
    }
    
    public static class Blocks {
        
        public static final TagKey<Block> ORES = modTag( "ores" );
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<Block> modTag( String path ) {
            return BlockTags.create( Tomfoolery.rl( path ) );
        }
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<Block> forgeTag( String path ) {
            return BlockTags.create( ResourceLocation.fromNamespaceAndPath( "forge", path ) );
        }
    }
    
    public static class PlacedFeatures {
        
        public static final TagKey<PlacedFeature> ORES = modTag( "ores" );
        public static final TagKey<PlacedFeature> OVERWORLD_ORES = modTag( "overworld_ores" );
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<PlacedFeature> modTag( String path ) {
            return TagKey.create( Registries.PLACED_FEATURE, Tomfoolery.rl( path ) );
        }
        
        @SuppressWarnings( "SameParameterValue" )
        private static TagKey<PlacedFeature> forgeTag( String path ) {
            return TagKey.create( Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath( "forge", path ) );
        }
    }
}
