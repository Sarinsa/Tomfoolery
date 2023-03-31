package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class InstaSaplingEntity extends Projectile implements IEntityAdditionalSpawnData, ItemSupplier {

    private static final EntityDataAccessor<ItemStack> ITEM_STACK = SynchedEntityData.defineId(InstaSaplingEntity.class, EntityDataSerializers.ITEM_STACK);

    private AbstractTreeGrower tree;

    public InstaSaplingEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public InstaSaplingEntity(double x, double y, double z, Level level, AbstractTreeGrower tree) {
        this(TomEntities.INSTA_SAPLING.get(), level);
        moveTo(x, y, z, getYRot(), getXRot());
        reapplyPosition();
        this.tree = tree;
    }

    public InstaSaplingEntity(LivingEntity shooter, Level level, AbstractTreeGrower tree) {
        this(shooter.getX(), shooter.getEyeY(), shooter.getZ(), level, tree);
        setOwner(shooter);
        setRot(shooter.getYRot(), shooter.getXRot());
    }

    @Override
    protected void defineSynchedData() {
        entityData.define(ITEM_STACK, new ItemStack(Items.OAK_SAPLING));
    }

    public void setItem(ItemStack itemStack) {
        entityData.set(ITEM_STACK, itemStack);
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
                BlockEntity blockEntity = level.getBlockEntity(blockpos);

                if (blockEntity instanceof TheEndGatewayBlockEntity && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(level, blockpos, blockstate, this, (TheEndGatewayBlockEntity) blockEntity);
                }
                teleporting = true;
            }
        }

        if (hitResult.getType() != HitResult.Type.MISS && !teleporting && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
            onHit(hitResult);
        }
        checkInsideBlocks();
        Vec3 deltaMovement = getDeltaMovement();
        double x = getX() + deltaMovement.x;
        double y = getY() + deltaMovement.y;
        double z = getZ() + deltaMovement.z;
        updateRotation();
        float motionScale;

        SimpleParticleType traceParticle;

        if (isInWater()) {
            traceParticle = ParticleTypes.BUBBLE;
            motionScale = 0.8F;
        }
        else {
            traceParticle = ParticleTypes.CLOUD;
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

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (result.getBlockPos().getY() < 3) {
            discard();
            return;
        }

        level.setBlock(result.getBlockPos(), Blocks.DIRT.defaultBlockState(), 2);

        if (getItem().getItem() == Items.DARK_OAK_SAPLING) {
            level.setBlock(result.getBlockPos().north(), Blocks.DIRT.defaultBlockState(), 2);
            level.setBlock(result.getBlockPos().west(), Blocks.DIRT.defaultBlockState(), 2);
            level.setBlock(result.getBlockPos().north().west(), Blocks.DIRT.defaultBlockState(), 2);
        }
        if (!level.isClientSide && tree != null) {
            ServerLevel serverLevel = (ServerLevel) level;
            tree.growTree(serverLevel, serverLevel.getChunkSource().getGenerator(), result.getBlockPos().above(), level.getBlockState(result.getBlockPos().above()), serverLevel.random);
        }
        discard();
    }

    protected double getGravity() {
        return 0.08D;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {

    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {

    }

    @Override
    public ItemStack getItem() {
        return entityData.get(ITEM_STACK);
    }
}
