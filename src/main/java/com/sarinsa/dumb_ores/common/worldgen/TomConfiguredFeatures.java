package com.sarinsa.dumb_ores.common.worldgen;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.core.registry.TomBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class TomConfiguredFeatures {

    public static ConfiguredFeature<?, ?> ORE_ORE;
    public static ConfiguredFeature<?, ?> CAKE_ORE;

    public static void registerFeatures() {
        ORE_ORE = register("ore_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TomBlocks.ORE_ORE.get().defaultBlockState(), 3)).range(16).squared().count(2));
        CAKE_ORE = register("cake_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, TomBlocks.CAKE_ORE.get().defaultBlockState(), 4)).range(40).squared().count(5));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Tomfoolery.resourceLoc(name).toString(), configuredFeature);
    }
}
