package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.core.config.TomCommonConfig;
import com.sarinsa.tomfoolery.common.core.registry.TomEffects;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.entity.living.Buffcat;
import com.sarinsa.tomfoolery.common.entity.living.ai.GrenadeLauncherAttackGoal;
import com.sarinsa.tomfoolery.common.item.CoolGlassesItem;
import com.sarinsa.tomfoolery.common.network.NetworkHelper;
import com.sarinsa.tomfoolery.common.util.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;

public class EntityEventsListener {
    
    @SubscribeEvent( priority = EventPriority.HIGH )
    public void onPotionEffectExpire( MobEffectEvent.Expired event ) {
        LivingEntity livingEntity = event.getEntity();
        
        if( event.getEffectInstance() == null )
            return;
        
        updateEntityCactusAttract( event.getEffectInstance().getEffect(), livingEntity, false );
    }
    
    @SubscribeEvent
    public void onPotionRemoved( MobEffectEvent.Remove event ) {
        LivingEntity livingEntity = event.getEntity();
        
        if( event.getEffectInstance() == null || !(livingEntity instanceof ServerPlayer) )
            return;
        
        updateEntityCactusAttract( event.getEffectInstance().getEffect(), livingEntity, false );
    }
    
    @SubscribeEvent( priority = EventPriority.HIGH )
    public void onPotionEffectAdded( MobEffectEvent.Added event ) {
        LivingEntity livingEntity = event.getEntity();
        
        updateEntityCactusAttract( event.getEffectInstance().getEffect(), livingEntity, true );
    }
    
    @SubscribeEvent
    public void onEntityJoinWorld( EntityJoinLevelEvent event ) {
        if( event.getEntity() instanceof LivingEntity livingEntity ) {
            // noinspection resource
            Level level = livingEntity.level();
            
            if( level.isLoaded( livingEntity.blockPosition() ) ) {
                if( !level.isClientSide ) {
                    ServerLevel serverLevel = (ServerLevel) level;
                    
                    for( ServerPlayer playerEntity : serverLevel.players() ) {
                        NetworkHelper.updateEntityCactusAttract( playerEntity, livingEntity );
                    }
                    
                    if( livingEntity instanceof Zombie zombie ) {
                        if( zombie.getItemBySlot( EquipmentSlot.MAINHAND ).getItem() == TomItems.GRENADE_LAUNCHER.get() ) {
                            Set<WrappedGoal> goals = zombie.goalSelector.getAvailableGoals();
                            boolean addGoal = true;
                            
                            for( WrappedGoal goal : goals ) {
                                if( goal.getGoal() instanceof GrenadeLauncherAttackGoal ) {
                                    addGoal = false;
                                    break;
                                }
                            }
                            if( addGoal ) {
                                zombie.goalSelector.addGoal( 1, new GrenadeLauncherAttackGoal( zombie, 1.0D, true ) );
                            }
                        }
                    }
                }
            }
        }
    }
    
    @SubscribeEvent( priority = EventPriority.LOWEST )
    public void onFinalizeSpawn( MobSpawnEvent.FinalizeSpawn event ) {
        if( event.getEntity() instanceof Zombie zombie ) {
            if( event.getLevel().getRandom().nextDouble() <= TomCommonConfig.COMMON.grenadeLauncherZombieChance.get() ) {
                zombie.setItemSlot( EquipmentSlot.MAINHAND, new ItemStack( TomItems.GRENADE_LAUNCHER.get() ) );
                zombie.goalSelector.addGoal( 1, new GrenadeLauncherAttackGoal( zombie, 1.0D, true ) );
            }
        }
    }
    
    @SubscribeEvent( priority = EventPriority.LOW )
    public void onPlayerUpdate( LivingEvent.LivingTickEvent event ) {
        if( event.getEntity() instanceof Player player ) {
            if( player.getItemBySlot( EquipmentSlot.HEAD ).getItem() instanceof CoolGlassesItem glasses ) {
                double range = glasses.getRange();
                
                Vec3 eyePosition = player.getEyePosition( 1.0F );
                Vec3 viewVector = player.getViewVector( 1.0F );
                Vec3 vector3d = eyePosition.add( viewVector.x * range, viewVector.y * range, viewVector.z * range );
                // noinspection resource
                BlockHitResult result = player.level().clip( new ClipContext( eyePosition, vector3d, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player ) );
                
                glasses.gaze( player, player.level(), result );
            }
        }
    }
    
    @SubscribeEvent
    public void onCatInteract( PlayerInteractEvent.EntityInteract event ) {
        if( event.getTarget() instanceof Cat cat ) {
            if( cat.hasEffect( MobEffects.DAMAGE_BOOST ) ) {
                if( event.getItemStack().getItem() == Items.GOLDEN_APPLE ) {
                    event.setCancellationResult( InteractionResult.CONSUME );
                    
                    if( event.getLevel() instanceof ServerLevel serverLevel ) {
                        Buffcat buffcat = TomEntities.BUFFCAT.get().spawn( serverLevel, null, event.getEntity(), cat.blockPosition(), MobSpawnType.TRIGGERED, true, false );
                        
                        if( buffcat != null ) {
                            buffcat.readAdditionalSaveData( cat.saveWithoutId( new CompoundTag() ) );
                        }
                        
                        cat.discard();
                    }
                    RandomSource random = event.getLevel().getRandom();
                    event.getLevel().playSound( event.getEntity(), cat.blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.NEUTRAL, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F );
                }
            }
        }
    }
    
    private static void updateEntityCactusAttract( MobEffect effect, LivingEntity livingEntity, boolean marked ) {
        if( effect == TomEffects.CACTUS_ATTRACTION.get() ) {
            NBTHelper.markEntityCactusAttr( livingEntity, marked );
        }
    }
}
