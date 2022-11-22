package com.sarinsa.dumb_ores.client.render.entity.grenade;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sarinsa.dumb_ores.common.core.Tomfoolery;
import com.sarinsa.dumb_ores.common.entity.GrenadeRoundEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
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

        IVertexBuilder vertexBuilder = buffer.getBuffer(model.renderType(TEXTURE));
        model.renderToBuffer(matrixStack, vertexBuilder, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.popPose();
        super.render(entity, limbRot, partialTick, matrixStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GrenadeRoundEntity entity) {
        return TEXTURE;
    }
}
