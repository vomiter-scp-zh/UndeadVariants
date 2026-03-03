package com.vomiter.undeadvariants.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface TridentThrowerModel<T extends  LivingEntity> {
    HumanoidModel<?> self();

    default void prepareTridentThrowing(T entity, float limbSwing, float limbSwingAmount, float partialTick) {
        self().rightArmPose = HumanoidModel.ArmPose.EMPTY;
        self().leftArmPose  = HumanoidModel.ArmPose.EMPTY;

        ItemStack main = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (main.is(Items.TRIDENT)
                && entity instanceof net.minecraft.world.entity.Mob mob
                && mob.isAggressive()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                self().rightArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            } else {
                self().leftArmPose = HumanoidModel.ArmPose.THROW_SPEAR;
            }
        }
    }

    default void setThrowingTridentAnim() {
        HumanoidModel<?> m = self();

        boolean leftThrow  = m.leftArmPose  == HumanoidModel.ArmPose.THROW_SPEAR;
        boolean rightThrow = m.rightArmPose == HumanoidModel.ArmPose.THROW_SPEAR;

        if (leftThrow) {
            m.leftArm.xRot = m.leftArm.xRot * 0.5F - (float)Math.PI;
            m.leftArm.yRot = 0.0F;
        } else if (rightThrow) {
            m.rightArm.xRot = m.rightArm.xRot * 0.5F - (float)Math.PI;
            m.rightArm.yRot = 0.0F;
        }
    }

}
