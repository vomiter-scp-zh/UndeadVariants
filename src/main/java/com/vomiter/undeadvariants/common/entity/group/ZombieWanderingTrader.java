package com.vomiter.undeadvariants.common.entity.group;

import com.vomiter.mobcivics.api.client.IVillagerDataHolder;
import com.vomiter.mobcivics.api.common.entity.IVillagerThreat;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerData;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

public class ZombieWanderingTrader extends ZombieVillager implements IVillagerDataHolder, IVillagerThreat {
    public ZombieWanderingTrader(EntityType<? extends ZombieVillager> p_34368_, Level p_34369_) {
        super(p_34368_, p_34369_);
        setVillagerData(new VillagerData(VillagerType.PLAINS, VillagerProfession.NITWIT, 5));
    }
    private int milkBucketCooldownMax = 20 * 60 * 20;
    private int milkBucketCooldown = 0;
    private int invisibleCooldownMax = 20 * 60 * 20;
    private int invisibleCooldown = 0;


    public boolean hasNegativeEffects(){
        var effectList = this.getActiveEffects().stream().filter(
                e
                        -> !e.getEffect().equals(MobEffects.POISON) && !e.getEffect().equals(MobEffects.WITHER));
        return effectList.anyMatch(e -> e.getEffect().getCategory().equals(MobEffectCategory.HARMFUL));
    }

    public boolean shouldDrinkMilk(){
        boolean isDay = !this.level().dimensionType().hasFixedTime() && this.level().getDayTime() % 24000 <= 13000;
        return isDay && hasNegativeEffects() && milkBucketCooldown <= 0;
    }

    public boolean shouldInvisible(){
        boolean isNight = !this.level().dimensionType().hasFixedTime() && this.level().getDayTime() % 24000 > 13000;
        return isNight && invisibleCooldown <= 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(milkBucketCooldown > 0) milkBucketCooldown--;
        if(invisibleCooldown > 0) invisibleCooldown--;
    }

    @Override
    public VillagerData mobcivics$getVillagerData() {
        return new VillagerData(VillagerType.PLAINS, VillagerProfession.NITWIT, 5);
    }

    @Override
    protected void addBehaviourGoals() {
        super.addBehaviourGoals();
        this.goalSelector.addGoal(
                0,
                new ZWTInvisibleGoal(this)
        );
        this.goalSelector.addGoal(
                0,
                new ZWTDrinkMilkGoal(this)
        );
    }

    @Override
    public boolean villagerFearEnabled(LivingEntity self) {
        if (self instanceof Mob mob) return mob.getTarget() instanceof Villager;
        return false;
    }


    static class ZWTDrinkMilkGoal extends UseItemGoal<ZombieWanderingTrader>{

        ZombieWanderingTrader mob;
        public ZWTDrinkMilkGoal(ZombieWanderingTrader mob) {
            super(mob, new ItemStack(Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, ZombieWanderingTrader::shouldDrinkMilk);
            this.mob = mob;
        }

        @Override
        public void stop() {
            super.stop();
            mob.milkBucketCooldown = mob.milkBucketCooldownMax;
        }
    }

    static class ZWTInvisibleGoal extends UseItemGoal<ZombieWanderingTrader>{

        ZombieWanderingTrader mob;
        public ZWTInvisibleGoal(ZombieWanderingTrader mob) {
            super(mob, PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, ZombieWanderingTrader::shouldInvisible);
            this.mob = mob;
        }

        @Override
        public void stop() {
            super.stop();
            mob.invisibleCooldown = mob.invisibleCooldownMax;
        }
    }
}
