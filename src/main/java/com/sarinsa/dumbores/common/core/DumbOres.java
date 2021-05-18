package com.sarinsa.dumbores.common.core;

import com.sarinsa.dumbores.common.core.registry.*;
import com.sarinsa.dumbores.common.event.BiomeEvents;
import com.sarinsa.dumbores.common.worldgen.DOConfiguredFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(DumbOres.MODID)
public class DumbOres {

    public static final String MODID = "dumb_ores";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public DumbOres() {
        DOEntities.initTypes();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new BiomeEvents());

        eventBus.addListener(DOEntities::createEntityAttributes);

        DOBlocks.BLOCKS.register(eventBus);
        DOItems.ITEMS.register(eventBus);
        DOEntities.ENTITIES.register(eventBus);
        DOEffects.EFFECTS.register(eventBus);
        DOPotions.POTIONS.register(eventBus);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DOConfiguredFeatures.registerFeatures();
            DOPotions.registerBrewingRecipes();
            DOEntities.registerEntitySpawnPlacement();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
