package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.capability.CapabilityHelper;
import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class CactusBlockEntity extends Entity implements IEntityAdditionalSpawnData {

    private LivingEntity followTarget;
    /** Makes sure the cactus entity doesn't revert to being a block the moment it is spawned */
    private int gracePeriod = 40;

    public CactusBlockEntity(EntityType<? extends CactusBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public CactusBlockEntity(World world, LivingEntity followTarget, double x, double y, double z) {
        super(TomEntities.CACTUS_BLOCK_ENTITY.get(), world);
        this.setPos(x, y, z);
        this.setFollowTarget(followTarget);
        this.noCulling = true;
    }

    @Override
    protected void defineSynchedData() {
    }

    public void setFollowTarget(LivingEntity livingEntity) {
        this.followTarget = livingEntity;
    }

    @Override
    public void tick() {
        super.tick();

        if (gracePeriod > 0)
            --gracePeriod;

        if (followTarget != null && followTarget.isAlive() && CapabilityHelper.getCactusAttract(followTarget)) {
            double xMotion = followTarget.getX() - getX();
            double yMotion = (followTarget.getY() + followTarget.getEyeHeight()) - getY();
            double zMotion = followTarget.getZ() - getZ();

            Vector3d deltaMovement = new Vector3d(xMotion, yMotion, zMotion).normalize().scale(0.7);
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

        if (onGround && gracePeriod <= 0) {
            level.setBlock(blockPosition(), Blocks.CACTUS.defaultBlockState(), 3);
            remove();
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
    protected boolean isMovementNoisy() {
        return false;
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compoundNBT) {
        Entity entity = level.getEntity(compoundNBT.getInt("FollowTarget"));

        if (entity instanceof LivingEntity) {
            followTarget = (LivingEntity) entity;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putInt("FollowTarget", followTarget == null ? getId() : followTarget.getId());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean displayFireAnimation() {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(followTarget == null ? getId() : followTarget.getId());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        Entity entity = level.getEntity(additionalData.readInt());

        if (entity instanceof LivingEntity) {
            followTarget = (LivingEntity) entity;
        }
    }
}
