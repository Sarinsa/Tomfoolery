package com.sarinsa.dumb_ores.common.core.registry;

import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.core.registry.types.GrenadeType;
import com.sarinsa.dumb_ores.common.item.GrenadeLauncherItem;
import com.sarinsa.dumb_ores.common.item.GrenadeRoundItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class TomItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Tomfoolery.MODID);

    public static final List<Supplier<GrenadeRoundItem>> GRENADE_AMMO = new ArrayList<>();

    public static final RegistryObject<Item> NETHERAIGHT_INGOT = registerItem("netheraight_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> GRENADE_LAUNCHER = registerItem("grenade_launcher", GrenadeLauncherItem::new);
    public static final RegistryObject<GrenadeRoundItem> EXPLOSIVE_GRENADE_ROUND = registerGrenadeAmmo(TomGrenadeTypes.EXPLOSIVE);
    public static final RegistryObject<GrenadeRoundItem> DOOM_GRENADE_ROUND = registerGrenadeAmmo(TomGrenadeTypes.DOOM);



    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static RegistryObject<Item> registerSimple(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new Item(properties));
    }

    private static RegistryObject<Item> registerSimple(String name, ItemGroup creativeTab) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().tab(creativeTab)));
    }

    protected static RegistryObject<GrenadeRoundItem> registerGrenadeAmmo(RegistryObject<GrenadeType> grenadeType) {
        String name = grenadeType.getId().getPath();
        RegistryObject<GrenadeRoundItem> regObj = ITEMS.register(name + "_grenade_round", () -> new GrenadeRoundItem(grenadeType));
        GRENADE_AMMO.add(regObj);
        return regObj;
    }
}
