package com.sarinsa.tomfoolery.common.network;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    
    private static final String PROTOCOL_NAME = "DUMB_ORES";
    public static final SimpleChannel CHANNEL = createChannel();
    
    private int messageIndex;
    
    private static SimpleChannel createChannel() {
        return NetworkRegistry.ChannelBuilder
                .named( Tomfoolery.rl( "channel" ) )
                .serverAcceptedVersions( PROTOCOL_NAME::equals )
                .clientAcceptedVersions( PROTOCOL_NAME::equals )
                .networkProtocolVersion( () -> PROTOCOL_NAME )
                .simpleChannel();
    }
    
    public void registerMessages() {
    }
    
    /**
     * Sends the specified message to the client.
     *
     * @param message The message to send to the client.
     * @param player  The player client that should receive this message.
     * @param <MSG>   Packet type.
     */
    public static <MSG> void sendToClient( MSG message, ServerPlayer player ) {
        CHANNEL.sendTo( message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT );
    }
}
