package com.vomiter.undeadvariants.common.entity.group.drowned;

import com.vomiter.undeadvariants.common.entity.ai.DrownedLikeGoToBeachGoal;
import com.vomiter.undeadvariants.common.entity.ai.DrownedLikeGoToWaterGoal;
import com.vomiter.undeadvariants.common.entity.ai.DrownedLikeSwimUpGoal;
import com.vomiter.undeadvariants.common.entity.ai.DrownedLikeTridentAttackGoal;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public final class DrownedTrait {
    private final Mob mob;
    private final IDrownedLike drownedLike;
    private final Level level;

    // 狀態欄位（interface 放不了的都放這）
    private boolean searchingForLand;
    private WaterBoundPathNavigation waterNavigation;
    private GroundPathNavigation groundNavigation;

    public DrownedTrait(Mob mob) {
        this.mob = mob;
        this.level = mob.level();

        if (!(mob instanceof IDrownedLike dl)) {
            throw new IllegalArgumentException("DrownedTrait requires mob to implement IDrownedLike: " + mob.getClass().getName());
        }
        this.drownedLike = dl;
    }

    public void init() {
        mob.setMaxUpStep(1.0F);
        mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);

        this.waterNavigation = new WaterBoundPathNavigation(mob, level);
        this.groundNavigation = new GroundPathNavigation(mob, level);

        drownedLike.setMoveControl(new DrownedMoveControl(mob, this));
    }

    public Mob getMob(){
        return mob;
    }

    public void useGroundNavigation() { drownedLike.setNavigation(groundNavigation); }
    public void useWaterNavigation()  { drownedLike.setNavigation(waterNavigation); }


    public boolean shouldAggro(LivingEntity target) {
        Level level = this.mob.level();

        // 只限制主世界；其他維度照常
        if (!level.dimension().equals(Level.OVERWORLD)) return true;

        boolean sunnyDay = level.isDay() && !level.isRaining() && !level.isThundering();
        if (!sunnyDay) return true; // 夜晚 / 下雨 / 雷雨：照常攻擊

        // 晴朗白天：只有 target 在水中才允許
        return target.isInWaterRainOrBubble();
    }

    public void addGoals() {
        if(mob instanceof PathfinderMob pathfinderMob){
            mob.goalSelector.addGoal(1, new DrownedLikeGoToWaterGoal(drownedLike, 1.0D)); // 白天找水（照抄 drowned 那個）
            mob.goalSelector.addGoal(5, new DrownedLikeGoToBeachGoal(drownedLike, 1.0D));
            mob.goalSelector.addGoal(6, new DrownedLikeSwimUpGoal(drownedLike, 1.0D, mob.level().getSeaLevel()));
            mob.goalSelector.addGoal(7, new RandomStrollGoal(pathfinderMob, 1.0D));
        }
        if(mob instanceof RangedAttackMob) mob.goalSelector.addGoal(2, new DrownedLikeTridentAttackGoal(this.drownedLike, 1.0D, 40, 10.0F));
    }

    public void tickSwimmingSwitch() {
        if (level.isClientSide) return;

        if (mob.isEffectiveAi() && mob.isInWater() && wantsToSwim()) {
            drownedLike.setNavigation(waterNavigation);
            mob.setSwimming(true);
        } else {
            drownedLike.setNavigation(groundNavigation);
            mob.setSwimming(false);
        }
    }

    public boolean tryTravel(Vec3 travelVec) {
        if (mob.isControlledByLocalInstance() && mob.isInWater() && wantsToSwim()) {
            mob.moveRelative(0.01F, travelVec);
            mob.move(net.minecraft.world.entity.MoverType.SELF, mob.getDeltaMovement());
            mob.setDeltaMovement(mob.getDeltaMovement().scale(0.9D));
            return true; // 表示已處理，不要再走 super.travel()
        }
        return false;
    }

    public boolean wantsToSwim() {
        if (searchingForLand) return true;
        LivingEntity target = mob.getTarget();
        return target != null && target.isInWater();
    }

    public boolean isPushedByFluid() {
        return !mob.isSwimming();
    }

    public boolean isSearchingForLand() { return searchingForLand; }

    public void setSearchingForLand(boolean v) {
        this.searchingForLand = v;
    }

    // --- MoveControl（從 Drowned 精簡移植） ---
    static final class DrownedMoveControl extends MoveControl {
        private final Mob mob;
        private final DrownedTrait trait;

        DrownedMoveControl(Mob mob, DrownedTrait trait) {
            super(mob);
            this.mob = mob;
            this.trait = trait;
        }

        @Override
        public void tick() {
            LivingEntity living = mob.getTarget();

            if (trait.wantsToSwim() && mob.isInWater()) {
                if ((living != null && living.getY() > mob.getY()) || trait.isSearchingForLand()) {
                    mob.setDeltaMovement(mob.getDeltaMovement().add(0.0D, 0.002D, 0.0D));
                }

                if (this.operation != Operation.MOVE_TO || mob.getNavigation().isDone()) {
                    mob.setSpeed(0.0F);
                    return;
                }

                double dx = wantedX - mob.getX();
                double dy = wantedY - mob.getY();
                double dz = wantedZ - mob.getZ();
                double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
                if (dist < 1.0E-6) return;

                dy /= dist;

                float yaw = (float)(Mth.atan2(dz, dx) * (180F / (float)Math.PI)) - 90.0F;
                mob.setYRot(this.rotlerp(mob.getYRot(), yaw, 90.0F));
                mob.yBodyRot = mob.getYRot();

                float base = (float)(this.speedModifier * mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float speed = Mth.lerp(0.125F, mob.getSpeed(), base);
                mob.setSpeed(speed);

                mob.setDeltaMovement(
                        mob.getDeltaMovement().add(speed * dx * 0.005D, speed * dy * 0.1D, speed * dz * 0.005D)
                );
            } else {
                if (!mob.onGround()) {
                    mob.setDeltaMovement(mob.getDeltaMovement().add(0.0D, -0.008D, 0.0D));
                }
                super.tick();
            }
        }
    }
}
