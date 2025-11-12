package com.sarinsa.tomfoolery.common.entity;

import com.sarinsa.tomfoolery.common.core.registry.TomEntities;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.level.Level;

public class HugeFireball extends LargeFireball {
    
    public HugeFireball( EntityType<? extends LargeFireball> entityType, Level level ) {
        super( entityType, level );
    }
    
    public HugeFireball( Level level, LivingEntity shooter, double x, double y, double z, int explosionPower ) {
        super( level, shooter, x, y, z, explosionPower );
    }
    
    @Override
    public EntityType<?> getType() {
        return TomEntities.HUGE_FIREBALL.get();
    }
    
    @Override
    public boolean hurt( DamageSource damageSource, float damage ) {
        if( isInvulnerableTo( damageSource ) ) {
            return false;
        }
        else {
            this.markHurt();
            return true;
        }
    }
}
