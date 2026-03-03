package com.vomiter.undeadvariants.common.entity.group.drowned;

import com.vomiter.undeadvariants.common.entity.IMob;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public interface IDrownedLike extends IMob, RangedAttackMob {
    DrownedTrait drownedTrait();

    default Mob getMob() {
        return drownedTrait().getMob();
    }

    default boolean closeToNextPos() {
        Path path = this.getNavigation().getPath();
        if (path != null) {
            BlockPos blockpos = path.getTarget();
            if (blockpos != null) {
                double d0 = this.getMob().distanceToSqr(blockpos.getX(), blockpos.getY(), blockpos.getZ());
                return d0 < 4.0D;
            }
        }

        return false;
    }


    default boolean shouldAggro(@Nullable LivingEntity target) {
        if (target == null) return false;
        return drownedTrait().shouldAggro(target);
    }

    default void addDrownedGoals() {
        drownedTrait().addGoals();
    }

    default void tickDrownedTraits() {
        drownedTrait().tickSwimmingSwitch();
    }

    default boolean tryDrownedTravel(Vec3 travelVec) {
        return drownedTrait().tryTravel(travelVec);
    }

    default void useGroundNavigation(){
        drownedTrait().useGroundNavigation();
    }

    default void useWaterNavigation(){
        drownedTrait().useWaterNavigation();
    }

    default void setSearchingForLand(boolean b){
        drownedTrait().setSearchingForLand(b);
    }

    default void performRangedAttack(LivingEntity p_32356_, float p_32357_) {
        ThrownTrident throwntrident = new ThrownTrident(getMob().level(), getMob(), new ItemStack(Items.TRIDENT));
        double d0 = p_32356_.getX() - getMob().getX();
        double d1 = p_32356_.getY(0.3333333333333333D) - throwntrident.getY();
        double d2 = p_32356_.getZ() - getMob().getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        throwntrident.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - getMob().level().getDifficulty().getId() * 4));
        getMob().playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (getMob().getRandom().nextFloat() * 0.4F + 0.8F));
        getMob().level().addFreshEntity(throwntrident);
    }


}
