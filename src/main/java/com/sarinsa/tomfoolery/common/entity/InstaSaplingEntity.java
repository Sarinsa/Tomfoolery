package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRendersAsItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.EndGatewayTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IRendersAsItem.class
)
public class InstaSaplingEntity extends ProjectileEntity implements IEntityAdditionalSpawnData, IRendersAsItem {

    private static final DataParameter<ItemStack> ITEM_STACK = EntityDataManager.defineId(InstaSaplingEntity.class, DataSerializers.ITEM_STACK);

    private Tree tree;

    public InstaSaplingEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public InstaSaplingEntity(double x, double y, double z, World world, Tree tree) {
        this(TomEntities.INSTA_SAPLING.get(), world);
        moveTo(x, y, z, yRot, xRot);
        reapplyPosition();
        this.tree = tree;
    }

    public InstaSaplingEntity(LivingEntity shooter, World world, Tree tree) {
        this(shooter.getX(), shooter.getEyeY(), shooter.getZ(), world, tree);
        setOwner(shooter);
        setRot(shooter.yRot, shooter.xRot);
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
        double x = getX() + deltaMovement.x;
        double y = getY() + deltaMovement.y;
        double z = getZ() + deltaMovement.z;
        updateRotation();
        float motionScale;

        IParticleData traceParticle;

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
            Vector3d deltaMovement1 = getDeltaMovement();
            setDeltaMovement(deltaMovement1.x, deltaMovement1.y - getGravity(), deltaMovement1.z);
        }
        setPos(x, y, z);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        super.onHitBlock(result);

        if (result.getBlockPos().getY() < 3) {
            remove();
            return;
        }

        level.setBlock(result.getBlockPos(), Blocks.DIRT.defaultBlockState(), 2);

        if (getItem().getItem() == Items.DARK_OAK_SAPLING) {
            level.setBlock(result.getBlockPos().north(), Blocks.DIRT.defaultBlockState(), 2);
            level.setBlock(result.getBlockPos().west(), Blocks.DIRT.defaultBlockState(), 2);
            level.setBlock(result.getBlockPos().north().west(), Blocks.DIRT.defaultBlockState(), 2);
        }
        if (!level.isClientSide && tree != null) {
            ServerWorld serverWorld = (ServerWorld) level;
            tree.growTree(serverWorld, serverWorld.getChunkSource().getGenerator(), result.getBlockPos().above(), level.getBlockState(result.getBlockPos().above()), serverWorld.random);
        }
        remove();
    }

    protected double getGravity() {
        return 0.08D;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {

    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {

    }

    @Override
    public ItemStack getItem() {
        return entityData.get(ITEM_STACK);
    }
}
