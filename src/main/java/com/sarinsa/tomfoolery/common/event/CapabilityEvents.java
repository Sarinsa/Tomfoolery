package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.capability.cactus.CactusAttractCapabilityProvider;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEvents {

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(Tomfoolery.resourceLoc("cactus_attract"), new CactusAttractCapabilityProvider());
        }
    }
}
