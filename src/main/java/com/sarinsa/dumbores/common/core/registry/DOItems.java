package com.sarinsa.dumbores.common.core.registry;

import com.sarinsa.dumbores.common.core.DumbOres;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class DOItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DumbOres.MODID);

    public static final RegistryObject<Item> NETHERAIGHT_INGOT = registerItem("netheraight_ingot", () -> new Item(new Item.Properties()));


    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
}
