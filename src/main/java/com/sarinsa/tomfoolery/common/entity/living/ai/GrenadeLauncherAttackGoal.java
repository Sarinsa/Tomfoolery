package com.sarinsa.tomfoolery.common.entity.living.ai;

import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomSounds;
import com.sarinsa.tomfoolery.common.entity.GrenadeRound;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.Path;

import java.util.EnumSet;

public class GrenadeLauncherAttackGoal extends Goal {
    
    protected final PathfinderMob mob;
    private final double speedModifier;
    private final boolean followingTargetEvenIfNotSeen;
    private Path path;
    private double pathedTargetX;
    private double pathedTargetY;
    private double pathedTargetZ;
    private int ticksUntilNextPathRecalculation;
    private int ticksUntilNextAttack;
    private final int attackInterval = 20;
    private long lastCanUseCheck;
    private int failedPathFindingPenalty = 0;
    private final boolean canPenalize = false;
    private int nadesShot = 0;
    private int cooldown = 0;
    
    public GrenadeLauncherAttackGoal( PathfinderMob mob, double speedModifier, boolean mustSee ) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        followingTargetEvenIfNotSeen = !mustSee;
        setFlags( EnumSet.of( Goal.Flag.MOVE, Goal.Flag.LOOK ) );
    }
    
    @Override
    public boolean canUse() {
        if( mob.getItemBySlot( EquipmentSlot.MAINHAND ).getItem() != TomItems.GRENADE_LAUNCHER.get() )
            return false;
        
        // noinspection resource
        long gameTime = mob.level().getGameTime();
        
        if( gameTime - lastCanUseCheck < 20L ) {
            return false;
        }
        else {
            lastCanUseCheck = gameTime;
            LivingEntity target = mob.getTarget();
            
            if( target == null ) {
                return false;
            }
            else if( !target.isAlive() ) {
                return false;
            }
            else {
                if( canPenalize ) {
                    if( --ticksUntilNextPathRecalculation <= 0 ) {
                        path = mob.getNavigation().createPath( target, 0 );
                        this.ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt( 7 );
                        return path != null;
                    }
                    else {
                        return true;
                    }
                }
                path = mob.getNavigation().createPath( target, 0 );
                
                if( path != null ) {
                    return true;
                }
                else {
                    return getAttackReachSqr( target ) >= mob.distanceToSqr( target.getX(), target.getY(), target.getZ() );
                }
            }
        }
    }
    
    @Override
    public boolean canContinueToUse() {
        if( mob.getItemBySlot( EquipmentSlot.MAINHAND ).getItem() != TomItems.GRENADE_LAUNCHER.get() )
            return false;
        
        LivingEntity target = mob.getTarget();
        
        if( target == null ) {
            return false;
        }
        else if( !target.isAlive() ) {
            return false;
        }
        else if( !followingTargetEvenIfNotSeen ) {
            return !mob.getNavigation().isDone();
        }
        else if( !mob.isWithinRestriction( target.blockPosition() ) ) {
            return false;
        }
        else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }
    }
    
    @Override
    public void start() {
        mob.getNavigation().moveTo( path, speedModifier );
        mob.setAggressive( true );
        ticksUntilNextPathRecalculation = 0;
        ticksUntilNextAttack = 0;
    }
    
    @Override
    public void stop() {
        LivingEntity livingentity = mob.getTarget();
        
        if( !EntitySelector.NO_CREATIVE_OR_SPECTATOR.test( livingentity ) ) {
            mob.setTarget( null );
        }
        mob.setAggressive( false );
        mob.getNavigation().stop();
    }
    
    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
    
    public void tick() {
        LivingEntity target = mob.getTarget();
        
        if( target != null ) {
            mob.getLookControl().setLookAt( target, 30.0F, 30.0F );
            double distanceForAttack = mob.getPerceivedTargetDistanceSquareForMeleeAttack( target );
            ticksUntilNextPathRecalculation = Math.max( ticksUntilNextPathRecalculation - 1, 0 );
            
            if( (followingTargetEvenIfNotSeen || mob.getSensing().hasLineOfSight( target ))
                    && ticksUntilNextPathRecalculation <= 0
                    && (pathedTargetX == 0.0D
                    && pathedTargetY == 0.0D
                    && pathedTargetZ == 0.0D
                    || target.distanceToSqr( pathedTargetX, pathedTargetY, pathedTargetZ ) >= 1.0D
                    || mob.getRandom().nextFloat() < 0.05F
            ) ) {
                pathedTargetX = target.getX();
                pathedTargetY = target.getY();
                pathedTargetZ = target.getZ();
                ticksUntilNextPathRecalculation = 4 + mob.getRandom().nextInt( 7 );
                
                if( canPenalize ) {
                    ticksUntilNextPathRecalculation += failedPathFindingPenalty;
                    
                    if( mob.getNavigation().getPath() != null ) {
                        Node finalPathPoint = mob.getNavigation().getPath().getEndNode();
                        
                        if( finalPathPoint != null && target.distanceToSqr( finalPathPoint.x, finalPathPoint.y, finalPathPoint.z ) < 1 )
                            failedPathFindingPenalty = 0;
                        else
                            failedPathFindingPenalty += 10;
                    }
                    else {
                        failedPathFindingPenalty += 10;
                    }
                }
                if( distanceForAttack > 1024.0D ) {
                    ticksUntilNextPathRecalculation += 10;
                }
                else if( distanceForAttack > 256.0D ) {
                    ticksUntilNextPathRecalculation += 5;
                }
                
                if( !mob.getNavigation().moveTo( target, speedModifier ) ) {
                    ticksUntilNextPathRecalculation += 15;
                }
                ticksUntilNextPathRecalculation = adjustedTickDelay( ticksUntilNextPathRecalculation );
            }
            ticksUntilNextAttack = Math.max( ticksUntilNextAttack - 1, 0 );
            checkAndPerformAttack( target, distanceForAttack );
        }
    }
    
    protected void checkAndPerformAttack( LivingEntity target, double distanceForAttack ) {
        if( cooldown > 0 ) {
            --cooldown;
        }
        else {
            double attackReach = this.getAttackReachSqr( target );
            
            if( distanceForAttack <= attackReach && ticksUntilNextAttack <= 0 ) {
                resetAttackCooldown();
                mob.swing( InteractionHand.MAIN_HAND );
                nadesShot++;
                
                GrenadeRound entity = new GrenadeRound( mob, mob.level() );
                entity.shootFromRotation( mob, mob.getXRot(), mob.getYRot(), 2.5F, 2.5F, 2.5F );
                entity.setGrenadeType( TomGrenadeTypes.EXPLOSIVE.get() );
                // noinspection resource
                mob.level().addFreshEntity( entity );
                // noinspection resource
                mob.level().playSound( null, mob.getX(), mob.getY(), mob.getZ(), TomSounds.LAUNCHER_THUMP.get(), SoundSource.MASTER, 1.0F, 1.0F );
                
                if( nadesShot >= 6 ) {
                    nadesShot = 0;
                    cooldown = 100;
                }
            }
        }
    }
    
    protected void resetAttackCooldown() {
        ticksUntilNextAttack = adjustedTickDelay( 40 );
    }
    
    protected boolean isTimeToAttack() {
        return ticksUntilNextAttack <= 0;
    }
    
    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }
    
    protected int getAttackInterval() {
        return adjustedTickDelay( 40 );
    }
    
    protected double getAttackReachSqr( LivingEntity target ) {
        return 512.0D;
    }
}
