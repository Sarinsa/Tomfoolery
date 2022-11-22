package com.sarinsa.dumb_ores.common.core.registry;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.loot_mods.SimpleAddLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class TomLootMods {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, Tomfoolery.MODID);


    public static final RegistryObject<GlobalLootModifierSerializer<SimpleAddLootModifier>> SIMPLE_ADD = register("simple_add", SimpleAddLootModifier.Serializer::new);


    private static <T extends IGlobalLootModifier> RegistryObject<GlobalLootModifierSerializer<T>> register(String name, Supplier<GlobalLootModifierSerializer<T>> supplier) {
        return LOOT_MODIFIERS.register(name, supplier);
    }
}
