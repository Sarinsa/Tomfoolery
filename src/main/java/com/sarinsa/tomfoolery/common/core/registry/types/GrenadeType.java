package com.sarinsa.tomfoolery.common.core.registry.types;

import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;

public class GrenadeType implements IForgeRegistryEntry<GrenadeType> {

    private ResourceLocation registryId;
    private final int safetyDist;
    private final IParticleData traceParticle;
    private final int shellColor;
    private final int topColor;

    public GrenadeType(int safetyDist, IParticleData traceParticle, int shellColor, int topColor) {
        this.safetyDist = safetyDist;
        this.traceParticle = traceParticle;
        this.shellColor = shellColor;
        this.topColor = topColor;
    }


    public <T extends GrenadeRoundEntity> void onBlockImpact(T entity, World world, BlockRayTraceResult result) {

    }

    public <T extends GrenadeRoundEntity> void onEntityImpact(T entity, World world, EntityRayTraceResult result) {

    }

    /**
     * Called when the grenade collides with something.
     */
    public <T extends GrenadeRoundEntity> void generalImpact(T entity, World world, RayTraceResult result) {

    }

    /**
     * @return The squared distance the grenade must travel before it can arm.
     */
    public double getSafetyDist() {
        return safetyDist;
    }

    public IParticleData getTraceParticle() {
        return traceParticle;
    }

    public final int getColor(int color) {
        return color == 0 ? shellColor : topColor;
    }

    @Override
    public GrenadeType setRegistryName(ResourceLocation name) {
        this.registryId = name;
        return this;
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return registryId;
    }

    @Override
    public Class<GrenadeType> getRegistryType() {
        return GrenadeType.class;
    }
}
