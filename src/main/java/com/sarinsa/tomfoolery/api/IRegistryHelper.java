package com.sarinsa.tomfoolery.api;

import net.minecraft.item.Item;

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
}
