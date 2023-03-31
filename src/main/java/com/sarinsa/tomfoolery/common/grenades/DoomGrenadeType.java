package com.sarinsa.tomfoolery.common.grenades;

import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class DoomGrenadeType extends GrenadeType {

    public DoomGrenadeType() {
        super(280, ParticleTypes.SMOKE, 0xFFDD66, 0x494949);
    }

    /**
     * Called when the grenade collides with something.
     */
    public <T extends GrenadeRoundEntity> void generalImpact(T entity, Level level, HitResult result) {
        if (!level.isClientSide) {
            level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 20.0F, Explosion.BlockInteraction.DESTROY);
        }
        entity.discard();
    }
}
