package com.vomiter.undeadvariants.common.entity.ai;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class DrownedLikeGoToWaterGoal extends Goal {
    private final PathfinderMob mob;
    private double wantedX, wantedY, wantedZ;
    private final double speedModifier;
    private final Level level;

    public DrownedLikeGoToWaterGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }


    public DrownedLikeGoToWaterGoal(IDrownedLike drownedLike, double speedModifier) {
        this.mob = (PathfinderMob) drownedLike.getMob();
        this.speedModifier = speedModifier;
        this.level = mob.level();
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!level.isDay()) return false;
        if (mob.isInWater()) return false;

        Vec3 pos = getWaterPos();
        if (pos == null) return false;
        wantedX = pos.x; wantedY = pos.y; wantedZ = pos.z;
        return true;
    }

    @Override public boolean canContinueToUse() { return !mob.getNavigation().isDone(); }

    @Override public void start() { mob.getNavigation().moveTo(wantedX, wantedY, wantedZ, speedModifier); }

    @Nullable
    private Vec3 getWaterPos() {
        RandomSource r = mob.getRandom();
        BlockPos base = mob.blockPosition();
        for (int i = 0; i < 10; i++) {
            BlockPos p = base.offset(r.nextInt(20) - 10, 2 - r.nextInt(8), r.nextInt(20) - 10);
            if (level.getBlockState(p).is(Blocks.WATER)) return Vec3.atBottomCenterOf(p);
        }
        return null;
    }
}
