package com.sarinsa.tomfoolery.client.render.entity.ghastinator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.entity.living.Ghastinator;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

/**
 * Renders the destroyer's fullbright eyes
 */
public class GhastinatorEyesLayer extends RenderLayer<Ghastinator, GhastinatorModel> {
    
    private static final RenderType EYES = RenderType.entityCutout( Tomfoolery.rl( "textures/entity/ghastinator/ghastinator_eyes.png" ) );
    
    
    public GhastinatorEyesLayer( RenderLayerParent<Ghastinator, GhastinatorModel> parent ) {
        super( parent );
    }
    
    @Override
    public void render( PoseStack poseStack, MultiBufferSource buffer, int packedLight, Ghastinator ghastinator, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch ) {
        if( ghastinator.isCharging() ) {
            VertexConsumer vertexConsumer = buffer.getBuffer( EYES );
            getParentModel().renderToBuffer( poseStack, vertexConsumer, LightTexture.pack( 15, 15 ), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F );
        }
    }
}