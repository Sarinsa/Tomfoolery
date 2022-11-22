package com.sarinsa.dumb_ores.common.core;

import com.sarinsa.dumb_ores.common.capability.TomCapabilities;
import com.sarinsa.dumb_ores.common.core.config.TomClientConfig;
import com.sarinsa.dumb_ores.common.core.registry.*;
import com.sarinsa.dumb_ores.common.core.registry.types.GrenadeType;
import com.sarinsa.dumb_ores.common.event.BiomeEvents;
import com.sarinsa.dumb_ores.common.event.CapabilityEvents;
import com.sarinsa.dumb_ores.common.event.EntityEvents;
import com.sarinsa.dumb_ores.common.network.PacketHandler;
import com.sarinsa.dumb_ores.common.tags.TomItemTags;
import com.sarinsa.dumb_ores.common.worldgen.TomConfiguredFeatures;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;


@Mod(Tomfoolery.MODID)
public class Tomfoolery {

    public static final String MODID = "tomfoolery";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private final PacketHandler packetHandler = new PacketHandler();


    static {
        TomItemTags.init();
    }


    public Tomfoolery() {
        packetHandler.registerMessages();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(new BiomeEvents());
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

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, TomClientConfig.CLIENT_SPEC);
    }


    public void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            TomCapabilities.registerCapabilities();
            TomConfiguredFeatures.registerFeatures();
            TomPotions.registerBrewingRecipes();
            TomEntities.registerEntitySpawnPlacement();
        });
    }

    public static ResourceLocation resourceLoc(String path) {
        return new ResourceLocation(MODID, path);
    }
}
