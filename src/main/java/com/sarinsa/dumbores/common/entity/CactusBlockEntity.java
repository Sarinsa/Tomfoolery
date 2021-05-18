package com.sarinsa.dumbores.common.entity;

import com.sarinsa.dumbores.common.core.registry.DOEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class CactusBlockEntity extends Entity {

    private LivingEntity followTarget;
    /** Makes sure the cactus entity doesn't revert to being a block the moment it is spawned */
    private int gracePeriod = 40;

    public CactusBlockEntity(EntityType<? extends CactusBlockEntity> entityType, World world) {
        super(entityType, world);
    }

    public CactusBlockEntity(World world, double x, double y, double z) {
        super(DOEntities.CACTUS_BLOCK_ENTITY.get(), world);
        this.setPos(x, y, z);
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

    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compoundNBT) {

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
}
