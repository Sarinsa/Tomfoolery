package com.sarinsa.dumb_ores.common.event;

import com.sarinsa.dumb_ores.common.capability.cactus.CactusAttractCapabilityProvider;
import com.sarinsa.dumb_ores.common.core.DumbOres;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvents {

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(DumbOres.resourceLoc("cactus_attract"), new CactusAttractCapabilityProvider());
        }
    }
}
