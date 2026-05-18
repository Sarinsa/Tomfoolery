package com.sarinsa.tomfoolery.common.entity.living;

import com.sarinsa.tomfoolery.common.core.config.TomConfig;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.entity.HugeFireball;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;

public class Ghastinator extends Ghast {
    
    public Ghastinator( EntityType<? extends Ghast> entityType, Level level ) {
        super( TomEntities.GHASTINATOR.get(), level );
        lookControl = new LookControl( this ) {
            @Override
            protected boolean resetXRotOnTick() {
                return false;
            }
        };
    }
    
    public static AttributeSupplier.Builder createGhastinatorAttributes() {
        return Mob.createMobAttributes()
                .add( Attributes.MAX_HEALTH, 200.0D );
    }
    
    @Override
    protected void registerGoals() {
        goalSelector.addGoal( 1, new LookAtTargetGoal( this ) );
        goalSelector.addGoal( 3, new ShootFireballGoal( this ) );
        targetSelector.addGoal( 1, new NearestEnemyTargetGoal<>( this, Player.class, true ) );
    }
    
    @Override
    public boolean removeWhenFarAway( double distance ) {
        return false;
    }
    
    @Override
    public void checkDespawn() {
        super.checkDespawn();
        final Level level = level();
        
        if( !level.isClientSide ) {
            if( TomConfig.GENERAL.GHASTINATOR.despawnConditions.getOrElse( level, blockPosition(), 0 ) > 0 ) {
                level.playSound( null, blockPosition(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.HOSTILE, 15.0F, 0.5F );
                discard();
            }
        }
    }
    
    @Override
    public boolean hurt( DamageSource damageSource, float damage ) {
        // Allow killing via command
        return damageSource == damageSources().genericKill() && super.hurt( damageSource, damage );
    }
    
    @Override
    public int getAmbientSoundInterval() {
        return 200;
    }
    
    @Override
    public int getExplosionPower() {
        return TomConfig.GENERAL.GHASTINATOR.explosionPower.get();
    }
    
    @Override
    public float getVoicePitch() {
        return 0.35F;
    }
    
    @Override
    protected float getSoundVolume() {
        return 15F;
    }
    
    @Override
    public void push( Entity entity ) {
    
    }
    
    @Override
    public void push( double xPower, double yPower, double zPower ) {
    
    }
    
    @Override
    public int getMaxHeadXRot() {
        return 50;
    }
    
    @Override
    public AABB getBoundingBoxForCulling() {
        return getBoundingBox().inflate( 140, 140D, 140D );
    }
    
    @Override
    public boolean hasLineOfSight( Entity target ) {
        // noinspection resource
        if( target.level() != level() ) {
            return false;
        }
        else {
            Vec3 pos = new Vec3( getX(), getEyeY(), getZ() );
            Vec3 targetPos = new Vec3( target.getX(), target.getEyeY(), target.getZ() );
            
            if( targetPos.distanceTo( pos ) > 400.0D ) {
                return false;
            }
            else {
                // noinspection resource
                return level().clip( new ClipContext( pos, targetPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this ) ).getType() == HitResult.Type.MISS;
            }
        }
    }
    
    @Override
    protected float getStandingEyeHeight( Pose pose, EntityDimensions dimensions ) {
        return 55.0F;
    }
    
    static class ShootFireballGoal extends Goal {
        private final Ghastinator ghastinator;
        public int chargeTime;
        
        public ShootFireballGoal( Ghastinator ghastinator ) {
            this.ghastinator = ghastinator;
        }
        
        @Override
        public boolean canUse() {
            return ghastinator.getTarget() != null;
        }
        
        @Override
        public void start() {
            chargeTime = 0;
        }
        
        @Override
        public void stop() {
            ghastinator.setCharging( false );
        }
        
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        
        @Override
        public void tick() {
            LivingEntity livingentity = ghastinator.getTarget();
            
            if( livingentity != null ) {
                if( livingentity.distanceToSqr( ghastinator ) < 40000.0D && ghastinator.hasLineOfSight( livingentity ) ) {
                    Level level = ghastinator.level();
                    ++chargeTime;
                    
                    if( chargeTime == 10 && !ghastinator.isSilent() ) {
                        level.playSound( null, ghastinator.blockPosition(), SoundEvents.GHAST_WARN, SoundSource.HOSTILE, 15.0F, 0.35F );
                    }
                    
                    if( chargeTime == 30 ) {
                        Vec3 viewVec = ghastinator.getViewVector( 1.0F );
                        double x = livingentity.getX() - (ghastinator.getX() + viewVec.x * 4.0D);
                        double y = livingentity.getY( 0.5D ) - (0.5D + ghastinator.getY( 0.5D ));
                        double z = livingentity.getZ() - (ghastinator.getZ() + viewVec.z * 4.0D);
                        
                        if( !ghastinator.isSilent() ) {
                            level.playSound( null, ghastinator.blockPosition(), SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 15.0F, 0.35F );
                        }
                        HugeFireball fireball = new HugeFireball( level, ghastinator, x, y, z, ghastinator.getExplosionPower() );
                        fireball.setPos( ghastinator.getX() + viewVec.x * 16.0D, ghastinator.getY( 0.5D ) + 0.5D, fireball.getZ() + viewVec.z * 16.0D );
                        level.addFreshEntity( fireball );
                        chargeTime = -120;
                    }
                }
                else if( chargeTime > 0 ) {
                    --chargeTime;
                }
                ghastinator.setCharging( chargeTime > 10 );
            }
        }
    }
    
    private static class LookAtTargetGoal extends Goal {
        private final Ghastinator ghastinator;
        
        public LookAtTargetGoal( Ghastinator ghastinator ) {
            this.ghastinator = ghastinator;
            setFlags( EnumSet.of( Goal.Flag.LOOK ) );
        }
        
        @Override
        public boolean canUse() {
            return true;
        }
        
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
        
        @Override
        public void tick() {
            if( ghastinator.getTarget() != null ) {
                LivingEntity target = ghastinator.getTarget();
                
                if( target.distanceToSqr( ghastinator ) < 40000.0D ) {
                    if( target.isAlive() ) {
                        ghastinator.getLookControl().setLookAt( target.getX(), target.getEyeY(), target.getZ() );
                    }
                }
            }
        }
    }
    
    private static class NearestEnemyTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        
        public NearestEnemyTargetGoal( Mob mob, Class<T> targetClass, boolean mustSee ) {
            super( mob, targetClass, mustSee );
            targetConditions = TargetingConditions.forCombat().selector( null );
        }
        
        @Override
        protected double getFollowDistance() {
            return 300.0D;
        }
        
        protected void findTarget() {
            if( targetType != Player.class && targetType != ServerPlayer.class ) {
                return;
            }
            // noinspection resource
            final List<Player> nearbyPlayers = mob.level().getEntitiesOfClass( Player.class, mob.getBoundingBox().inflate( 200.0D, 300.0D, 200.0D ) );
            
            targetConditions = TomConfig.GENERAL.GHASTINATOR.ignoreInvisibility.get()
                    ? TargetingConditions.forCombat().selector( null )
                    : TargetingConditions.forCombat().range( 200.0D );
            // noinspection resource
            target = mob.level().getNearestEntity( nearbyPlayers, targetConditions, mob, mob.getX(), mob.getEyeY(), mob.getZ() );
            
            mob.setAggressive( target != null );
        }
    }
}
