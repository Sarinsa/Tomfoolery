package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.core.registry.TomEffects;
import com.sarinsa.tomfoolery.common.item.CoolGlassesItem;
import com.sarinsa.tomfoolery.common.network.NetworkHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onPlayerUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();

            final double range = 50;

            if (player.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof CoolGlassesItem) {
                CoolGlassesItem glasses = (CoolGlassesItem) player.getItemBySlot(EquipmentSlotType.HEAD).getItem();
                Vector3d eyePosition = player.getEyePosition(1.0F);
                Vector3d viewVector = player.getViewVector(1.0F);
                Vector3d vector3d2 = eyePosition.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
                BlockRayTraceResult result = player.level.clip(new RayTraceContext(eyePosition, vector3d2, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));
                glasses.gaze(player, player.level, result);
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
