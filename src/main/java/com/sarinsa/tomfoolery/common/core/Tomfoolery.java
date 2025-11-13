package com.sarinsa.tomfoolery.common.core;

import com.sarinsa.tomfoolery.api.ITomfooleryApi;
import com.sarinsa.tomfoolery.api.ITomfooleryPlugin;
import com.sarinsa.tomfoolery.api.TomfooleryPlugin;
import com.sarinsa.tomfoolery.api.impl.RegistryHelper;
import com.sarinsa.tomfoolery.api.impl.TomfooleryAPI;
import com.sarinsa.tomfoolery.common.core.config.TomConfig;
import com.sarinsa.tomfoolery.common.core.registry.*;
import com.sarinsa.tomfoolery.common.event.CapabilityEventsListener;
import com.sarinsa.tomfoolery.common.event.EntityEventsListener;
import com.sarinsa.tomfoolery.common.event.ServerEventListener;
import com.sarinsa.tomfoolery.common.network.PacketHandler;
import com.sarinsa.tomfoolery.common.worldgen.TomConfiguredFeatures;
import fathertoast.crust.api.config.common.ConfigManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;


@Mod( Tomfoolery.MODID )
public class Tomfoolery {
    
    public static final String MODID = "tomfoolery";
    public static final Logger LOGGER = LogManager.getLogger( MODID );
    
    @SuppressWarnings( "FieldCanBeLocal" )
    private final PacketHandler packetHandler = new PacketHandler();
    
    private final ITomfooleryApi api = new TomfooleryAPI();
    
    private final RegistryHelper registryHelper = new RegistryHelper();
    
    
    public Tomfoolery( FMLJavaModLoadingContext context ) {
        packetHandler.registerMessages();
        
        ConfigManager.create( "Tomfoolery", MODID );
        
        IEventBus eventBus = context.getModEventBus();
        
        eventBus.addListener( this::onCommonSetup );
        eventBus.addListener( this::onLoadComplete );
        
        MinecraftForge.EVENT_BUS.register( new CapabilityEventsListener() );
        MinecraftForge.EVENT_BUS.register( new EntityEventsListener() );
        MinecraftForge.EVENT_BUS.register( new ServerEventListener() );
        
        eventBus.addListener( TomEntities::createEntityAttributes );
        eventBus.addListener( TomEntities::registerEntitySpawnPlacement );
        eventBus.addListener( TomItems::onCreativeTabPopulate );
        
        TomBlocks.BLOCKS.register( eventBus );
        TomItems.ITEMS.register( eventBus );
        TomEntities.ENTITIES.register( eventBus );
        TomSounds.SOUNDS.register( eventBus );
        TomEffects.EFFECTS.register( eventBus );
        TomPotions.POTIONS.register( eventBus );
        TomGrenadeTypes.GRENADE_TYPES.register( eventBus );
        TomLootMods.LOOT_MODIFIERS.register( eventBus );
        TomBiomeModifiers.BIOME_MODS.register( eventBus );
        TomConfiguredFeatures.CF_REGISTRY.register( eventBus );
        TomConfiguredFeatures.P_REGISTRY.register( eventBus );
        TomDamageTypes.DAMAGE_TYPES.register( eventBus );
    }
    
    
    public void onCommonSetup( FMLCommonSetupEvent event ) {
        event.enqueueWork( TomPotions::registerBrewingRecipes );
    }
    
    public void onLoadComplete( FMLLoadCompleteEvent event ) {
        event.enqueueWork( () -> {
            registryHelper.registerDefaults();
            processPlugins();
            TomConfig.initialize();
        } );
    }
    
    @SuppressWarnings( "all" )
    private void processPlugins() {
        // Load mod plugins
        ModList.get().getAllScanData().forEach( scanData -> {
            scanData.getAnnotations().forEach( annotationData -> {
                
                // Look for classes annotated with @ApocalypsePlugin
                if( annotationData.annotationType().getClassName().equals( TomfooleryPlugin.class.getName() ) ) {
                    String modid = (String) annotationData.annotationData().getOrDefault( "modid", "" );
                    
                    if( ModList.get().isLoaded( modid ) || modid.isEmpty() ) {
                        try {
                            Class<?> pluginClass = Class.forName( annotationData.memberName() );
                            
                            if( ITomfooleryPlugin.class.isAssignableFrom( pluginClass ) ) {
                                ITomfooleryPlugin plugin = (ITomfooleryPlugin) pluginClass.newInstance();
                                registryHelper.setCurrentPluginId( plugin.getPluginId() );
                                plugin.onLoad( getApi() );
                                LOGGER.info( "Found Tomfoolery plugin at {} with plugin ID: {}", annotationData.memberName(), plugin.getPluginId() );
                            }
                        }
                        catch( Exception e ) {
                            LOGGER.error( "Failed to load Tomfoolery plugin at {}! Damn dag nabbit dang it!", annotationData.memberName() );
                            e.printStackTrace();
                        }
                    }
                }
            } );
        } );
    }
    
    public static ResourceLocation rl( @Nonnull String path ) {
        return ResourceLocation.fromNamespaceAndPath( MODID, path );
    }
    
    public ITomfooleryApi getApi() {
        return api;
    }
}
