package com.sarinsa.dumbores.common.event;

import com.sarinsa.dumbores.common.worldgen.DOConfiguredFeatures;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.MobSpawnInfoBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBiomeLoad(BiomeLoadingEvent event) {
        ResourceLocation biomeName = event.getName();

        if (biomeName == null)
            return;

        BiomeGenerationSettingsBuilder generationSettings = event.getGeneration();
        MobSpawnInfoBuilder spawnInfoBuilder = event.getSpawns();

        if (hasDictType(biomeName, BiomeDictionary.Type.OVERWORLD)) {
            generationSettings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, DOConfiguredFeatures.ORE_ORE);
        }

        if (biomeName.equals(Biomes.JUNGLE.location())) {
            generationSettings.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, DOConfiguredFeatures.CAKE_ORE);
        }
    }

    public static boolean hasDictType(ResourceLocation biomeName, BiomeDictionary.Type type) {
        return BiomeDictionary.hasType(RegistryKey.create(ForgeRegistries.Keys.BIOMES, biomeName), type);
    }
}
