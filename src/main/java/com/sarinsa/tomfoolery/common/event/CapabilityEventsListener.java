package com.sarinsa.tomfoolery.common.event;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityEventsListener {
    
    @SubscribeEvent
    public void onAttachCapability( AttachCapabilitiesEvent<Entity> event ) {
    
    }
}
