package com.sarinsa.tomfoolery.client.render.entity.buffcat;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.sarinsa.tomfoolery.common.entity.living.BuffcatEntity;
import net.minecraft.client.renderer.entity.model.*;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class BuffcatEntityModel extends EntityModel<BuffcatEntity> {

    private final ModelRenderer left_ear;
    private final ModelRenderer right_ear;
    private final ModelRenderer neck;
    private final ModelRenderer mane;
    private final ModelRenderer body;
    private final ModelRenderer cat;
    private final ModelRenderer cat_r1;
    private final ModelRenderer body_sub_1;
    private final ModelRenderer body_sub_1_r1;
    private final ModelRenderer bone;
    private final ModelRenderer bone_r1;
    private final ModelRenderer bone3;
    private final ModelRenderer body_sub_4;
    private final ModelRenderer body_sub_4_r1;
    private final ModelRenderer bone6;
    private final ModelRenderer bone4;
    private final ModelRenderer body_sub_7;
    private final ModelRenderer bone2;
    private final ModelRenderer body_sub_9;
    private final ModelRenderer bone5;

    public BuffcatEntityModel() {
        texWidth = 128;
        texHeight = 128;

        left_ear = new ModelRenderer(this);
        left_ear.setPos(0.0F, 1.0F, -9.0F);


        right_ear = new ModelRenderer(this);
        right_ear.setPos(0.0F, 1.0F, -9.0F);


        neck = new ModelRenderer(this);
        neck.setPos(0.0F, 2.0F, -8.9F);


        mane = new ModelRenderer(this);
        mane.setPos(0.0F, 2.0F, -9.0F);


        body = new ModelRenderer(this);
        body.setPos(0.0F, 11.0F, 6.0F);


        cat = new ModelRenderer(this);
        cat.setPos(0.0F, -7.0F, -6.0F);
        body.addChild(cat);
        cat.texOffs(22, 16).addBox(2.0F, 15.7F, -0.65F, 1.0F, 4.0F, 1.0F, 0.2F, false);
        cat.texOffs(48, 36).addBox(0.6F, 17.975F, -3.35F, 4.0F, 2.0F, 5.0F, 0.0F, false);
        cat.texOffs(2, 19).addBox(-2.5F, -8.0F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cat.texOffs(4, 16).addBox(1.5F, -8.0F, -0.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
        cat.texOffs(6, 19).addBox(-0.3F, 10.85F, 2.7F, 1.0F, 1.0F, 1.0F, 0.0F, false);
        cat.texOffs(44, 45).addBox(7.9221F, 5.3711F, -2.2F, 5.0F, 7.0F, 4.0F, 0.0F, false);
        cat.texOffs(44, 21).addBox(-2.5F, -7.0F, -2.5F, 5.0F, 4.0F, 5.0F, 0.0F, false);
        cat.texOffs(0, 4).addBox(-1.5F, -5.0F, -3.5F, 3.0F, 2.0F, 1.0F, 0.0F, false);
        cat.texOffs(30, 0).addBox(-3.2F, 10.0F, -2.65F, 6.0F, 3.0F, 5.0F, 0.0F, false);
        cat.texOffs(0, 0).addBox(-5.5F, -3.0F, -4.4F, 11.0F, 8.0F, 8.0F, 0.2F, false);
        cat.texOffs(33, 11).addBox(-4.55F, 5.0F, -2.625F, 9.0F, 5.0F, 5.0F, 0.0F, false);

        cat_r1 = new ModelRenderer(this);
        cat_r1.setPos(9.9221F, 13.8711F, -0.2F);
        cat.addChild(cat_r1);
        setRotationAngle(cat_r1, 0.0F, 0.0F, 0.2182F);
        cat_r1.texOffs(30, 53).addBox(-2.4F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        body_sub_1 = new ModelRenderer(this);
        body_sub_1.setPos(0.0F, 0.0F, 0.0F);
        cat.addChild(body_sub_1);
        body_sub_1.texOffs(0, 44).addBox(-12.9221F, 5.3711F, -2.2F, 5.0F, 7.0F, 4.0F, 0.0F, false);
        body_sub_1.texOffs(0, 16).addBox(-3.425F, 15.7F, -0.65F, 1.0F, 4.0F, 1.0F, 0.2F, false);
        body_sub_1.texOffs(47, 3).addBox(-4.525F, 17.975F, -3.35F, 4.0F, 2.0F, 5.0F, 0.0F, false);

        body_sub_1_r1 = new ModelRenderer(this);
        body_sub_1_r1.setPos(-9.9221F, 13.8711F, -0.2F);
        body_sub_1.addChild(body_sub_1_r1);
        setRotationAngle(body_sub_1_r1, 0.0F, 0.0F, -0.2182F);
        body_sub_1_r1.texOffs(18, 49).addBox(-1.6F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);

        bone = new ModelRenderer(this);
        bone.setPos(-7.3F, 16.75F, 7.0F);
        cat.addChild(bone);
        setRotationAngle(bone, 0.0F, 0.0F, -0.3927F);


        bone_r1 = new ModelRenderer(this);
        bone_r1.setPos(10.3348F, 1.4763F, -7.0F);
        bone.addChild(bone_r1);
        setRotationAngle(bone_r1, 0.0F, 0.0F, 0.2618F);
        bone_r1.texOffs(46, 56).addBox(-2.2F, -2.4F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        bone3 = new ModelRenderer(this);
        bone3.setPos(-3.075F, 14.65F, 0.0F);
        cat.addChild(bone3);
        setRotationAngle(bone3, 0.0F, 0.0F, 0.3927F);


        body_sub_4 = new ModelRenderer(this);
        body_sub_4.setPos(0.0F, 0.0F, 0.0F);
        bone3.addChild(body_sub_4);
        setRotationAngle(body_sub_4, 0.0F, 0.0F, -0.1309F);


        body_sub_4_r1 = new ModelRenderer(this);
        body_sub_4_r1.setPos(0.1999F, -0.4536F, 0.0F);
        body_sub_4.addChild(body_sub_4_r1);
        setRotationAngle(body_sub_4_r1, 0.0F, 0.0F, -0.1309F);
        body_sub_4_r1.texOffs(0, 55).addBox(-1.3F, -2.5F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);

        bone6 = new ModelRenderer(this);
        bone6.setPos(-8.3F, 12.2278F, 11.3823F);
        cat.addChild(bone6);
        setRotationAngle(bone6, 0.7854F, 0.0F, 0.0F);
        bone6.texOffs(54, 30).addBox(8.0F, -5.0608F, -1.6595F, 1.0F, 1.0F, 5.0F, 0.0F, false);

        bone4 = new ModelRenderer(this);
        bone4.setPos(-18.375F, -5.35F, 7.0F);
        cat.addChild(bone4);
        setRotationAngle(bone4, 0.0F, 0.0F, 0.3927F);


        body_sub_7 = new ModelRenderer(this);
        body_sub_7.setPos(0.0F, 0.0F, 0.0F);
        bone4.addChild(body_sub_7);
        body_sub_7.texOffs(24, 36).addBox(9.1406F, 0.8182F, -10.2F, 6.0F, 7.0F, 6.0F, 0.0F, false);
        body_sub_7.texOffs(22, 21).addBox(8.6119F, -3.5163F, -12.0F, 6.0F, 5.0F, 10.0F, 0.0F, false);

        bone2 = new ModelRenderer(this);
        bone2.setPos(-18.9F, -2.2F, 7.0F);
        cat.addChild(bone2);
        setRotationAngle(bone2, 0.0F, 0.0F, -0.3927F);
        bone2.texOffs(0, 31).addBox(20.5024F, 12.1725F, -10.2F, 6.0F, 7.0F, 6.0F, 0.0F, false);

        body_sub_9 = new ModelRenderer(this);
        body_sub_9.setPos(0.0F, 0.0F, 0.0F);
        bone2.addChild(body_sub_9);
        body_sub_9.texOffs(0, 16).addBox(21.0687F, 7.8535F, -12.0F, 6.0F, 5.0F, 10.0F, 0.0F, false);

        bone5 = new ModelRenderer(this);
        bone5.setPos(-8.3F, 12.2606F, 9.9285F);
        cat.addChild(bone5);
        setRotationAngle(bone5, 0.3927F, 0.0F, 0.0F);
        bone5.texOffs(0, 0).addBox(8.0F, -3.5907F, -5.1815F, 1.0F, 1.0F, 3.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(BuffcatEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        body_sub_9.yRot = netHeadYaw * ((float)Math.PI / 180F);
        body_sub_9.xRot = headPitch * ((float)Math.PI / 180F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        left_ear.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        right_ear.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        neck.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        mane.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        body.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}
