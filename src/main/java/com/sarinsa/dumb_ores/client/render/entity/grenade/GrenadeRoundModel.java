package com.sarinsa.dumb_ores.client.render.entity.grenade;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sarinsa.dumb_ores.common.entity.GrenadeRoundEntity;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class GrenadeRoundModel extends EntityModel<GrenadeRoundEntity> {

    private final ModelRenderer grenade;

    public GrenadeRoundModel() {
        texWidth = 16;
        texHeight = 16;

        grenade = new ModelRenderer(this);
        grenade.setPos(0.0F, 24.0F, 0.0F);
        grenade.texOffs(0, 0).addBox(-2.0F, -3.0F, -2.0F, 3.0F, 3.0F, 4.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(GrenadeRoundEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        grenade.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
