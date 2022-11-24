package com.sarinsa.tomfoolery.common.grenades;

import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosiveGrenadeType extends GrenadeType {

    public ExplosiveGrenadeType() {
        super(80, ParticleTypes.CLOUD, 0xFFDD66, 0x6066C4);
    }

    @Override
    public <T extends GrenadeRoundEntity> void generalImpact(T entity, World world, RayTraceResult result) {
        if (!world.isClientSide) {
            world.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 6.0F, Explosion.Mode.NONE);
        }
        entity.remove();
    }
}
