package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class GrenadeRoundEntity extends Projectile implements IEntityAdditionalSpawnData {

    private BlockPos initialPos = BlockPos.ZERO;
    private GrenadeType grenadeType = TomGrenadeTypes.EXPLOSIVE.get();

    public GrenadeRoundEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public GrenadeRoundEntity(double x, double y, double z, Level level) {
        this(TomEntities.GRENADE_ROUND.get(), level);
        moveTo(x, y, z, getYRot(), getXRot());
        initialPos = new BlockPos(x, y, z);
        reapplyPosition();
    }

    public GrenadeRoundEntity(LivingEntity shooter, Level level) {
        this(shooter.getX(), shooter.getEyeY(), shooter.getZ(), level);
        setOwner(shooter);
        setRot(shooter.getYRot(), shooter.getXRot());
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        HitResult hitResult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        boolean teleporting = false;

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult)hitResult).getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);

            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                handleInsidePortal(blockpos);
                teleporting = true;
            }
            else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockEntity = level.getExistingBlockEntity(blockpos);

                if (blockEntity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(level, blockpos, level.getBlockState(blockpos), this, (TheEndGatewayBlockEntity) blockEntity);
                }
                teleporting = true;
            }
        }

        if (hitResult.getType() != HitResult.Type.MISS && !teleporting && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
            onHit(hitResult);
        }
        checkInsideBlocks();
        Vec3 deltaMovement = getDeltaMovement();
        double x = this.getX() + deltaMovement.x;
        double y = this.getY() + deltaMovement.y;
        double z = this.getZ() + deltaMovement.z;
        updateRotation();
        float motionScale;

        SimpleParticleType traceParticle;

        if (isInWater()) {
            traceParticle = ParticleTypes.BUBBLE;
            motionScale = 0.8F;
        }
        else {
            traceParticle = grenadeType.getTraceParticle();
            motionScale = 0.99F;
        }
        level.addParticle(traceParticle, x - deltaMovement.x * 0.25D, y - deltaMovement.y * 0.25D, z - deltaMovement.z * 0.25D, deltaMovement.x, deltaMovement.y, deltaMovement.z);
        setDeltaMovement(deltaMovement.scale(motionScale));

        if (!isNoGravity()) {
            Vec3 deltaMovement1 = getDeltaMovement();
            setDeltaMovement(deltaMovement1.x, deltaMovement1.y - getGravity(), deltaMovement1.z);
        }
        setPos(x, y, z);
    }

    protected double getGravity() {
        return 0.08D;
    }

    public void setGrenadeType(GrenadeType grenadeType) {
        this.grenadeType = grenadeType;
    }

    public GrenadeType getGrenadeType() {
        return grenadeType;
    }

    public BlockPos getInitialPos() {
        return initialPos;
    }

    @Override
    protected void onHit(HitResult traceResult) {
        if (grenadeType == null)
            return;

        if (distanceToSqr(initialPos.getX(), initialPos.getY(), initialPos.getZ()) > grenadeType.getSafetyDist()) {
            HitResult.Type resultType = traceResult.getType();

            if (resultType == HitResult.Type.ENTITY) {
                grenadeType.onEntityImpact(this, level, (EntityHitResult) traceResult);
            }
            else if (resultType == HitResult.Type.BLOCK) {
                grenadeType.onBlockImpact(this, level, (BlockHitResult) traceResult);
            }
            grenadeType.generalImpact(this, level, traceResult);
        }
        discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        if (grenadeType != null) {
            compoundTag.putString("GrenadeType", TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().getKey(grenadeType).toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);

        if (compoundTag.contains("GrenadeType", Tag.TAG_STRING)) {
            ResourceLocation id = ResourceLocation.tryParse(compoundTag.getString("GrenadeType"));

            if (id != null && TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().containsKey(id)) {
                grenadeType = TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().getValue(id);
            }
            else {
                grenadeType = TomGrenadeTypes.EXPLOSIVE.get();
            }
        }
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        if (grenadeType != null) {
            ResourceLocation id = TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().containsValue(grenadeType)
                    ? TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().getKey(grenadeType)
                    : new ResourceLocation("");

            buffer.writeResourceLocation(id);
        }
        else {
            buffer.writeResourceLocation(new ResourceLocation(""));
        }
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        ResourceLocation grenadeTypeId = additionalData.readResourceLocation();
        setGrenadeType(TomGrenadeTypes.getOrDefault(grenadeTypeId));
    }
}
