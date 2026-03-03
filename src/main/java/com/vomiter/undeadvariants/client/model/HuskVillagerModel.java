package com.vomiter.undeadvariants.client.model;

import com.vomiter.undeadvariants.common.entity.group.husk.HuskVillager;
import net.minecraft.client.model.ZombieVillagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HuskVillagerModel extends ZombieVillagerModel<HuskVillager> {
    public HuskVillagerModel(ModelPart root) {
        super(root);
    }
}
