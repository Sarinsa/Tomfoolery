package com.sarinsa.tomfoolery.client.render.entity.buffcat;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.living.BuffcatEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.spawner.WorldEntitySpawner;

public class BuffcatEntityRenderer extends LivingRenderer<BuffcatEntity, BuffcatEntityModel> {

    private static final ResourceLocation TEXTURE = Tomfoolery.resourceLoc("textures/entity/buffcat/calico.png");

    public BuffcatEntityRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new BuffcatEntityModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BuffcatEntity entity) {
        return TEXTURE;
    }
}
