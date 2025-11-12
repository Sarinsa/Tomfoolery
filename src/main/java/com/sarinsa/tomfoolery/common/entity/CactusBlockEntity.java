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
        if( gracePeriod > 0 )
            --gracePeriod;
        
        if( followTarget != null && followTarget.isAlive() && followTarget.hasEffect( TomEffects.CACTUS_ATTRACTION.get() ) ) {
            Vec3 vec = new Vec3( followTarget.getX() - getX(), (followTarget.getY() + followTarget.getEyeHeight()) - getY(), followTarget.getZ() - getZ() );
            setDeltaMovement( vec.scale( 0.1D ) );
            
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
        
        for( LivingEntity livingEntity : level.getEntitiesOfClass( LivingEntity.class, getBoundingBox().inflate( 1.2D ) ) ) {
            if( getBoundingBox().intersects( livingEntity.getBoundingBox() ) ) {
                livingEntity.hurt( level.damageSources().cactus(), 1.0F );
            }
        }
        
        if( !level.isClientSide && onGround() && gracePeriod <= 0 ) {
            level.setBlock( blockPosition(), Blocks.CACTUS.defaultBlockState(), 3 );
            discard();
        }
        
        super.tick();
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
