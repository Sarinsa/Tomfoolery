package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TallGrassBlock;
import net.minecraft.world.level.block.WallTorchBlock;
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

public class LaunchedTorchEntity extends Projectile implements IEntityAdditionalSpawnData, ItemSupplier {

    private static final ItemStack renderedItem = new ItemStack(Items.TORCH);

    private BlockPos initialPos = BlockPos.ZERO;

    public LaunchedTorchEntity(EntityType<? extends Projectile> entityType, Level level) {
        super(entityType, level);
    }

    public LaunchedTorchEntity(double x, double y, double z, Level level) {
        this(TomEntities.LAUNCHED_TORCH.get(), level);
        moveTo(x, y, z, getYRot(), getXRot());
        initialPos = new BlockPos(x, y, z);
        reapplyPosition();
    }

    public LaunchedTorchEntity(LivingEntity shooter, Level level) {
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
        double x = getX() + deltaMovement.x;
        double y = getY() + deltaMovement.y;
        double z = getZ() + deltaMovement.z;
        updateRotation();

        setDeltaMovement(deltaMovement.scale(0.99F));

        if (!isNoGravity()) {
            Vec3 deltaMovement1 = getDeltaMovement();
            setDeltaMovement(deltaMovement1.x, deltaMovement1.y - getGravity(), deltaMovement1.z);
        }
        setPos(x, y, z);

        if (isInWater()) {
            discard();

            if (!level.isClientSide) {
                level.addFreshEntity(new ItemEntity(level, x, y, z, new ItemStack(Items.TORCH)));
            }
        }
    }

    protected double getGravity() {
        return 0.08D;
    }

    public BlockPos getInitialPos() {
        return initialPos;
    }

    @Override
    protected void onHitBlock(BlockHitResult hitResult) {
        BlockPos pos = hitResult.getBlockPos();
        Direction direction = hitResult.getDirection();
        boolean placed = false;

        if (direction == Direction.UP) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                level.setBlock(pos.relative(direction), Blocks.TORCH.defaultBlockState(), 3);
                placed = true;
            }
        }
        else if (direction != Direction.DOWN) {
            if (level.getBlockState(pos.relative(direction)).isAir()) {
                level.setBlock(pos.relative(direction), Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, direction), 3);
                placed = true;
            }
        }
        if (!level.isClientSide && !placed) {
            level.addFreshEntity(new ItemEntity(level, getX(), getY(), getZ(), new ItemStack(Items.TORCH)));
        }
        discard();
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        if (hitResult.getEntity() instanceof LivingEntity livingEntity && !livingEntity.getType().fireImmune()) {
            livingEntity.setSecondsOnFire(3);
        }
        discard();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
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
        return renderedItem;
    }
}
