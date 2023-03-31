package com.sarinsa.tomfoolery.common.core;

import com.sarinsa.tomfoolery.api.ITomfooleryApi;
import com.sarinsa.tomfoolery.api.ITomfooleryPlugin;
import com.sarinsa.tomfoolery.api.TomfooleryPlugin;
import com.sarinsa.tomfoolery.api.impl.RegistryHelper;
import com.sarinsa.tomfoolery.api.impl.TomfooleryAPI;
import com.sarinsa.tomfoolery.common.core.config.TomClientConfig;
import com.sarinsa.tomfoolery.common.core.registry.*;
import com.sarinsa.tomfoolery.common.event.CapabilityEvents;
import com.sarinsa.tomfoolery.common.event.EntityEvents;
import com.sarinsa.tomfoolery.common.network.PacketHandler;
import com.sarinsa.tomfoolery.common.tags.TomItemTags;
import com.sarinsa.tomfoolery.common.worldgen.TomConfiguredFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Tomfoolery.MODID)
public class Tomfoolery {

    public static final String MODID = "tomfoolery";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final PacketHandler packetHandler = new PacketHandler();
    private final ITomfooleryApi api = new TomfooleryAPI();
    private final RegistryHelper registryHelper = new RegistryHelper();


    static {
        TomItemTags.init();
    }


    public Tomfoolery() {
        packetHandler.registerMessages();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);
        eventBus.addListener(this::onLoadComplete);

        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
        MinecraftForge.EVENT_BUS.register(new EntityEvents());

        eventBus.addListener(TomEntities::createEntityAttributes);

        TomBlocks.BLOCKS.register(eventBus);
        TomItems.ITEMS.register(eventBus);
        TomEntities.ENTITIES.register(eventBus);
        TomSounds.SOUNDS.register(eventBus);
        TomEffects.EFFECTS.register(eventBus);
        TomPotions.POTIONS.register(eventBus);
        TomGrenadeTypes.GRENADE_TYPES.register(eventBus);
        TomLootMods.LOOT_MODIFIERS.register(eventBus);
        TomBiomeModifiers.BIOME_MODS.register(eventBus);
        TomConfiguredFeatures.CF_REGISTRY.register(eventBus);
        TomConfiguredFeatures.P_REGISTRY.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TomClientConfig.CLIENT_SPEC);
    }


    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TomPotions.registerBrewingRecipes();
            TomEntities.registerEntitySpawnPlacement();
        });
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            registryHelper.registerDefaults();
            processPlugins();
        });
    }

    private void processPlugins() {
        // Load mod plugins
        ModList.get().getAllScanData().forEach(scanData -> {
            scanData.getAnnotations().forEach(annotationData -> {

                // Look for classes annotated with @ApocalypsePlugin
                if (annotationData.annotationType().getClassName().equals(TomfooleryPlugin.class.getName())) {
                    String modid = (String) annotationData.annotationData().getOrDefault("modid", "");

                    if (ModList.get().isLoaded(modid) || modid.isEmpty()) {
                        try {
                            Class<?> pluginClass = Class.forName(annotationData.memberName());

                            if (ITomfooleryPlugin.class.isAssignableFrom(pluginClass)) {
                                ITomfooleryPlugin plugin = (ITomfooleryPlugin) pluginClass.newInstance();
                                registryHelper.setCurrentPluginId(plugin.getPluginId());
                                plugin.onLoad(getApi());
                                LOGGER.info("Found Tomfoolery plugin at {} with plugin ID: {}", annotationData.memberName(), plugin.getPluginId());
                            }
                        }
                        catch (Exception e) {
                            LOGGER.error("Failed to load Tomfoolery plugin at {}! Damn dag nabit damnit!", annotationData.memberName());
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }

    public ITomfooleryApi getApi() {
        return api;
    }
}
