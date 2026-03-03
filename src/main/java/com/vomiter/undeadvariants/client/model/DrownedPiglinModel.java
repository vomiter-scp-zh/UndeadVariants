package com.vomiter.undeadvariants.client.model;

import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedPiglin;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelPart;
import org.jetbrains.annotations.NotNull;

public class DrownedPiglinModel extends PiglinModel<DrownedPiglin> implements TridentThrowerModel<DrownedPiglin> {
    public DrownedPiglinModel(ModelPart p_170810_) {
        super(p_170810_);
    }

    @Override
    public void prepareMobModel(@NotNull DrownedPiglin entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        prepareTridentThrowing(entity, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void setupAnim(@NotNull DrownedPiglin entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        boolean throwing =
                this.leftArmPose == HumanoidModel.ArmPose.THROW_SPEAR
                        || this.rightArmPose == HumanoidModel.ArmPose.THROW_SPEAR;

        if (!throwing) {
        }
        AnimationUtils.animateZombieArms(this.leftArm, this.rightArm, entity.isAggressive(), this.attackTime, ageInTicks);

        setThrowingTridentAnim();
    }


    @Override
    public HumanoidModel<?> self() {
        return this;
    }
}
