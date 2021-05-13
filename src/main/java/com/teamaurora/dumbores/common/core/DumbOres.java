package com.teamaurora.dumbores.common.core;

import com.teamaurora.dumbores.common.core.registry.DOBlocks;
import com.teamaurora.dumbores.common.core.registry.DOItems;
import com.teamaurora.dumbores.common.event.BiomeEvents;
import com.teamaurora.dumbores.common.worldgen.DOConfiguredFeatures;
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
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new BiomeEvents());

        DOBlocks.BLOCKS.register(eventBus);
        DOItems.ITEMS.register(eventBus);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DOConfiguredFeatures.registerFeatures();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
