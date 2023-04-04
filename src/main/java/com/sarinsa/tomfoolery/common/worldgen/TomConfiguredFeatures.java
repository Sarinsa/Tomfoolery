package com.sarinsa.tomfoolery.common.worldgen;

import com.google.common.collect.ImmutableList;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.worldgen.biome.modifier.ModOres;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.MegaJungleFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.MegaJungleTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.data.worldgen.placement.VegetationPlacements.TREE_THRESHOLD;

public class TomConfiguredFeatures {

    public static final DeferredRegister<ConfiguredFeature<?, ?>> CF_REGISTRY = DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, Tomfoolery.MODID);
    public static final DeferredRegister<PlacedFeature> P_REGISTRY = DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, Tomfoolery.MODID);


    private static final Map<ModOres, RegistryObject<PlacedFeature>> ORES_BY_TYPE = new HashMap<>();


    //----------------------------- CONFIGURED ----------------------------------
    public static RegistryObject<ConfiguredFeature<OreConfiguration, ?>> ORE_ORE = registerOre("ore_ore", () -> new OreConfiguration(OreFeatures.NATURAL_STONE, TomBlocks.ORE_ORE.get().defaultBlockState(), 9));
    public static RegistryObject<ConfiguredFeature<OreConfiguration, ?>> CAKE_ORE = registerOre("cake_ore", () -> new OreConfiguration(OreFeatures.NATURAL_STONE, TomBlocks.CAKE_ORE.get().defaultBlockState(), 9));


    //------------------------------- PLACED ------------------------------------
    public static final RegistryObject<PlacedFeature> PLACED_ORE_ORE = registerOre(ModOres.ORE_ORE, "ore_ore", ORE_ORE, () -> rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(40))));
    public static final RegistryObject<PlacedFeature> PLACED_CAKE_ORE = registerOre(ModOres.CAKE, "cake_ore", CAKE_ORE, () -> rareOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(70))));




    private static <FC extends FeatureConfiguration, F extends Feature<FC>> RegistryObject<ConfiguredFeature<FC, ?>> register(String name, Supplier<F> feature, Supplier<FC> config) {
        return CF_REGISTRY.register(name, () -> new ConfiguredFeature<>(feature.get(), config.get()));
    }

    private static RegistryObject<ConfiguredFeature<OreConfiguration, ?>> registerOre(String name, Supplier<OreConfiguration> configuration) {
        return register(name, () -> Feature.ORE, configuration);
    }

    private static RegistryObject<PlacedFeature> registerOre(ModOres ore, String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> modifiers) {
        RegistryObject<PlacedFeature> placedFeature = registerPlaced(name, feature, modifiers);
        ORES_BY_TYPE.put(ore, placedFeature);
        return placedFeature;
    }

    private static RegistryObject<PlacedFeature> registerPlaced(String name, RegistryObject<? extends ConfiguredFeature<?, ?>> feature, Supplier<List<PlacementModifier>> modifiers) {
        return P_REGISTRY.register(name, () -> new PlacedFeature(Holder.hackyErase(feature.getHolder().orElseThrow()), modifiers.get()));
    }




    private static List<PlacementModifier> orePlacement(PlacementModifier modifier1, PlacementModifier modifier2) {
        return List.of(modifier1, InSquarePlacement.spread(), modifier2, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(CountPlacement.of(count), modifier);
    }

    private static List<PlacementModifier> rareOrePlacement(int count, PlacementModifier modifier) {
        return orePlacement(RarityFilter.onAverageOnceEvery(count), modifier);
    }



    private static ImmutableList.Builder<PlacementModifier> treePlacementBase(PlacementModifier modifier) {
        return ImmutableList.<PlacementModifier>builder().add(modifier).add(InSquarePlacement.spread()).add(TREE_THRESHOLD).add(PlacementUtils.HEIGHTMAP_OCEAN_FLOOR).add(BiomeFilter.biome());
    }

    public static List<PlacementModifier> treePlacement(PlacementModifier modifier) {
        return treePlacementBase(modifier).build();
    }

    public static List<PlacementModifier> treePlacement(PlacementModifier modifier, Block block) {
        return treePlacementBase(modifier).add(BlockPredicateFilter.forPredicate(BlockPredicate.wouldSurvive(block.defaultBlockState(), BlockPos.ZERO))).build();
    }

    @Nullable
    public static RegistryObject<PlacedFeature> getOreForType(ModOres ore) {
        return ORES_BY_TYPE.get(ore);
    }
}
