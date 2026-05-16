package com.sarinsa.tomfoolery.common.event;

import com.sarinsa.tomfoolery.common.core.config.TomConfig;
import com.sarinsa.tomfoolery.common.core.registry.TomEffects;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.entity.living.ai.GrenadeLauncherAttackGoal;
import com.sarinsa.tomfoolery.common.item.CoolGlassesItem;
import com.sarinsa.tomfoolery.common.network.NetworkHelper;
import com.sarinsa.tomfoolery.common.util.NBTUtil;
import fathertoast.crust.api.lib.DeferredAction;
import fathertoast.crust.api.lib.EnvironmentHelper;
import fathertoast.crust.api.lib.NBTHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EntityEventsListener {
    
    public static final String KEY_LAUNCHER_MOB = "TMFOOLRLauncherMob";
    
    
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
                }
            }
        }
    }
    
    @SubscribeEvent( priority = EventPriority.LOWEST )
    public void onEntityJoinLevel( EntityJoinLevelEvent event ) {
        if( event.getEntity() instanceof Mob mob && event.getLevel() instanceof ServerLevel serverLevel ) {
            if( serverLevel.getServer().isSameThread() ) {
                maybeMakeLauncherMob( mob, serverLevel );
            }
            else {
                DeferredAction.queue( () -> maybeMakeLauncherMob( mob, serverLevel ) );
            }
        }
    }
    
    /**
     * Checks if the given mob should be made into a "launcher mob"
     * by giving it a ridicu-launcher and an AI goal to "use" it.
     *
     * @return True if the mob was processed.
     * Returns false if the mob is in an unloaded location.
     */
    private static boolean maybeMakeLauncherMob( Mob mob, ServerLevel level ) {
        if( !EnvironmentHelper.isLoaded( level, mob.blockPosition() ) || mob.isRemoved() )
            return false;
        
        // If the mob already has the launcher item and is marked as
        // a launcher mob, give it the insaneo mode launcher goal.
        if( mob.getMainHandItem().is( TomItems.GRENADE_LAUNCHER.get() ) &&
                NBTHelper.containsNumber( mob.getPersistentData(), KEY_LAUNCHER_MOB ) ) {
            mob.goalSelector.addGoal( 1, new GrenadeLauncherAttackGoal( mob, 1.0D, true ) );
        }
        else {
            if( TomConfig.GENERAL.RIDICULAUNCHER.launcherWielders.contains( mob ) ) {
                if( TomConfig.GENERAL.RIDICULAUNCHER.launcherWielders.rollChance( mob, level.getRandom() ) ) {
                    mob.setItemSlot( EquipmentSlot.MAINHAND, new ItemStack( TomItems.GRENADE_LAUNCHER.get() ) );
                    mob.goalSelector.addGoal( 1, new GrenadeLauncherAttackGoal( mob, 1.0D, true ) );
                    mob.getPersistentData().putBoolean( KEY_LAUNCHER_MOB, true );
                }
            }
        }
        return true;
    }
    
    @SubscribeEvent( priority = EventPriority.LOW )
    public void onPlayerUpdate( LivingEvent.LivingTickEvent event ) {
        // noinspection resource
        if( event.getEntity() instanceof Player player && !player.level().isClientSide ) {
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
    
    // TODO - Maybe
    @SubscribeEvent
    public void onCatInteract( PlayerInteractEvent.EntityInteract event ) {
        /*
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
        
         */
    }
    
    private static void updateEntityCactusAttract( MobEffect effect, LivingEntity livingEntity, boolean marked ) {
        if( effect == TomEffects.CACTUS_ATTRACTION.get() ) {
            NBTUtil.markEntityCactusAttr( livingEntity, marked );
        }
    }
}
