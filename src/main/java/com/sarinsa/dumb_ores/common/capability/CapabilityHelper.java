package com.sarinsa.dumb_ores.common.capability;

import com.sarinsa.dumb_ores.common.network.NetworkHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

@SuppressWarnings("all")
public class CapabilityHelper {

    public static boolean getCactusAttract(LivingEntity entity) {
        return entity.getCapability(TomCapabilities.CACTUS_ATTRACT_CAPABILITY).orElse(TomCapabilities.CACTUS_ATTRACT_CAPABILITY.getDefaultInstance()).getMarked();
    }

    public static void setCactusAttract(LivingEntity entity, boolean marked) {
        entity.getCapability(TomCapabilities.CACTUS_ATTRACT_CAPABILITY).orElse(TomCapabilities.CACTUS_ATTRACT_CAPABILITY.getDefaultInstance()).setMarked(marked);
    }

    public static void setCactusAttract(ServerPlayerEntity playerEntity, LivingEntity entity, boolean marked) {
        setCactusAttract(entity, marked);
        NetworkHelper.updateEntityCactusAttract(playerEntity, entity, marked);
    }
}
