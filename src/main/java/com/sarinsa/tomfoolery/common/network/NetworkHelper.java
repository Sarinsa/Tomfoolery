package com.sarinsa.tomfoolery.common.network;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.network.message.S2CUpdateEntityCactusAttract;
import com.sarinsa.tomfoolery.common.util.NBTHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;

public class NetworkHelper {

    public static void updateEntityCactusAttract(ServerPlayer player, LivingEntity entity, boolean marked) {
        PacketHandler.sendToClient(new S2CUpdateEntityCactusAttract(marked, entity.getId()), player);
    }

    public static void updateEntityCactusAttract(ServerPlayer player, LivingEntity entity) {
        PacketHandler.sendToClient(new S2CUpdateEntityCactusAttract(NBTHelper.isEntityCactusMarked(entity), entity.getId()), player);
    }
}
