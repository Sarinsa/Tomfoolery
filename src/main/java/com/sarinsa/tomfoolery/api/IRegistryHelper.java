package com.sarinsa.tomfoolery.api;


import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

/**
 * Helper interface for registering various things
 * to Tomfoolery.
 */
public interface IRegistryHelper {

    /**
     * Registers the given ILauncherLogic instance to be associated with
     * the given item, making the item a valid ammo type for the grenade launcher.<br>
     * <br>
     * If override is set to true, any existing launcher logic registered for the given item
     * will be replaced.
     */
    void registerLauncherLogic(Item item, ILauncherLogic launcherLogic, boolean override);

    /**
     * @return Tomfoolery's registry for grenade types. Unlike registering launcher logic for an item,
     * this registry allows adding custom grenade rounds that will use the grenade entity projectile.
     */
    Supplier<IForgeRegistry<GrenadeType>> getGrenadeTypeRegistry();
}
