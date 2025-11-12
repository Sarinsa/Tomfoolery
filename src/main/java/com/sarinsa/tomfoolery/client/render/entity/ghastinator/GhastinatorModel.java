package com.sarinsa.tomfoolery.client.render.entity.ghastinator;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sarinsa.tomfoolery.common.entity.living.Ghastinator;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class GhastinatorModel extends HierarchicalModel<Ghastinator> {
    
    
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart[] tentacles = new ModelPart[19];
    
    public GhastinatorModel( ModelPart root ) {
        this.root = root;
        this.body = root.getChild( "body" );
        
        for( int i = 0; i < tentacles.length; ++i ) {
            tentacles[i] = body.getChild( "tentacle_" + i );
        }
    }
    
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        
        PartDefinition body = partdefinition.addOrReplaceChild( "body", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation( 0.0F ) ), PartPose.offset( 0.0F, 16.0F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_3", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -3.0F, -7.0F, 2.0F, 0.7854F, 3.1416F, -0.1745F ) );
        
        body.addOrReplaceChild( "tentacle_2", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 3.0F, -7.0F, 2.0F, 0.7854F, 3.1416F, 0.1745F ) );
        
        body.addOrReplaceChild( "tentacle_1", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -9.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 0.0F, -7.0F, -3.0F, 0.6109F, 3.1416F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_5", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -4.0F, 8.0F, 0.0F, 0.0873F, 0.0F, 0.0436F ) );
        
        body.addOrReplaceChild( "tentacle_18", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -7.0F, 5.0F, 1.0F, 0.5672F, 0.2618F, 1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_17", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -7.0F, -3.0F, 1.0F, 0.5672F, -0.2618F, 1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_16", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 7.0F, 5.0F, 1.0F, 0.5672F, -0.2618F, -1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_15", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 7.0F, -3.0F, 1.0F, 0.5672F, 0.2618F, -1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_14", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 7.0F, 0.0F, 4.0F, 0.7854F, 0.0F, -1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_13", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 7.0F, 2.0F, -4.0F, 0.5672F, 0.0F, -1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_12", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -7.0F, 0.0F, 4.0F, 0.7854F, 0.0F, 1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_11", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -7.0F, 2.0F, -4.0F, 0.5672F, 0.0F, 1.5708F ) );
        
        body.addOrReplaceChild( "tentacle_10", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 5.0F, 7.0F, -5.0F, 0.0873F, 0.0F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_9", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 5.0F, 8.0F, 5.0F, 0.2182F, 0.5236F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_8", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 8.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -5.0F, 8.0F, -5.0F, 0.0873F, 0.0F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_7", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( -5.0F, 8.0F, 5.0F, 0.2182F, -0.5236F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_6", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 4.0F, 8.0F, 0.0F, 0.0873F, 0.0F, -0.0436F ) );
        
        body.addOrReplaceChild( "tentacle_4", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 0.0F, 8.0F, 4.0F, 0.2182F, 0.0F, 0.0F ) );
        
        body.addOrReplaceChild( "tentacle_0", CubeListBuilder.create().texOffs( 0, 0 ).addBox( -1.0F, -1.0F, -1.0F, 2.0F, 10.0F, 2.0F, new CubeDeformation( 0.0F ) ), PartPose.offsetAndRotation( 0.0F, 7.0F, -3.0F, 0.0873F, 0.0F, 0.0F ) );
        
        return LayerDefinition.create( meshdefinition, 64, 32 );
    }
    
    @Override
    public void setupAnim( Ghastinator ghastinator, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch ) {
        body.xRot = headPitch * (float) Math.PI / 180.0F;
        body.yRot = netHeadYaw * (float) Math.PI / 180.0F;
        
        for( int i = 0; i < tentacles.length; ++i ) {
            float mult = ghastinator.isAggressive() ? 3.0F : 1.0F;
            tentacles[i].xRot = 0.2F * Mth.sin( mult * ageInTicks * 0.3F + (float) i ) + 0.4F;
        }
    }
    
    @Override
    public ModelPart root() {
        return root;
    }
    
    @Override
    public void renderToBuffer( PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha ) {
        body.render( poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha );
    }
}
