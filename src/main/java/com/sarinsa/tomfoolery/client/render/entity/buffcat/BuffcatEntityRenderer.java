package com.sarinsa.tomfoolery.client.render.entity.buffcat;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.living.BuffcatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class BuffcatEntityRenderer extends LivingEntityRenderer<BuffcatEntity, BuffcatEntityModel> {

    private static final ResourceLocation TEXTURE = Tomfoolery.resourceLoc("textures/entity/buffcat/calico.png");

    public BuffcatEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new BuffcatEntityModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BuffcatEntity entity) {
        return TEXTURE;
    }
}
