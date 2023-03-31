package com.sarinsa.tomfoolery.common.grenades;

import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class ExplosiveGrenadeType extends GrenadeType {

    public ExplosiveGrenadeType() {
        super(80, ParticleTypes.CLOUD, 0xFFDD66, 0x6066C4);
    }

    @Override
    public <T extends GrenadeRoundEntity> void generalImpact(T entity, Level level, HitResult result) {
        if (!level.isClientSide) {
            level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 6.0F, Explosion.BlockInteraction.NONE);
        }
        entity.discard();
    }
}
