package com.sarinsa.tomfoolery.client.render.entity.grenade;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sarinsa.tomfoolery.client.TomfooleryModelLayers;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class GrenadeRoundRenderer extends EntityRenderer<GrenadeRoundEntity> {

    private static final ResourceLocation TEXTURE = Tomfoolery.resourceLoc("textures/entity/grenade/grenade.png");
    private GrenadeRoundModel model;

    public GrenadeRoundRenderer(EntityRendererProvider.Context context) {
        super(context);
        model = new GrenadeRoundModel(context.bakeLayer(TomfooleryModelLayers.GRENADE_ROUND));
    }

    public void render(GrenadeRoundEntity entity, float limbRot, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0D, -1.25D, 0.0D);

        int color = entity.getGrenadeType().getColor(1);

        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        VertexConsumer vertexConsumer = bufferSource.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);

        poseStack.popPose();
        super.render(entity, limbRot, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GrenadeRoundEntity entity) {
        return TEXTURE;
    }
}
