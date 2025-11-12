package com.sarinsa.tomfoolery.api.impl;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.api.IRegistryHelper;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.LaunchedTorch;
import com.sarinsa.tomfoolery.common.item.GrenadeLauncherItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Supplier;

public class RegistryHelper implements IRegistryHelper {
    
    private ResourceLocation currentPluginId = null;
    
    @Override
    public void registerLauncherLogic( Item item, ILauncherLogic launcherLogic, boolean override ) {
        if( item == null || launcherLogic == null ) {
            Tomfoolery.LOGGER.warn( "Mod plugin with ID {} attempted to register grenade launcher logic with either a missing item or missing logic instance.", currentPluginId == null ? "None :(" : currentPluginId );
            
            // noinspection ConstantConditions
            String regName = ForgeRegistries.ITEMS.containsValue( item ) ? ForgeRegistries.ITEMS.getKey( item ).toString() : "null";
            Tomfoolery.LOGGER.warn( "Item type: {}", regName );
            Tomfoolery.LOGGER.warn( "Logic instance: {}", launcherLogic == null ? "null" : launcherLogic );
            return;
        }
        
        if( override ) {
            GrenadeLauncherItem.LAUNCHER_LOGICS.put( item, launcherLogic );
        }
        else {
            if( !GrenadeLauncherItem.LAUNCHER_LOGICS.containsKey( item ) ) {
                GrenadeLauncherItem.LAUNCHER_LOGICS.put( item, launcherLogic );
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
                ( level, player, hand ) -> {
                    LaunchedTorch torchEntity = new LaunchedTorch( player, level );
                    torchEntity.shootFromRotation( player, player.getXRot(), player.getYRot(), 2.5F, 2.5F, 2.5F );
                    level.addFreshEntity( torchEntity );
                },
                false
        );
    }
    
    public void setCurrentPluginId( ResourceLocation id ) {
        currentPluginId = id;
    }
}
