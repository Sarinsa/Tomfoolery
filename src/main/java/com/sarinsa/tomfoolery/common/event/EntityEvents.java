package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.core.registry.TomEffects;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.entity.living.ai.CreeperChestHideGoal;
import com.sarinsa.tomfoolery.common.item.CoolGlassesItem;
import com.sarinsa.tomfoolery.common.network.NetworkHelper;
import net.minecraft.block.ChestBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

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
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) event.getEntity();
            creeper.goalSelector.addGoal(4, new CreeperChestHideGoal(creeper, 1.0D, 30));
        }
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

            if (player.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof CoolGlassesItem) {
                CoolGlassesItem glasses = (CoolGlassesItem) player.getItemBySlot(EquipmentSlotType.HEAD).getItem();
                double range = glasses.getRange();

                Vector3d eyePosition = player.getEyePosition(1.0F);
                Vector3d viewVector = player.getViewVector(1.0F);
                Vector3d vector3d = eyePosition.add(viewVector.x * range, viewVector.y * range, viewVector.z * range);
                BlockRayTraceResult result = player.level.clip(new RayTraceContext(eyePosition, vector3d, RayTraceContext.BlockMode.OUTLINE, RayTraceContext.FluidMode.NONE, player));

                glasses.gaze(player, player.level, result);
            }
        }
    }

    @SubscribeEvent
    public void onChestOpen(PlayerContainerEvent.Open event) {
        if (event.getContainer() instanceof ChestContainer) {
            ChestContainer container = (ChestContainer) event.getContainer();

            if (container.getContainer() instanceof ChestTileEntity) {
                ChestTileEntity chest = (ChestTileEntity) container.getContainer();
                CompoundNBT data = chest.getTileData();

                if (data.contains(CreeperChestHideGoal.HIDDEN_CREEPER_TAG, Constants.NBT.TAG_BYTE)) {
                    if (data.getBoolean(CreeperChestHideGoal.HIDDEN_CREEPER_TAG)) {
                        data.putBoolean(CreeperChestHideGoal.HIDDEN_CREEPER_TAG, false);
                        BlockPos pos = chest.getBlockPos().above();
                        PlayerEntity player = event.getPlayer();

                        if (!player.level.isClientSide) {
                            ServerWorld serverWorld = (ServerWorld) player.level;
                            EntityType.CREEPER.spawn(serverWorld, null, null, player, pos, SpawnReason.TRIGGERED, true, false);

                            Random random = player.level.random;

                            serverWorld.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 10, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.1D);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onChestDestroyed(BlockEvent.BreakEvent event) {
        if (event.isCanceled())
            return;

        PlayerEntity player = event.getPlayer();
        TileEntity te = player.level.getBlockEntity(event.getPos());

        if (te instanceof ChestTileEntity) {
            ChestTileEntity tileEntity = (ChestTileEntity) te;

            if (tileEntity.getTileData().contains(CreeperChestHideGoal.HIDDEN_CREEPER_TAG, Constants.NBT.TAG_BYTE)) {
                if (tileEntity.getTileData().getBoolean(CreeperChestHideGoal.HIDDEN_CREEPER_TAG)) {

                    if (!player.level.isClientSide) {
                        BlockPos pos = tileEntity.getBlockPos().above();
                        ServerWorld serverWorld = (ServerWorld) player.level;
                        EntityType.CREEPER.spawn(serverWorld, null, null, player, pos, SpawnReason.TRIGGERED, true, false);

                        Random random = player.level.random;

                        serverWorld.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 10, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.1D);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onCatInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof CatEntity) {
            CatEntity cat = (CatEntity) event.getTarget();

            if (cat.hasEffect(Effects.DAMAGE_BOOST)) {
                if (event.getItemStack().getItem() == Items.GOLDEN_APPLE) {
                    event.setCancellationResult(ActionResultType.CONSUME);

                    if (event.getWorld() instanceof ServerWorld) {
                        ServerWorld world = (ServerWorld) event.getWorld();
                        TomEntities.BUFFCAT.get().spawn(world, cat.saveWithoutId(new CompoundNBT()), null, event.getPlayer(), cat.blockPosition(), SpawnReason.TRIGGERED, true, false);
                        cat.remove();
                    }
                    Random random = event.getWorld().getRandom();
                    event.getWorld().playSound(event.getPlayer(), cat.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundCategory.NEUTRAL, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F );
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
