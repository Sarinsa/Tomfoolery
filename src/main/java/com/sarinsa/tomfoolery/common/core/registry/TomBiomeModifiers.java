package com.sarinsa.tomfoolery.common.core.registry;

import com.mojang.serialization.Codec;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.worldgen.biome.modifier.AddOreModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TomBiomeModifiers {

    public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Tomfoolery.MODID);


    public static final RegistryObject<Codec<AddOreModifier>> ADD_ORE = register("add_ore", AddOreModifier::create);


    private static <T extends BiomeModifier> RegistryObject<Codec<T>> register(String name, Supplier<Codec<T>> codecSupplier) {
        return BIOME_MODS.register(name, codecSupplier);
    }
}
