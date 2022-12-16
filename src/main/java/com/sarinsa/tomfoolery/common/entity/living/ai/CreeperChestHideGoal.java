package com.sarinsa.tomfoolery.common.entity.living.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class CreeperChestHideGoal extends MoveToBlockGoal {

    public static final String HIDDEN_CREEPER_TAG = "tomfooleryHasHiddenCreeper";

    /** Needed to prevent creepers popping out of chests jumping straight back in. */
    private int cooldown;

    public CreeperChestHideGoal(CreatureEntity creatureEntity, double speedMod, int range) {
        super(creatureEntity, speedMod, range);
        this.cooldown = 120;
    }

    @Override
    public boolean canUse() {
        if (cooldown > 0) {
            --cooldown;
        }
        return cooldown <= 0 && super.canUse();
    }

    @Override
    protected boolean isValidTarget(IWorldReader world, BlockPos pos) {
        if (world.getBlockEntity(pos) instanceof ChestTileEntity) {
            ChestTileEntity chest = (ChestTileEntity) world.getBlockEntity(pos);

            if (chest.getTileData().contains(HIDDEN_CREEPER_TAG, Constants.NBT.TAG_BYTE)) {
                return !chest.getTileData().getBoolean(HIDDEN_CREEPER_TAG);
            }
            else {
                return true;
            }
        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();

        if (isReachedTarget()) {
            ChestTileEntity tileEntity = (ChestTileEntity) mob.level.getBlockEntity(blockPos);
            tileEntity.getTileData().putBoolean(HIDDEN_CREEPER_TAG, true);

            Random random = mob.level.random;
            BlockPos mobPos = mob.blockPosition();

            if (mob.level instanceof ServerWorld) {
                ((ServerWorld)mob.level).sendParticles(ParticleTypes.CLOUD, mobPos.getX() + 0.5D, mobPos.getY() + 0.5D, mobPos.getZ() + 0.5D, 10, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.1D);
            }
            mob.remove();
        }
    }
}
