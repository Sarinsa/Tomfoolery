package com.sarinsa.tomfoolery.common.worldgen.biome.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sarinsa.tomfoolery.common.core.registry.TomBiomeModifiers;
import com.sarinsa.tomfoolery.common.worldgen.TomConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.RegistryObject;

public record AddOreModifier(HolderSet<Biome> biomes, GenerationStep.Decoration step, ModOres ore) implements BiomeModifier {

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            BiomeGenerationSettingsBuilder settings = builder.getGenerationSettings();
            RegistryObject<PlacedFeature> oreFeature = TomConfiguredFeatures.getOreForType(ore);

            if (oreFeature != null) {
                settings.addFeature(step, oreFeature.getHolder().orElse(OrePlacements.ORE_COAL_LOWER));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return TomBiomeModifiers.ADD_ORE.get();
    }

    public static Codec<AddOreModifier> create() {
        return RecordCodecBuilder.create(builder -> builder.group(
                Biome.LIST_CODEC.fieldOf("biomes").forGetter(AddOreModifier::biomes),
                GenerationStep.Decoration.CODEC.fieldOf("step").forGetter(AddOreModifier::step),
                ModOres.CODEC.fieldOf("ore").forGetter(AddOreModifier::ore)
        ).apply(builder, AddOreModifier::new));
    }
}
