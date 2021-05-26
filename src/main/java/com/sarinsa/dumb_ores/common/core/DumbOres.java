package com.sarinsa.dumb_ores.common.core;

import com.sarinsa.dumb_ores.common.capability.DOCapabilities;
import com.sarinsa.dumb_ores.common.core.config.DOClientConfig;
import com.sarinsa.dumb_ores.common.core.registry.*;
import com.sarinsa.dumb_ores.common.event.BiomeEvents;
import com.sarinsa.dumb_ores.common.event.CapabilityEvents;
import com.sarinsa.dumb_ores.common.event.EntityEvents;
import com.sarinsa.dumb_ores.common.network.PacketHandler;
import com.sarinsa.dumb_ores.common.tags.DOItemTags;
import com.sarinsa.dumb_ores.common.worldgen.DOConfiguredFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(DumbOres.MODID)
public class DumbOres {

    public static final String MODID = "dumb_ores";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final PacketHandler packetHandler = new PacketHandler();

    static {
        DOItemTags.init();
    }

    public DumbOres() {
        DOEntities.initTypes();

        this.packetHandler.registerMessages();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new BiomeEvents());
        MinecraftForge.EVENT_BUS.register(new CapabilityEvents());
        MinecraftForge.EVENT_BUS.register(new EntityEvents());

        eventBus.addListener(DOEntities::createEntityAttributes);

        DOBlocks.BLOCKS.register(eventBus);
        DOItems.ITEMS.register(eventBus);
        DOEntities.ENTITIES.register(eventBus);
        DOEffects.EFFECTS.register(eventBus);
        DOPotions.POTIONS.register(eventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, DOClientConfig.CLIENT_SPEC);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DOCapabilities.registerCapabilities();
            DOConfiguredFeatures.registerFeatures();
            DOPotions.registerBrewingRecipes();
            DOEntities.registerEntitySpawnPlacement();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
