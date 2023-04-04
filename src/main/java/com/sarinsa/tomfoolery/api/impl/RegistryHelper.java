package com.sarinsa.tomfoolery.api.impl;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.api.IRegistryHelper;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.LaunchedTorchEntity;
import com.sarinsa.tomfoolery.common.item.GrenadeLauncherItem;
import com.sarinsa.tomfoolery.common.util.EntityHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class RegistryHelper implements IRegistryHelper {

    private ResourceLocation currentPluginId = null;

    @Override
    public void registerLauncherLogic(Item item, ILauncherLogic launcherLogic, boolean override) {
        if (item == null || launcherLogic == null) {
            Tomfoolery.LOGGER.warn("Mod plugin with ID {} attempted to register grenade launcher logic with either a missing item or missing logic instance.", currentPluginId == null ? "None :(" : currentPluginId);

            String regName = ForgeRegistries.ITEMS.containsValue(item) ? ForgeRegistries.ITEMS.getKey(item).toString() : "null";
            Tomfoolery.LOGGER.warn("Item type: {}", regName);
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

    @Override
    public Supplier<IForgeRegistry<GrenadeType>> getGrenadeTypeRegistry() {
        return TomGrenadeTypes.GRENADE_TYPE_REGISTRY;
    }

    public void registerDefaults() {
        registerLauncherLogic(
                Items.TORCH,
                (level, player, hand) -> {
                    LaunchedTorchEntity torchEntity = new LaunchedTorchEntity(player, level);
                    torchEntity.shootFromRotation(player, player.getXRot(), player.getYRot(), 2.5F, 2.5F, 2.5F);
                    level.addFreshEntity(torchEntity);
                },
                false
        );
    }

    public void setCurrentPluginId(ResourceLocation id) {
        currentPluginId = id;
    }
}
