package com.sarinsa.dumbores.common.effect;

import com.sarinsa.dumbores.common.entity.CactusBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class CactusAttractionEffect extends Effect {

    public CactusAttractionEffect() {
        super(EffectType.HARMFUL, 0x8BB499);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        World world = livingEntity.getCommandSenderWorld();
        BlockPos pos = livingEntity.blockPosition();

        Iterable<BlockPos> scanArea = BlockPos.betweenClosed(pos.offset(6, 5, 6), pos.offset(-6, -5, -6));

        for (BlockPos blockPos : scanArea) {
            BlockState state = world.getBlockState(blockPos);

            if (state.is(Blocks.CACTUS)) {
                world.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                CactusBlockEntity cactusEntity = new CactusBlockEntity(world, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
                world.addFreshEntity(cactusEntity);
                cactusEntity.setFollowTarget(livingEntity);
            }
        }
    }

    @Override
    public void applyInstantenousEffect(@Nullable Entity source, @Nullable Entity indirectSource, LivingEntity livingEntity, int amplifier, double health) {

    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
