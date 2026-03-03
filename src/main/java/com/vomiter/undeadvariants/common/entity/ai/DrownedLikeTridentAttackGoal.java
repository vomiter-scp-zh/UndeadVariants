package com.vomiter.undeadvariants.common.entity.ai;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.Items;

public class DrownedLikeTridentAttackGoal extends RangedAttackGoal {
    private final IDrownedLike drowned;

    public DrownedLikeTridentAttackGoal(RangedAttackMob p_32450_, double p_32451_, int p_32452_, float p_32453_) {
        super(p_32450_, p_32451_, p_32452_, p_32453_);
        this.drowned = (IDrownedLike) p_32450_;
    }

    public boolean canUse() {
        return super.canUse() && this.drowned.getMob().getMainHandItem().is(Items.TRIDENT);
    }

    public void start() {
        super.start();
        this.drowned.getMob().setAggressive(true);
        this.drowned.getMob().startUsingItem(InteractionHand.MAIN_HAND);
    }

    public void stop() {
        super.stop();
        this.drowned.getMob().stopUsingItem();
        this.drowned.getMob().setAggressive(false);
    }
}