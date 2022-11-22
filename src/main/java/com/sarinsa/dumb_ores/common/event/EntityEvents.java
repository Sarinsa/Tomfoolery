package com.sarinsa.dumb_ores.common.event;

import com.sarinsa.dumb_ores.common.capability.CapabilityHelper;
import com.sarinsa.dumb_ores.common.core.registry.TomEffects;
import com.sarinsa.dumb_ores.common.network.NetworkHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEvents {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPotionEffectExpire(PotionEvent.PotionExpiryEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getCommandSenderWorld();

        if (event.getPotionEffect() == null)
            return;

        updateEntityCactusAttract(event.getPotionEffect().getEffect(), world, livingEntity, false);
    }

    @SubscribeEvent
    public void onPotionRemoved(PotionEvent.PotionRemoveEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getCommandSenderWorld();

        if (event.getPotionEffect() == null || !(event.getEntityLiving() instanceof ServerPlayerEntity))
            return;

        updateEntityCactusAttract(event.getPotionEffect().getEffect(), world, livingEntity, false);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPotionEffectAdded(PotionEvent.PotionAddedEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        World world = livingEntity.getCommandSenderWorld();

        updateEntityCactusAttract(event.getPotionEffect().getEffect(), world, livingEntity, true);
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) event.getEntity();
            World world = livingEntity.getCommandSenderWorld();

            if (event.getWorld().isLoaded(livingEntity.blockPosition())) {
                if (!world.isClientSide) {
                    ServerWorld serverWorld = (ServerWorld) world;

                    for (ServerPlayerEntity playerEntity : serverWorld.players()) {
                        NetworkHelper.updateEntityCactusAttract(playerEntity, livingEntity);
                    }
                }
            }
        }
    }

    private static void updateEntityCactusAttract(Effect effect, World world, LivingEntity livingEntity, boolean marked) {
        if (effect == TomEffects.CACTUS_ATTRACTION.get()) {
            if (!world.isClientSide) {
                ServerWorld serverWorld = (ServerWorld) world;

                for (ServerPlayerEntity playerEntity : serverWorld.players()) {
                    CapabilityHelper.setCactusAttract(playerEntity, livingEntity, marked);
                }
            }
        }
    }
}
