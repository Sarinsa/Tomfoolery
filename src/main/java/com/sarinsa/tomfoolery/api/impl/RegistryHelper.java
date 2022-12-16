package com.sarinsa.tomfoolery.api.impl;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.api.IRegistryHelper;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.item.GrenadeLauncherItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

public class RegistryHelper implements IRegistryHelper {

    private ResourceLocation currentPluginId = null;

    @Override
    public void registerLauncherLogic(Item item, ILauncherLogic launcherLogic, boolean override) {
        if (item == null || launcherLogic == null) {
            Tomfoolery.LOGGER.warn("Mod plugin with ID {} attempted to register grenade launcher logic with either a missing item or missing logic instance.", currentPluginId == null ? "None :(" : currentPluginId);
            Tomfoolery.LOGGER.warn("Item type: {}", item == null ? "null" : item.getRegistryName());
            Tomfoolery.LOGGER.warn("Logic instance: {}", launcherLogic == null ? "null" : launcherLogic);
            return;
        }

        if (override) {
            GrenadeLauncherItem.LAUNCHER_LOGICS.put(item, launcherLogic);
        }
        else {
            if (!GrenadeLauncherItem.LAUNCHER_LOGICS.containsKey(item)) {
                GrenadeLauncherItem.LAUNCHER_LOGICS.put(item, launcherLogic);
            }
        }
    }


    public void registerDefaults() {

    }

    public void setCurrentPluginId(ResourceLocation id) {
        currentPluginId = id;
    }
}
