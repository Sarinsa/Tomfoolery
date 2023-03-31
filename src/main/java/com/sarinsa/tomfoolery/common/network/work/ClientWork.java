package com.sarinsa.tomfoolery.common.network.work;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.network.message.S2CUpdateEntityCactusAttract;
import com.sarinsa.tomfoolery.common.util.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class ClientWork {

    public static void handleCactusAttractUpdate(S2CUpdateEntityCactusAttract message) {
        LocalPlayer player = Minecraft.getInstance().player;

        if (player != null) {
            Entity entity = Minecraft.getInstance().player.clientLevel.getEntity(message.entityId);

            if (entity instanceof LivingEntity livingEntity) {
                NBTHelper.markEntityCactusAttr(livingEntity, message.marked);
            }
        }
    }
}
