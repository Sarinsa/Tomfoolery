package com.sarinsa.tomfoolery.common.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;

public class EntityHelper {

    public static void shootFromRotation(Entity launched, Entity shooter, float xRot, float yRot, float p_37255_, float p_37256_, float p_37257_) {
        float x = -Mth.sin(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));
        float y = -Mth.sin((xRot + p_37255_) * ((float)Math.PI / 180F));
        float z = Mth.cos(yRot * ((float)Math.PI / 180F)) * Mth.cos(xRot * ((float)Math.PI / 180F));

        shoot(launched, x, y, z, p_37256_, p_37257_);

        Vec3 vec3 = shooter.getDeltaMovement();
        launched.setDeltaMovement(launched.getDeltaMovement().add(vec3.x, launched.isOnGround() ? 0.0D : vec3.y, vec3.z));
    }

    public static void shoot(Entity launched, double x, double y, double z, float p_37269_, float p_37270_) {
        Vec3 vec3 = (new Vec3(x, y, z)).normalize().add(launched.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), launched.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_), launched.level.random.triangle(0.0D, 0.0172275D * (double)p_37270_)).scale((double)p_37269_);
        launched.setDeltaMovement(vec3);
        double d0 = vec3.horizontalDistance();
        launched.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
        launched.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
        launched.yRotO = launched.getYRot();
        launched.xRotO = launched.getXRot();
    }
}
