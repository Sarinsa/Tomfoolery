package com.sarinsa.dumb_ores.common.capability;

import com.sarinsa.dumb_ores.common.capability.cactus.CactusAttractCapabilityStorage;
import com.sarinsa.dumb_ores.common.capability.cactus.DefaultCactusAttractCapability;
import com.sarinsa.dumb_ores.common.capability.cactus.ICactusAttractCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class DOCapabilities {

    @CapabilityInject(value = ICactusAttractCapability.class)
    public static final Capability<ICactusAttractCapability> CACTUS_ATTRACT_CAPABILITY = null;


    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(ICactusAttractCapability.class, new CactusAttractCapabilityStorage(), DefaultCactusAttractCapability::new);
    }
}
