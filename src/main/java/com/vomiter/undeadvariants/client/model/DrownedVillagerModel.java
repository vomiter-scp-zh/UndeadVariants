package com.vomiter.undeadvariants.client.model;

import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedVillager;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DrownedVillagerModel extends ZombieVillagerModel<DrownedVillager> implements TridentThrowerModel<DrownedVillager> {

    public DrownedVillagerModel(ModelPart root) {
        super(root);
    }

    @Override
    public void prepareMobModel(@NotNull DrownedVillager entity, float limbSwing, float limbSwingAmount, float partialTick) {
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTick);
        prepareTridentThrowing(entity, limbSwing, limbSwingAmount, partialTick);
    }

    @Override
    public void setupAnim(@NotNull DrownedVillager p_102526_, float p_102527_, float p_102528_, float p_102529_, float p_102530_, float p_102531_) {
        super.setupAnim(p_102526_, p_102527_, p_102528_, p_102529_, p_102530_, p_102531_);
        setThrowingTridentAnim();
    }

    @Override
    public HumanoidModel<?> self() {
        return this;
    }
}
