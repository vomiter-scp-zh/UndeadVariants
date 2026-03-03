package com.vomiter.undeadvariants.common.entity.conversion.trigger.rule;

import com.vomiter.undeadvariants.common.entity.conversion.trigger.AbstractTwoPhaseDrowningRule;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedVillager;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombieVillager;

public final class SubmergingZombieVillagerRule extends AbstractTwoPhaseDrowningRule<ZombieVillager> {

    @Override protected String prefix() { return "UV_ZV2DV_"; }
    @Override public Class<ZombieVillager> sourceClass() { return ZombieVillager.class; }

    // 只排除 final；transitional 不要排除
    @Override public boolean isAlreadyTarget(ZombieVillager entity) {
        return entity instanceof DrownedVillager;
    }

    @Override public EntityType<? extends Mob> sourceType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override public EntityType<? extends Mob> transitionalType() {
        return ModEntityTypes.DROWNING_ZOMBIE_VILLAGER.get();
    }

    @Override public EntityType<? extends Mob> finalType() {
        return ModEntityTypes.DROWNED_VILLAGER.get();
    }

    @Override
    public void afterStart(ServerLevel level, ZombieVillager oldEntity, LivingEntity newEntity) {
        if (newEntity instanceof ZombieVillager zv) {
            zv.setVillagerData(oldEntity.getVillagerData());
        }
    }

    @Override
    public void afterFinish(ServerLevel level, ZombieVillager oldEntity, LivingEntity newEntity) {
        if (newEntity instanceof ZombieVillager zv) {
            zv.setVillagerData(oldEntity.getVillagerData());
        }
    }
}
