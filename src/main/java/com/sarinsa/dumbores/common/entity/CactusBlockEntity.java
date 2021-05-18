package com.sarinsa.dumbores.common.entity;

import com.sarinsa.dumbores.common.core.registry.DOEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
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
        super(DOEntities.CACTUS_BLOCK_ENTITY.get(), world);
        this.setPos(x, y, z);
        this.setFollowTarget(followTarget);
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

        for (LivingEntity livingEntity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(1.2D))) {
            if (this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
                livingEntity.hurt(DamageSource.CACTUS, 1.0F);
            }
        }
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
