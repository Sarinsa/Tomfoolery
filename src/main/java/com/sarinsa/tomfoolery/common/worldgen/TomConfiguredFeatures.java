package com.sarinsa.tomfoolery.common.worldgen;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.worldgen.biome.modifier.ModOres;
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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TomConfiguredFeatures {
    
    public static final DeferredRegister<ConfiguredFeature<?, ?>> CF_REGISTRY = DeferredRegister.create( Registries.CONFIGURED_FEATURE, Tomfoolery.MODID );
    public static final DeferredRegister<PlacedFeature> P_REGISTRY = DeferredRegister.create( Registries.PLACED_FEATURE, Tomfoolery.MODID );
    
    private static final Map<ModOres, RegistryObject<PlacedFeature>> ORES_BY_TYPE = new HashMap<>();
    
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_ORE = configuredKey( "ore_ore" );
    public static final ResourceKey<ConfiguredFeature<?, ?>> CAKE_ORE = configuredKey( "cake_ore" );
    
    public static final ResourceKey<PlacedFeature> PLACED_ORE_ORE = placedKey( "ore_ore" );
    public static final ResourceKey<PlacedFeature> PLACED_CAKE_ORE = placedKey( "cake_ore" );
    
    
    public static void bootstrapConfigured( BootstapContext<ConfiguredFeature<?, ?>> context ) {
        register( context, TomConfiguredFeatures.ORE_ORE, new ConfiguredFeature<>( Feature.ORE, new OreConfiguration( new BlockMatchTest( Blocks.STONE ), TomBlocks.ORE_ORE.get().defaultBlockState(), 9 ) ) );
        register( context, TomConfiguredFeatures.CAKE_ORE, new ConfiguredFeature<>( Feature.ORE, new OreConfiguration( new BlockMatchTest( Blocks.STONE ), TomBlocks.ORE_ORE.get().defaultBlockState(), 9 ) ) );
    }
    
    
    public static void bootstrapPlaced( BootstapContext<PlacedFeature> context ) {
        HolderGetter<ConfiguredFeature<?, ?>> getter = context.lookup( Registries.CONFIGURED_FEATURE );
        
        final Holder<ConfiguredFeature<?, ?>> ORE_ORE = getter.getOrThrow( TomConfiguredFeatures.ORE_ORE );
        final Holder<ConfiguredFeature<?, ?>> CAKE_ORE = getter.getOrThrow( TomConfiguredFeatures.CAKE_ORE );
        
        register( context, TomConfiguredFeatures.PLACED_ORE_ORE, ORE_ORE, rareOrePlacement( 3, HeightRangePlacement.triangle( VerticalAnchor.absolute( -16 ), VerticalAnchor.absolute( 40 ) ) ) );
        register( context, TomConfiguredFeatures.PLACED_CAKE_ORE, CAKE_ORE, rareOrePlacement( 6, HeightRangePlacement.triangle( VerticalAnchor.absolute( -16 ), VerticalAnchor.absolute( 70 ) ) ) );
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
    
    
    private static List<PlacementModifier> orePlacement( PlacementModifier modifier1, PlacementModifier modifier2 ) {
        return List.of( modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome() );
    }
    
    private static List<PlacementModifier> commonOrePlacement( int count, PlacementModifier modifier ) {
        return orePlacement( CountPlacement.of( count ), modifier );
    }
    
    private static List<PlacementModifier> rareOrePlacement( int count, PlacementModifier modifier ) {
        return orePlacement( RarityFilter.onAverageOnceEvery( count ), modifier );
    }
    
    
    @Nullable
    public static RegistryObject<PlacedFeature> getOreForType( ModOres ore ) {
        return ORES_BY_TYPE.get( ore );
    }
}
