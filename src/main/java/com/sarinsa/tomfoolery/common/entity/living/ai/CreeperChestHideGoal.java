package com.sarinsa.tomfoolery.common.entity.living.ai;


import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.ChestBlockEntity;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
public class CreeperChestHideGoal extends MoveToBlockGoal {

    public static final String HIDDEN_CREEPER_TAG = "tomfooleryHasHiddenCreeper";

    /** Needed to prevent creepers popping out of chests jumping straight back in. */
    private int cooldown;

    public CreeperChestHideGoal(Creeper creeper, double speedMod, int range) {
        super(creeper, speedMod, range);
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
    protected boolean isValidTarget(LevelReader world, BlockPos pos) {
        if (world.getExistingBlockEntity(pos) instanceof ChestBlockEntity chest) {
            if (chest.getPersistentData().contains(HIDDEN_CREEPER_TAG, Tag.TAG_BYTE)) {
                return !chest.getPersistentData().getBoolean(HIDDEN_CREEPER_TAG);
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
            ChestBlockEntity chest = (ChestBlockEntity) mob.level.getExistingBlockEntity(blockPos);
            chest.getPersistentData().putBoolean(HIDDEN_CREEPER_TAG, true);

            RandomSource random = mob.level.random;
            BlockPos mobPos = mob.blockPosition();

            if (mob.level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.CLOUD, mobPos.getX() + 0.5D, mobPos.getY() + 0.5D, mobPos.getZ() + 0.5D, 10, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.1D);
            }
            mob.discard();
        }
    }
}
