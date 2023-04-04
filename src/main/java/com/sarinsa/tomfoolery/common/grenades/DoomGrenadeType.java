package com.sarinsa.tomfoolery.common.grenades;

import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.util.TomDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class DoomGrenadeType extends GrenadeType {

    public DoomGrenadeType() {
        super(280, ParticleTypes.SMOKE, 0xFFDD66, 0x494949);
    }

    /**
     * Called when the grenade collides with something.
     */
    @Override
    public <T extends Projectile> void generalImpact(T entity, @Nullable Entity shooter, Level level, HitResult result) {
        if (!level.isClientSide) {
            level.explode(entity, entity.getX(), entity.getY(), entity.getZ(), 20.0F, Explosion.BlockInteraction.DESTROY);
        }
        entity.discard();
    }

    @Override
    public <T extends Projectile> void onEntityImpact(T entity, @Nullable Entity shooter, Level level, EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity) {
            result.getEntity().hurt(TomDamageSource.grenadeImpact(entity, shooter), 4.0F);
        }
    }
}
