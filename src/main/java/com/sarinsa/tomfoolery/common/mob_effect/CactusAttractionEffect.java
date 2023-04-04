package com.sarinsa.tomfoolery.common.mob_effect;

import com.sarinsa.tomfoolery.common.entity.CactusBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class CactusAttractionEffect extends MobEffect {

    public CactusAttractionEffect() {
        super(MobEffectCategory.HARMFUL, 0x8BB499);
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        Level level = livingEntity.getCommandSenderWorld();
        BlockPos pos = livingEntity.blockPosition();
        int range = amplifier > 0 ? 12 : 6;

        Iterable<BlockPos> scanArea = BlockPos.betweenClosed(pos.offset(range, 5, range), pos.offset(-range, -5, -range));

        for (BlockPos blockPos : scanArea) {
            BlockState state = level.getBlockState(blockPos);

            if (state.is(Blocks.CACTUS)) {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 3);
                CactusBlockEntity cactusEntity = new CactusBlockEntity(level, livingEntity, blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D);
                level.addFreshEntity(cactusEntity);
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
