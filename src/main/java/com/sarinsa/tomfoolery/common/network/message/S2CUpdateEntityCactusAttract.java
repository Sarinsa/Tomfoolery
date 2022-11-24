package com.sarinsa.tomfoolery.common.network.message;

import com.sarinsa.tomfoolery.common.network.work.ClientWork;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class S2CUpdateEntityCactusAttract {

    public final boolean marked;
    public final int entityId;

    public S2CUpdateEntityCactusAttract(boolean marked, int entityId) {
        this.marked = marked;
        this.entityId = entityId;
    }

    public static void handle(S2CUpdateEntityCactusAttract message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();

        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> ClientWork.handleCactusAttractUpdate(message));
        }
        context.setPacketHandled(true);
    }

    public static S2CUpdateEntityCactusAttract decode(PacketBuffer buffer) {
        return new S2CUpdateEntityCactusAttract(buffer.readBoolean(), buffer.readInt());
    }

    public static void encode(S2CUpdateEntityCactusAttract message, PacketBuffer buffer) {
        buffer.writeBoolean(message.marked);
        buffer.writeInt(message.entityId);
    }
}
