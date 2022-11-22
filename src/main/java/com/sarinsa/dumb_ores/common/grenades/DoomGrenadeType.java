package com.sarinsa.dumb_ores.common.grenades;

import com.sarinsa.dumb_ores.common.core.registry.types.GrenadeType;
import com.sarinsa.dumb_ores.common.entity.GrenadeRoundEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class DoomGrenadeType extends GrenadeType {

    public DoomGrenadeType() {
        super(200, ParticleTypes.SMOKE, 0xFFDD66, 0x494949);
    }

    /**
     * Called when the grenade collides with something.
     */
    public <T extends GrenadeRoundEntity> void generalImpact(T entity, World world, RayTraceResult result) {
        BlockPos initialPos = entity.getInitialPos();

        if (entity.distanceToSqr(initialPos.getX(), initialPos.getY(), initialPos.getZ()) > getSafetyDist()) {
            if (!world.isClientSide) {
                world.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 20.0F, Explosion.Mode.DESTROY);
            }
        }
        entity.remove();
    }
}
