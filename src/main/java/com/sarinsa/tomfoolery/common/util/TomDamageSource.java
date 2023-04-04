package com.sarinsa.tomfoolery.common.util;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class TomDamageSource {

    public static DamageSource grenadeImpact(Entity grenade, @Nullable Entity shooter) {
        return new IndirectEntityDamageSource(Tomfoolery.MODID + "." + "grenade_impact", grenade, shooter).setProjectile();
    }
}
