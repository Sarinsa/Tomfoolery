package com.sarinsa.tomfoolery.datagen.worldgen;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;

import java.util.List;


public class TomFeatureProvider {
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ORE = configuredKey( "ore_ore" );
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAKE_ORE = configuredKey( "cake_ore" );
    
    public static final ResourceKey<PlacedFeature> PLACED_ORE_ORE = placedKey( "ore_ore" );
    public static final ResourceKey<PlacedFeature> PLACED_CAKE_ORE = placedKey( "cake_ore" );
    
    
    public static void bootstrapConfigured( BootstapContext<ConfiguredFeature<?, ?>> context ) {
        register( context, TomFeatureProvider.ORE_ORE, new ConfiguredFeature<>( Feature.ORE, new OreConfiguration(
                new BlockMatchTest( Blocks.STONE ),
                TomBlocks.ORE_ORE.get().defaultBlockState(),
                8 )
        ) );
        register( context, TomFeatureProvider.CAKE_ORE, new ConfiguredFeature<>( Feature.ORE, new OreConfiguration(
                new BlockMatchTest( Blocks.STONE ),
                TomBlocks.CAKE_ORE.get().defaultBlockState(),
                5 )
        ) );
    }
    
    
    public static void bootstrapPlaced( BootstapContext<PlacedFeature> context ) {
        HolderGetter<ConfiguredFeature<?, ?>> getter = context.lookup( Registries.CONFIGURED_FEATURE );
        
        final Holder<ConfiguredFeature<?, ?>> ORE_ORE = getter.getOrThrow( TomFeatureProvider.ORE_ORE );
        final Holder<ConfiguredFeature<?, ?>> CAKE_ORE = getter.getOrThrow( TomFeatureProvider.CAKE_ORE );
        
        register( context, TomFeatureProvider.PLACED_ORE_ORE, ORE_ORE, rareOrePlacement(
                3,
                HeightRangePlacement.triangle(
                        VerticalAnchor.absolute( 0 ),
                        VerticalAnchor.absolute( 40 )
                )
        ) );
        register( context, TomFeatureProvider.PLACED_CAKE_ORE, CAKE_ORE, rareOrePlacement(
                2,
                HeightRangePlacement.triangle(
                        VerticalAnchor.absolute( 10 ),
                        VerticalAnchor.absolute( 80 ) )
        ) );
    }
    
    
    protected static void register( BootstapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> confFeatureKey, ConfiguredFeature<?, ?> configuredFeature ) {
        context.register( confFeatureKey, configuredFeature );
    }
    
    protected static void register( BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> placedFeatureKey, Holder<ConfiguredFeature<?, ?>> configuredFeature, PlacementModifier... modifiers ) {
        register( context, placedFeatureKey, configuredFeature, List.of( modifiers ) );
    }
    
    protected static void register( BootstapContext<PlacedFeature> context, ResourceKey<PlacedFeature> placedFeatureKey, Holder<ConfiguredFeature<?, ?>> configuredFeature, List<PlacementModifier> modifiers ) {
        context.register( placedFeatureKey, new PlacedFeature( configuredFeature, modifiers ) );
    }
    
    public static ResourceKey<ConfiguredFeature<?, ?>> configuredKey( String name ) {
        return ResourceKey.create( Registries.CONFIGURED_FEATURE, Tomfoolery.rl( name ) );
    }
    
    public static ResourceKey<PlacedFeature> placedKey( String name ) {
        return ResourceKey.create( Registries.PLACED_FEATURE, Tomfoolery.rl( name ) );
    }
    
    
    private static List<PlacementModifier> orePlacement( PlacementModifier placementCount, HeightRangePlacement heightRange ) {
        return List.of( placementCount, InSquarePlacement.spread(), heightRange, BiomeFilter.biome() );
    }
    
    private static List<PlacementModifier> commonOrePlacement( int count, HeightRangePlacement modifier ) {
        return orePlacement( CountPlacement.of( count ), modifier );
    }
    
    private static List<PlacementModifier> rareOrePlacement( int count, HeightRangePlacement modifier ) {
        return orePlacement( RarityFilter.onAverageOnceEvery( count ), modifier );
    }
}
