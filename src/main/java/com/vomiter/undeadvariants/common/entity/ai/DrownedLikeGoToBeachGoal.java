package com.vomiter.undeadvariants.common.entity.ai;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;

public class DrownedLikeGoToBeachGoal extends MoveToBlockGoal {
    private final IDrownedLike drownedLike;

    public DrownedLikeGoToBeachGoal(IDrownedLike drownedLike, double p_32410_) {
        super((PathfinderMob) drownedLike.getMob(), p_32410_, 8, 2);
        this.drownedLike = drownedLike;
    }

    public boolean canUse() {
        return super.canUse() && !drownedLike.getMob().level().isDay() && drownedLike.getMob().isInWater() && drownedLike.getMob().getY() >= (double)(drownedLike.getMob().level().getSeaLevel() - 3);
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse();
    }

    protected boolean isValidTarget(LevelReader p_32413_, BlockPos p_32414_) {
        BlockPos blockpos = p_32414_.above();
        return p_32413_.isEmptyBlock(blockpos) && p_32413_.isEmptyBlock(blockpos.above()) && p_32413_.getBlockState(p_32414_).entityCanStandOn(p_32413_, p_32414_, drownedLike.getMob());
    }

    public void start() {
        drownedLike.setSearchingForLand(false);
        drownedLike.useGroundNavigation();
        super.start();
    }

    public void stop() {
        super.stop();
    }

}
