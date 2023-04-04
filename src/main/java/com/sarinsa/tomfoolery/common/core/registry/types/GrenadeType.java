package com.sarinsa.tomfoolery.common.core.registry.types;

import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class GrenadeType {

    private final int safetyDist;
    private final SimpleParticleType traceParticle;
    private final int shellColor;
    private final int topColor;

    public GrenadeType(int safetyDist, SimpleParticleType traceParticle, int shellColor, int topColor) {
        this.safetyDist = safetyDist;
        this.traceParticle = traceParticle;
        this.shellColor = shellColor;
        this.topColor = topColor;
    }


    public <T extends Projectile> void onBlockImpact(T entity, @Nullable Entity shooter, Level level, BlockHitResult result) {

    }

    public <T extends Projectile> void onEntityImpact(T entity, @Nullable Entity shooter, Level level, EntityHitResult result) {

    }

    /**
     * Called when the grenade collides with something.
     */
    public <T extends Projectile> void generalImpact(T entity, @Nullable Entity shooter, Level level, HitResult result) {

    }

    /**
     * @return The squared distance the grenade must travel before it can arm.
     */
    public double getSafetyDist() {
        return safetyDist;
    }

    public SimpleParticleType getTraceParticle() {
        return traceParticle;
    }

    public final int getColor(int color) {
        return color == 0 ? shellColor : topColor;
    }
}
