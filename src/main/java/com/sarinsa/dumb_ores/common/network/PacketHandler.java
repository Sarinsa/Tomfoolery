package com.sarinsa.dumb_ores.common.network;

import com.sarinsa.dumb_ores.common.core.DumbOres;
import com.sarinsa.dumb_ores.common.network.message.S2CUpdateEntityCactusAttract;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_NAME = "DUMB_ORES";
    public static final SimpleChannel CHANNEL = createChannel();

    private int messageIndex;

    private static SimpleChannel createChannel() {
        return NetworkRegistry.ChannelBuilder
                .named(DumbOres.resourceLoc("channel"))
                .serverAcceptedVersions(PROTOCOL_NAME::equals)
                .clientAcceptedVersions(PROTOCOL_NAME::equals)
                .networkProtocolVersion(() -> PROTOCOL_NAME)
                .simpleChannel();
    }

    public void registerMessages() {
        CHANNEL.registerMessage(messageIndex++, S2CUpdateEntityCactusAttract.class, S2CUpdateEntityCactusAttract::encode, S2CUpdateEntityCactusAttract::decode, S2CUpdateEntityCactusAttract::handle);
    }

    /**
     * Sends the specified message to the client.
     *
     * @param message The message to send to the client.
     * @param player The player client that should receive this message.
     * @param <MSG> Packet type.
     */
    public static <MSG> void sendToClient(MSG message, ServerPlayerEntity player) {
        CHANNEL.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
    }
}
