package com.sarinsa.tomfoolery.client.render.entity;

import net.minecraft.client.model.HumanoidModel;

public class TomArmPoses {
    
    public static final HumanoidModel.ArmPose STRAIGHT_ARM = HumanoidModel.ArmPose.create( "straight_arm", true,
            ( model, entity, arm ) -> {
                model.leftArm.xRot = (model.head.xRot) - 1.7F;
                model.rightArm.xRot = (model.head.xRot) - 1.7F;
            }
    );
    
    public static void init() { }
}
