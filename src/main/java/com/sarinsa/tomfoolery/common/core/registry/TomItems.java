package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.item.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
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

    public static final RegistryObject<Item> NETHERAIGHT_HELMET = registerItem("netheraight_helmet", () -> new ArmorItem(TomArmorMaterial.NETHERAIGHT, EquipmentSlotType.HEAD, new Item.Properties()));
    public static final RegistryObject<Item> NETHERAIGHT_CHESTPLATE = registerItem("netheraight_chestplate", () -> new ArmorItem(TomArmorMaterial.NETHERAIGHT, EquipmentSlotType.CHEST, new Item.Properties()));
    public static final RegistryObject<Item> NETHERAIGHT_LEGGINGS = registerItem("netheraight_leggings", () -> new ArmorItem(TomArmorMaterial.NETHERAIGHT, EquipmentSlotType.LEGS, new Item.Properties()));
    public static final RegistryObject<Item> NETHERAIGHT_BOOTS = registerItem("netheraight_boots", () -> new ArmorItem(TomArmorMaterial.NETHERAIGHT, EquipmentSlotType.FEET, new Item.Properties()));

    public static final RegistryObject<Item> COOL_DIRT_GLASSES = registerItem("cool_dirt_glasses", () -> new CoolBlockReplacerGlassesItem(() -> Blocks.DIRT));

    public static final RegistryObject<SpawnEggItem> BUFFCAT_SPAWN_EGG = registerSpawnEgg(TomEntities.BUFFCAT, 0x4E7EA4, 0x714B34);


    private static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }

    private static RegistryObject<Item> registerSimple(String name, Item.Properties properties) {
        return ITEMS.register(name, () -> new Item(properties));
    }

    private static RegistryObject<Item> registerSimple(String name, ItemGroup creativeTab) {
        return ITEMS.register(name, () -> new Item(new Item.Properties().tab(creativeTab)));
    }

    private static <T extends LivingEntity> RegistryObject<SpawnEggItem> registerSpawnEgg(RegistryObject<EntityType<T>> registryObject, int primaryColor, int spotColor) {
        return ITEMS.register(registryObject.getId().getPath() + "_spawn_egg", () -> new ForgeSpawnEggItem(registryObject, primaryColor, spotColor, new Item.Properties().tab(ItemGroup.TAB_MISC)));
    }

    protected static RegistryObject<GrenadeRoundItem> registerGrenadeAmmo(RegistryObject<GrenadeType> grenadeType) {
        String name = grenadeType.getId().getPath();
        RegistryObject<GrenadeRoundItem> regObj = ITEMS.register(name + "_grenade_round", () -> new GrenadeRoundItem(grenadeType));
        GRENADE_AMMO.add(regObj);
        return regObj;
    }
}
