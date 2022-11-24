package com.sarinsa.tomfoolery.client.render.entity.grenade;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;

public class GrenadeRoundRenderer extends EntityRenderer<GrenadeRoundEntity> {

    private static final ResourceLocation TEXTURE = Tomfoolery.resourceLoc("textures/entity/grenade/grenade.png");
    private final GrenadeRoundModel model = new GrenadeRoundModel();

    public GrenadeRoundRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    public void render(GrenadeRoundEntity entity, float limbRot, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        matrixStack.pushPose();
        matrixStack.translate(0.0D, -1.25D, 0.0D);

        int color = entity.getGrenadeType().getColor(1);

        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;

        IVertexBuilder vertexBuilder = buffer.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);

        matrixStack.popPose();
        super.render(entity, limbRot, partialTick, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GrenadeRoundEntity entity) {
        return TEXTURE;
    }
}
