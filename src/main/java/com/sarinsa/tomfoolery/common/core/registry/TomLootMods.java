package com.sarinsa.tomfoolery.common.core.registry;

import com.mojang.serialization.Codec;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.loot_mods.SimpleAddLootModifier;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class TomLootMods {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Tomfoolery.MODID);


    public static final RegistryObject<Codec<SimpleAddLootModifier>> SIMPLE_ADD = register("simple_add", SimpleAddLootModifier.CODEC);


    private static <T extends Codec<? extends IGlobalLootModifier>> RegistryObject<T> register(String name, Supplier<T> supplier) {
        return LOOT_MODIFIERS.register(name, supplier);
    }
}
