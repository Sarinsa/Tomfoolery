package com.sarinsa.tomfoolery.common.capability;

import com.sarinsa.tomfoolery.common.capability.cactus.CactusAttractCapabilityStorage;
import com.sarinsa.tomfoolery.common.capability.cactus.DefaultCactusAttractCapability;
import com.sarinsa.tomfoolery.common.capability.cactus.ICactusAttractCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class TomCapabilities {

    @CapabilityInject(value = ICactusAttractCapability.class)
    public static final Capability<ICactusAttractCapability> CACTUS_ATTRACT_CAPABILITY = null;


    public static void registerCapabilities() {
        CapabilityManager.INSTANCE.register(ICactusAttractCapability.class, new CactusAttractCapabilityStorage(), DefaultCactusAttractCapability::new);
    }
}
