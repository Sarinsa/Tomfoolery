package com.sarinsa.tomfoolery.common.grenades;

import com.sarinsa.tomfoolery.common.core.registry.TomDamageTypes;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;

public class ExplosiveGrenadeType extends GrenadeType {
    
    public ExplosiveGrenadeType() {
        super( 80, ParticleTypes.CLOUD, 0xFFDD66, 0x6066C4 );
    }
    
    @Override
    public <T extends Projectile> void generalImpact( T entity, @Nullable Entity shooter, Level level, HitResult result ) {
        level.explode( entity, entity.getX(), entity.getY(), entity.getZ(), 6.0F, Level.ExplosionInteraction.NONE );
    }
    
    @Override
    public <T extends Projectile> void onEntityImpact( T entity, @Nullable Entity shooter, Level level, EntityHitResult result ) {
        if( result.getEntity() instanceof LivingEntity ) {
            result.getEntity().hurt( TomDamageTypes.of( level, TomDamageTypes.GRENADE, entity, shooter ), 4.0F );
        }
    }
}
