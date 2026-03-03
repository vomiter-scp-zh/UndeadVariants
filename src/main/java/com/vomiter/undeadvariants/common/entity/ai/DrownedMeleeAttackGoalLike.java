package com.vomiter.undeadvariants.common.entity.ai;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.Zombie;

public class DrownedMeleeAttackGoalLike extends ZombieAttackGoal {
    private final Zombie self;
    private final IDrownedLike drownedLike;

    public DrownedMeleeAttackGoalLike(Zombie self, double speed, boolean followingTargetEvenIfNotSeen) {
        super(self, speed, followingTargetEvenIfNotSeen);
        this.self = self;
        if(self instanceof IDrownedLike drownedLike0){
            this.drownedLike = drownedLike0;
        }
        else {
            throw new IllegalArgumentException("DrownedMeleeAttackGoalLike requires mob to implement IDrownedLike: " + mob.getClass().getName());
        }
    }

    @Override
    public boolean canUse() {
        return super.canUse() && drownedLike.shouldAggro(self.getTarget());
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && drownedLike.shouldAggro(self.getTarget());
    }
}
