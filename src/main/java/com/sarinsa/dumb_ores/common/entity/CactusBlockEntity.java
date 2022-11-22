package com.sarinsa.dumb_ores.common.entity;

import com.sarinsa.dumb_ores.common.capability.CapabilityHelper;
import com.sarinsa.dumb_ores.common.core.registry.TomEntities;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.DamageSource;
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

        if (this.gracePeriod > 0)
            --this.gracePeriod;

        if (this.followTarget != null && this.followTarget.isAlive() && CapabilityHelper.getCactusAttract(this.followTarget)) {
            double xMotion = this.followTarget.getX() - this.getX();
            double yMotion = (this.followTarget.getY() + this.followTarget.getEyeHeight()) - this.getY();
            double zMotion = this.followTarget.getZ() - this.getZ();

            this.setDeltaMovement(xMotion * 0.2D, yMotion * 0.2D, zMotion * 0.2D);

            if (this.distanceToSqr(this.followTarget) > 600) {
                this.followTarget = null;
            }
        }
        else {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
            }
        }
        this.move(MoverType.SELF, this.getDeltaMovement());

        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.2D))) {
            if (this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                livingEntity.hurt(DamageSource.CACTUS, 1.0F);
            }
        }

        if (this.onGround && this.gracePeriod <= 0) {
            this.level.setBlock(this.blockPosition(), Blocks.CACTUS.defaultBlockState(), 3);
            this.remove();
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
        Entity entity = this.level.getEntity(compoundNBT.getInt("FollowTarget"));

        if (entity instanceof LivingEntity) {
            this.followTarget = (LivingEntity) entity;
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {
        compoundNBT.putInt("FollowTarget", this.followTarget == null ? this.getId() : this.followTarget.getId());
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
        buffer.writeInt(this.followTarget == null ? this.getId() : this.followTarget.getId());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        Entity entity = this.level.getEntity(additionalData.readInt());

        if (entity instanceof LivingEntity) {
            this.followTarget = (LivingEntity) entity;
        }
    }
}
