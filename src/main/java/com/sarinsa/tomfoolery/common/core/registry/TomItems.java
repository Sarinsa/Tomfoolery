package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.item.CoolBlockReplacingGlassesItem;
import com.sarinsa.tomfoolery.common.item.GrenadeLauncherItem;
import com.sarinsa.tomfoolery.common.item.GrenadeRoundItem;
import com.sarinsa.tomfoolery.common.item.TomArmorMaterial;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TomItems {
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create( ForgeRegistries.ITEMS, Tomfoolery.MODID );
    public static final Map<ResourceKey<CreativeModeTab>, List<RegistryObject<? extends Item>>> TAB_ITEMS = new HashMap<>();
    
    
    public static final List<Supplier<GrenadeRoundItem>> GRENADE_AMMO = new ArrayList<>();
    
    public static final RegistryObject<Item> NETHERAIGHT_INGOT = registerItem( "netheraight_ingot", () -> new Item( new Item.Properties() ) );
    public static final RegistryObject<Item> GRENADE_LAUNCHER = registerItem( "grenade_launcher", GrenadeLauncherItem::new, CreativeModeTabs.COMBAT );
    public static final RegistryObject<GrenadeRoundItem> EXPLOSIVE_GRENADE_ROUND = registerGrenadeAmmo( TomGrenadeTypes.EXPLOSIVE );
    public static final RegistryObject<GrenadeRoundItem> DOOM_GRENADE_ROUND = registerGrenadeAmmo( TomGrenadeTypes.DOOM );
    
    public static final RegistryObject<Item> NETHERAIGHT_HELMET = registerItem( "netheraight_helmet", () -> new ArmorItem( TomArmorMaterial.NETHERAIGHT, ArmorItem.Type.HELMET, new Item.Properties() ) );
    public static final RegistryObject<Item> NETHERAIGHT_CHESTPLATE = registerItem( "netheraight_chestplate", () -> new ArmorItem( TomArmorMaterial.NETHERAIGHT, ArmorItem.Type.CHESTPLATE, new Item.Properties() ) );
    public static final RegistryObject<Item> NETHERAIGHT_LEGGINGS = registerItem( "netheraight_leggings", () -> new ArmorItem( TomArmorMaterial.NETHERAIGHT, ArmorItem.Type.LEGGINGS, new Item.Properties() ) );
    public static final RegistryObject<Item> NETHERAIGHT_BOOTS = registerItem( "netheraight_boots", () -> new ArmorItem( TomArmorMaterial.NETHERAIGHT, ArmorItem.Type.BOOTS, new Item.Properties() ) );
    
    public static final RegistryObject<Item> COOL_DIRT_GLASSES = registerItem( "cool_dirt_glasses", () -> new CoolBlockReplacingGlassesItem( () -> Blocks.DIRT, "dirt" ), CreativeModeTabs.TOOLS_AND_UTILITIES );
    public static final RegistryObject<Item> COOL_STONE_GLASSES = registerItem( "cool_stone_glasses", () -> new CoolBlockReplacingGlassesItem( () -> Blocks.STONE, "stone" ), CreativeModeTabs.TOOLS_AND_UTILITIES );
    
    
    //public static final RegistryObject<ForgeSpawnEggItem> BUFFCAT_SPAWN_EGG = registerSpawnEgg(TomEntities.BUFFCAT, 0x4E7EA4, 0x714B34);
    
    @SafeVarargs
    protected static <T extends Item> RegistryObject<T> registerItem( String name, Supplier<T> itemSupplier, ResourceKey<CreativeModeTab>... creativeTabs ) {
        RegistryObject<T> regObj = ITEMS.register( name, itemSupplier );
        queueForCreativeTabs( regObj, creativeTabs );
        return regObj;
    }
    
    @SafeVarargs
    protected static RegistryObject<Item> registerSimple( String name, Item.Properties properties, ResourceKey<CreativeModeTab>... creativeTabs ) {
        RegistryObject<Item> regObj = ITEMS.register( name, () -> new Item( properties ) );
        queueForCreativeTabs( regObj, creativeTabs );
        return regObj;
    }
    
    @SafeVarargs
    protected static RegistryObject<Item> registerSimple( String name, ResourceKey<CreativeModeTab>... creativeTabs ) {
        RegistryObject<Item> regObj = ITEMS.register( name, () -> new Item( new Item.Properties() ) );
        queueForCreativeTabs( regObj, creativeTabs );
        return regObj;
    }
    
    private static <T extends Mob> RegistryObject<ForgeSpawnEggItem> registerSpawnEgg( RegistryObject<EntityType<T>> registryObject, int primaryColor, int spotColor ) {
        // noinspection ConstantConditions
        RegistryObject<ForgeSpawnEggItem> regObj = ITEMS.register( registryObject.getId().getPath() + "_spawn_egg",
                () -> new ForgeSpawnEggItem( registryObject, primaryColor, spotColor, new Item.Properties() ) );
        queueForCreativeTabs( regObj, CreativeModeTabs.SPAWN_EGGS );
        return regObj;
    }
    
    protected static RegistryObject<GrenadeRoundItem> registerGrenadeAmmo( RegistryObject<GrenadeType> grenadeType ) {
        // noinspection ConstantConditions
        String name = grenadeType.getId().getPath();
        RegistryObject<GrenadeRoundItem> regObj = ITEMS.register( name + "_grenade_round", () -> new GrenadeRoundItem( grenadeType ) );
        queueForCreativeTabs( regObj, CreativeModeTabs.COMBAT );
        GRENADE_AMMO.add( regObj );
        return regObj;
    }
    
    
    @SafeVarargs
    protected static void queueForCreativeTabs( RegistryObject<? extends Item> item, ResourceKey<CreativeModeTab>... creativeTabs ) {
        for( ResourceKey<CreativeModeTab> tab : creativeTabs ) {
            if( !TAB_ITEMS.containsKey( tab ) ) {
                List<RegistryObject<? extends Item>> list = new ArrayList<>();
                list.add( item );
                TAB_ITEMS.put( tab, list );
            }
            else {
                TAB_ITEMS.get( tab ).add( item );
            }
        }
    }
    
    /**
     * Called when creative tabs gets populated with items.
     */
    public static void onCreativeTabPopulate( BuildCreativeModeTabContentsEvent event ) {
        if( TAB_ITEMS.containsKey( event.getTabKey() ) ) {
            List<RegistryObject<? extends Item>> items = TAB_ITEMS.get( event.getTabKey() );
            items.forEach( ( regObj ) -> event.accept( regObj.get() ) );
        }
    }
}
