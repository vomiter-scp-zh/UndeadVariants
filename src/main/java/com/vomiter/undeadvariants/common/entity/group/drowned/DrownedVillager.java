package com.vomiter.undeadvariants.common.entity.group.drowned;

import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.api.common.entity.IVillagerThreat;
import com.vomiter.undeadvariants.common.entity.ai.DrownedMeleeAttackGoalLike;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITransitionalEntity;
import com.vomiter.undeadvariants.common.registry.HeadOwner;
import com.vomiter.undeadvariants.common.registry.ModBlocks;
import com.vomiter.undeadvariants.common.registry.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MoveThroughVillageGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class DrownedVillager extends ZombieVillager implements IDrownedLike, IVillagerThreat, IVillagerDataHolder {
    final DrownedTrait drownedTrait;
    public DrownedVillager(EntityType<? extends ZombieVillager> type, Level level) {
        super(type, level);
        drownedTrait = new DrownedTrait(this);
        drownedTrait.init();
        this.addDrownedGoals();
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return ZombieVillager.createAttributes();
    }

    @Override
    protected @NotNull ItemStack getSkull() {
        return new ItemStack(ModBlocks.HEADS.get(HeadOwner.DROWNED_VILLAGER).get());
    }


    @Override
    public DrownedTrait drownedTrait() {
        return drownedTrait;
    }

    @Override
    public void updateSwimming() {
        drownedTrait.tickSwimmingSwitch();
    }

    @Override
    public void travel(@NotNull Vec3 travelVec) {
        if (!drownedTrait.tryTravel(travelVec)) {
            super.travel(travelVec);
        }
    }

    @Override
    public boolean isPushedByFluid() {
        return drownedTrait.isPushedByFluid();
    }

    @Override
    public void setMoveControl(MoveControl moveControl) {
        this.moveControl = moveControl;
    }

    @Override
    public void setNavigation(PathNavigation pathNavigation) {
        this.navigation = pathNavigation;
    }

    public @NotNull MoveControl getMoveControl(){
        return moveControl;
    }

    public @NotNull PathNavigation getNavigation(){
        return navigation;
    }

    @Override
    public boolean villagerFearEnabled(LivingEntity self) {
        if(self instanceof Mob mob) return mob.getTarget() instanceof Villager;
        return false;
    }

    /** 1) 讓所有以 canAttack 為基礎的目標選擇先被擋掉 */
    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return super.canAttack(target) && drownedTrait.shouldAggro(target);
    }

    /** 2) 連已經鎖到的目標也要在晴朗白天把它清掉（避免目標狀態改變後還繼續追） */
    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            LivingEntity target = this.getTarget();
            if (target != null && !shouldAggro(target)) {
                this.setTarget(null);
                this.setAggressive(false);
                this.getNavigation().stop();
            }
        }
    }


    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(6, new MoveThroughVillageGoal(this, 1.0D, true, 4, this::canBreakDoors));
        this.goalSelector.addGoal(2, new DrownedMeleeAttackGoalLike(this, 1.0D, false)); // 近戰

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(ZombifiedPiglin.class));

        // 玩家：用 predicate 閘門（白天晴朗只鎖水中玩家）
        this.targetSelector.addGoal(2,
                new NearestAttackableTargetGoal<>(
                this,
                        Player.class,
                        10,
                        true,
                        false,
                        this::shouldAggro
        ));

        // 村民/鐵魔像：「保留對村民的攻擊行為」=> 這兩條照 Zombie 原樣即可
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));

        // 烏龜小孩
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public VillagerData mobcivics$getVillagerData() {
        return this.getVillagerData();
    }


    public static class DrowningZombieVillager
            extends ZombieVillager implements IVillagerThreat, ITransitionalEntity<DrowningZombieVillager> {

        public DrowningZombieVillager(EntityType<? extends ZombieVillager> type, Level level) {
            super(type, level);
        }

        public static AttributeSupplier.@NotNull Builder createAttributes() {
            return ZombieVillager.createAttributes();
        }

        @Override
        protected @NotNull Component getTypeName() {
            return Component.translatable(getRule().sourceType().getDescriptionId());
        }

        // ===== IVillagerThreat =====

        @Override
        public boolean villagerFearEnabled(LivingEntity self) {
            if (self instanceof Mob mob) return mob.getTarget() instanceof Villager;
            return false;
        }
    }
}
