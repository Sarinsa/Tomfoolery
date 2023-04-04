package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.util.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nonnull;


public class CactusBlockEntity extends Entity implements IEntityAdditionalSpawnData {

    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(CactusBlockEntity.class, EntityDataSerializers.BLOCK_POS);

    private LivingEntity followTarget;
    /** Makes sure the cactus entity doesn't revert to being a block the moment it is spawned */
    private int gracePeriod = 40;

    public CactusBlockEntity(EntityType<? extends CactusBlockEntity> entityType, Level level) {
        super(entityType, level);
    }

    public CactusBlockEntity(Level level, LivingEntity followTarget, double x, double y, double z) {
        super(TomEntities.CACTUS_BLOCK_ENTITY.get(), level);
        setPos(x, y, z);
        setFollowTarget(followTarget);
        noCulling = true;
        setStartPos(blockPosition());
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(DATA_START_POS, BlockPos.ZERO);
    }

    public void setFollowTarget(LivingEntity livingEntity) {
        followTarget = livingEntity;
    }

    public void setStartPos(BlockPos pos) {
        entityData.set(DATA_START_POS, pos);
    }

    public BlockPos getStartPos() {
        return entityData.get(DATA_START_POS);
    }

    @Nonnull
    @Override
    protected MovementEmission getMovementEmission() {
        return MovementEmission.NONE;
    }

    @Override
    public void tick() {
        super.tick();

        if (gracePeriod > 0)
            --gracePeriod;

        if (followTarget != null && followTarget.isAlive() && NBTHelper.isEntityCactusMarked(followTarget)) {
            double xMotion = followTarget.getX() - getX();
            double yMotion = (followTarget.getY() + followTarget.getEyeHeight()) - getY();
            double zMotion = followTarget.getZ() - getZ();

            Vec3 deltaMovement = new Vec3(xMotion, yMotion, zMotion).normalize().scale(0.7);
            setDeltaMovement(deltaMovement);

            if (distanceToSqr(followTarget) > 600) {
                followTarget = null;
            }
        }
        else {
            if (!isNoGravity()) {
                setDeltaMovement(getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }
        }
        move(MoverType.SELF, getDeltaMovement());

        for (LivingEntity livingEntity : level.getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(1.2D))) {
            if (getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                livingEntity.hurt(DamageSource.CACTUS, 1.0F);
            }
        }

        if (!level.isClientSide && onGround && gracePeriod <= 0) {
            level.setBlock(blockPosition(), Blocks.CACTUS.defaultBlockState(), 3);
            discard();
        }
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
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        Entity entity = level.getEntity(compoundTag.getInt("FollowTarget"));

        if (entity instanceof LivingEntity) {
            followTarget = (LivingEntity) entity;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        compoundTag.putInt("FollowTarget", followTarget == null ? getId() : followTarget.getId());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Nonnull
    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(followTarget == null ? getId() : followTarget.getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        Entity entity = level.getEntity(additionalData.readInt());

        if (entity instanceof LivingEntity) {
            followTarget = (LivingEntity) entity;
        }
    }
}
