package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEffects;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;


public class CactusBlockEntity extends Entity implements IEntityAdditionalSpawnData {
    
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId( CactusBlockEntity.class, EntityDataSerializers.BLOCK_POS );
    
    private LivingEntity followTarget;
    /** Makes sure the cactus entity doesn't revert to being a block the moment it is spawned */
    private int gracePeriod = 40;
    
    public CactusBlockEntity( EntityType<? extends CactusBlockEntity> entityType, Level level ) {
        super( entityType, level );
    }
    
    public CactusBlockEntity( Level level, LivingEntity followTarget, double x, double y, double z ) {
        super( TomEntities.CACTUS_BLOCK_ENTITY.get(), level );
        setPos( x, y, z );
        setFollowTarget( followTarget );
        noCulling = true;
        setStartPos( blockPosition() );
    }
    
    @Override
    protected void defineSynchedData() {
        entityData.define( DATA_START_POS, BlockPos.ZERO );
    }
    
    public void setFollowTarget( LivingEntity livingEntity ) {
        followTarget = livingEntity;
    }
    
    public void setStartPos( BlockPos pos ) {
        entityData.set( DATA_START_POS, pos );
    }
    
    public BlockPos getStartPos() {
        return entityData.get( DATA_START_POS );
    }
    
    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }
    
    @Override
    public void tick() {
        super.tick();
        
        // Tick grace period
        if( gracePeriod > 0 )
            --gracePeriod;
        
        // Move towards target, if target exists
        if( followTarget != null && followTarget.isAlive() && followTarget.hasEffect( TomEffects.CACTUS_ATTRACTION.get() ) ) {
            final boolean intersects = followTarget.getBoundingBox().intersects( getBoundingBox() );
            final Vec3 vec = intersects
                    ? new Vec3( 0, 0, 0 )
                    : new Vec3(
                    followTarget.getX() - getX(),
                    (followTarget.getY() + (followTarget.getBbHeight() / 2)) - getY(),
                    followTarget.getZ() - getZ()
            ).normalize().scale( followTarget.getBoundingBox().intersects( getBoundingBox() ) ? 0.005 : 0.3 );
            setDeltaMovement( vec );
            
            // Check if target is too far away to follow
            if( distanceToSqr( followTarget ) > 600 )
                followTarget = null;
        }
        else {
            if( !isNoGravity() ) {
                setDeltaMovement( getDeltaMovement().add( 0.0D, -0.04D, 0.0D ) );
            }
        }
        move( MoverType.SELF, getDeltaMovement() );
        // noinspection resource
        final Level level = level();
        
        // Hurt all living entities collided with
        for( LivingEntity livingEntity : level.getEntitiesOfClass( LivingEntity.class, getBoundingBox().inflate( 1.2D ) ) ) {
            if( getBoundingBox().intersects( livingEntity.getBoundingBox() ) ) {
                livingEntity.hurt( level.damageSources().cactus(), 1.0F );
            }
        }
        // Check if we are on the ground and should "solidify"
        if( !level.isClientSide && onGround() && gracePeriod <= 0 ) {
            level.setBlock( blockPosition(), Blocks.CACTUS.defaultBlockState(), Block.UPDATE_CLIENTS );
            discard();
        }
    }
    
    @Override
    public boolean isPushable() {
        return true;
    }
    
    @Override
    public boolean canChangeDimensions() {
        return false;
    }
    
    @Override
    public boolean isAttackable() {
        return false;
    }
    
    @Override
    protected void readAdditionalSaveData( CompoundTag compoundTag ) {
        // noinspection resource
        Entity entity = level().getEntity( compoundTag.getInt( "FollowTarget" ) );
        
        if( entity instanceof LivingEntity ) {
            followTarget = (LivingEntity) entity;
        }
    }
    
    @Override
    protected void addAdditionalSaveData( CompoundTag compoundTag ) {
        compoundTag.putInt( "FollowTarget", followTarget == null ? getId() : followTarget.getId() );
    }
    
    @Override
    public boolean displayFireAnimation() {
        return false;
    }
    
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket( this );
    }
    
    @Override
    public void writeSpawnData( FriendlyByteBuf buffer ) {
        buffer.writeInt( followTarget == null ? getId() : followTarget.getId() );
    }
    
    @Override
    public void readSpawnData( FriendlyByteBuf additionalData ) {
        // noinspection resource
        Entity entity = level().getEntity( additionalData.readInt() );
        
        if( entity instanceof LivingEntity ) {
            followTarget = (LivingEntity) entity;
        }
    }
}
