package com.teamaurora.orespawn.common.worldgen;

import com.teamaurora.orespawn.common.core.Orespawn;
import com.teamaurora.orespawn.common.core.registry.OrespawnBlocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class OrespawnConfiguredFeatures {

    public static ConfiguredFeature<?, ?> ORE_ORE;

    public static void registerFeatures() {
        ORE_ORE = register("ore_ore", Feature.ORE.configured(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, OrespawnBlocks.ORE_ORE.get().defaultBlockState(), 3)).range(16).squared().count(2));
    }

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String name, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Orespawn.resourceLoc(name).toString(), configuredFeature);
    }
}
