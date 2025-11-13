package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.core.config.TomConfig;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.entity.living.Ghastinator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.List;

public class ServerEventListener {
    
    private MinecraftServer server = null;
    
    private int timeNextGhastinatorSpawnCheck = 60;
    
    
    @SubscribeEvent
    public void onServerStarted( ServerStartedEvent event ) {
        server = event.getServer();
    }
    
    @SubscribeEvent
    public void onServerStopped( ServerStoppedEvent event ) {
        server = null;
    }
    
    @SubscribeEvent
    public void serverTick( TickEvent.ServerTickEvent event ) {
        if( event.phase == TickEvent.Phase.START ) {
            
            if( TomConfig.GENERAL.GHASTINATOR.spawnGhastinator.get() ) {
                if( --timeNextGhastinatorSpawnCheck <= 0 ) {
                    timeNextGhastinatorSpawnCheck = 900;
                    
                    if( server.overworld().getDifficulty() == Difficulty.PEACEFUL )
                        return;
                    
                    List<ServerPlayer> players = server.overworld().players();
                    Collections.shuffle( players );
                    
                    for( ServerPlayer player : players ) {
                        checkGhastinatorSpawn( server, player );
                    }
                }
            }
        }
    }
    
    private static void checkGhastinatorSpawn( MinecraftServer server, ServerPlayer player ) {
        ServerLevel level = server.overworld();
        List<Ghastinator> existingGhastinators = level.getEntitiesOfClass( Ghastinator.class, player.getBoundingBox().inflate( 200, 200, 200 ) );
        
        if( !existingGhastinators.isEmpty() ) return;
        
        final RandomSource random = player.getRandom();
        final int spawnY = 200;
        
        // Try 5 different random locations before giving up
        for( int i = 0; i < 4; i++ ) {
            int spawnX = (int) player.getX() + random.nextInt( 100 ) - random.nextInt( 100 );
            int spawnZ = (int) player.getZ() + random.nextInt( 100 ) - random.nextInt( 100 );
            
            BlockPos spawnPos = new BlockPos( spawnX, spawnY, spawnZ );
            
            if( TomConfig.GENERAL.GHASTINATOR.spawnConditions.getOrElseIfLoaded( level, spawnPos, 0 ) > 0 ) {
                if( !level.noCollision( TomEntities.GHASTINATOR.get().getAABB( (double) spawnPos.getX() + 0.5D, spawnPos.getY(), (double) spawnPos.getZ() + 0.5D ) ) ) {
                    return;
                }
                Ghastinator ghastinator = TomEntities.GHASTINATOR.get().create( level, null, null, spawnPos, MobSpawnType.EVENT, true, true );
                
                if( ghastinator != null ) {
                    level.addFreshEntity( ghastinator );
                    level.playSound( null, spawnPos, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 15.0F, 0.8F );
                }
            }
        }
    }
}
