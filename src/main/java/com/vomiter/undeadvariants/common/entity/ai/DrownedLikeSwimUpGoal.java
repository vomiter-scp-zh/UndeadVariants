package com.vomiter.undeadvariants.common.entity.ai;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class DrownedLikeSwimUpGoal extends Goal {
    private final IDrownedLike drownedLike;
    private final double speedModifier;
    private final int seaLevel;
    private boolean stuck;

    public DrownedLikeSwimUpGoal(IDrownedLike drownedLike, double p_32441_, int p_32442_) {
        this.drownedLike = drownedLike;
        this.speedModifier = p_32441_;
        this.seaLevel = p_32442_;
    }

    public boolean canUse() {
        return !drownedLike.getMob().level().isDay() && drownedLike.getMob().isInWater() && drownedLike.getMob().getY() < (double)(this.seaLevel - 2);
    }

    public boolean canContinueToUse() {
        return this.canUse() && !this.stuck;
    }

    public void tick() {
        if (drownedLike.getMob().getY() < (double)(this.seaLevel - 1) && (drownedLike.getMob().getNavigation().isDone() || drownedLike.closeToNextPos())) {
            Vec3 vec3 = DefaultRandomPos.getPosTowards((PathfinderMob) drownedLike.getMob(), 4, 8, new Vec3(drownedLike.getMob().getX(), (double)(this.seaLevel - 1), drownedLike.getMob().getZ()), (double)((float)Math.PI / 2F));
            if (vec3 == null) {
                this.stuck = true;
                return;
            }

            drownedLike.getMob().getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
        }

    }

    public void start() {
        drownedLike.setSearchingForLand(true);
        this.stuck = false;
    }

    public void stop() {
        drownedLike.setSearchingForLand(false);
    }
}
