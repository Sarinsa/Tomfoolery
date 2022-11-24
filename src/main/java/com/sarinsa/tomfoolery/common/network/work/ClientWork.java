package com.sarinsa.tomfoolery.common.network.work;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.network.message.S2CUpdateEntityCactusAttract;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class ClientWork {

    public static void handleCactusAttractUpdate(S2CUpdateEntityCactusAttract message) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player != null) {
            Entity entity = Minecraft.getInstance().player.clientLevel.getEntity(message.entityId);

            if (entity instanceof LivingEntity) {
                LivingEntity livingEntity = (LivingEntity) entity;
                CapabilityHelper.setCactusAttract(livingEntity, message.marked);
            }
        }
    }
}
