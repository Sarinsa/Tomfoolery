package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class GrenadeRoundEntity extends ProjectileEntity implements IEntityAdditionalSpawnData {

    private BlockPos initialPos = BlockPos.ZERO;
    private GrenadeType grenadeType = TomGrenadeTypes.EXPLOSIVE.get();

    public GrenadeRoundEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public GrenadeRoundEntity(double x, double y, double z, World world) {
        this(TomEntities.GRENADE_ROUND.get(), world);
        moveTo(x, y, z, yRot, xRot);
        initialPos = new BlockPos(x, y, z);
        reapplyPosition();
    }

    public GrenadeRoundEntity(LivingEntity shooter, World world) {
        this(shooter.getX(), shooter.getEyeY(), shooter.getZ(), world);
        setOwner(shooter);
        setRot(shooter.yRot, shooter.xRot);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();

        RayTraceResult traceResult = ProjectileHelper.getHitResult(this, this::canHitEntity);
        boolean teleporting = false;

        if (traceResult.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockRayTraceResult)traceResult).getBlockPos();
            BlockState blockstate = level.getBlockState(blockpos);

            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                handleInsidePortal(blockpos);
                teleporting = true;
            }
            else if (blockstate.is(Blocks.END_GATEWAY)) {
                TileEntity tileentity = level.getBlockEntity(blockpos);

                if (tileentity instanceof EndGatewayTileEntity && EndGatewayTileEntity.canEntityTeleport(this)) {
                    ((EndGatewayTileEntity)tileentity).teleportEntity(this);
                }
                teleporting = true;
            }
        }

        if (traceResult.getType() != RayTraceResult.Type.MISS && !teleporting && !ForgeEventFactory.onProjectileImpact(this, traceResult)) {
            onHit(traceResult);
        }
        checkInsideBlocks();
        Vector3d deltaMovement = getDeltaMovement();
        double x = this.getX() + deltaMovement.x;
        double y = this.getY() + deltaMovement.y;
        double z = this.getZ() + deltaMovement.z;
        updateRotation();
        float motionScale;

        IParticleData traceParticle;

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
            Vector3d deltaMovement1 = getDeltaMovement();
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
    protected void onHit(RayTraceResult traceResult) {
        if (grenadeType == null)
            return;

        if (distanceToSqr(initialPos.getX(), initialPos.getY(), initialPos.getZ()) > grenadeType.getSafetyDist()) {
            RayTraceResult.Type resultType = traceResult.getType();

            if (resultType == RayTraceResult.Type.ENTITY) {
                grenadeType.onEntityImpact(this, level, (EntityRayTraceResult) traceResult);
            }
            else if (resultType == RayTraceResult.Type.BLOCK) {
                grenadeType.onBlockImpact(this, level, (BlockRayTraceResult) traceResult);
            }
            grenadeType.generalImpact(this, level, traceResult);
        }
        remove();
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);

        if (grenadeType != null) {
            compoundNBT.putString("GrenadeType", grenadeType.getRegistryName().toString());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);

        if (compoundNBT.contains("GrenadeType", Constants.NBT.TAG_STRING)) {
            ResourceLocation id = ResourceLocation.tryParse(compoundNBT.getString("GrenadeType"));

            if (id != null && TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().containsKey(id)) {
                grenadeType = TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get().getValue(id);
            }
            else {
                grenadeType = TomGrenadeTypes.EXPLOSIVE.get();
            }
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeResourceLocation(grenadeType == null ? new ResourceLocation("") : grenadeType.getRegistryName());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        ResourceLocation grenadeTypeId = additionalData.readResourceLocation();
        setGrenadeType(TomGrenadeTypes.getOrDefault(grenadeTypeId));
    }
}
