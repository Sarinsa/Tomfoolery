package com.teamaurora.orespawn.common.core;

import com.teamaurora.orespawn.common.core.registry.OrespawnBlocks;
import com.teamaurora.orespawn.common.core.registry.OrespawnItems;
import com.teamaurora.orespawn.common.event.BiomeEvents;
import com.teamaurora.orespawn.common.worldgen.OrespawnConfiguredFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(Orespawn.MODID)
public class Orespawn {

    public static final String MODID = "orespawn";
    private static final Logger LOGGER = LogManager.getLogger(MODID);

    public Orespawn() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new BiomeEvents());

        OrespawnBlocks.BLOCKS.register(eventBus);
        OrespawnItems.ITEMS.register(eventBus);
    }

    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            OrespawnConfiguredFeatures.registerFeatures();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
