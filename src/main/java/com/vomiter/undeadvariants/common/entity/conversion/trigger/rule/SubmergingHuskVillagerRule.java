package com.vomiter.undeadvariants.common.entity.conversion.trigger.rule;

import com.vomiter.undeadvariants.common.entity.conversion.trigger.AbstractTwoPhaseDrowningRule;
import com.vomiter.undeadvariants.common.entity.group.husk.HuskVillager;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.jetbrains.annotations.NotNull;

public final class SubmergingHuskVillagerRule extends AbstractTwoPhaseDrowningRule<HuskVillager> {

    @Override protected @NotNull String prefix() { return "UV_HV2ZV_"; }

    @Override public Class<HuskVillager> sourceClass() { return HuskVillager.class; }

    /**
     * 只排除 final；transitional（同 type + PD tag）不要排除
     */
    @Override
    public boolean isAlreadyTarget(HuskVillager entity) {
        return entity.getType().equals(EntityType.ZOMBIE_VILLAGER);
    }

    @Override
    public EntityType<? extends Mob> sourceType() {
        return ModEntityTypes.HUSK_VILLAGER.get();
    }

    /**
     * transitional = source（同一個 entity type，用 PD tag 表示已進入 converting）
     */
    @Override
    public EntityType<? extends Mob> transitionalType() {
        return ModEntityTypes.HUSK_VILLAGER.get();
    }

    @Override
    public EntityType<? extends Mob> finalType() {
        return EntityType.ZOMBIE_VILLAGER;
    }

    @Override
    public void afterStart(ServerLevel level, HuskVillager oldEntity, LivingEntity newEntity) {
        // transitional 同 type，所以 newEntity 理論上就是 oldEntity
        if (newEntity instanceof HuskVillager hv) {
            hv.setVillagerData(oldEntity.getVillagerData());
            hv.getEntityData().set(HuskVillager.UV_CONVERTING, true);
        } else if (newEntity instanceof ZombieVillager zv) {
            zv.setVillagerData(oldEntity.getVillagerData());
        }
    }

    @Override
    public void afterFinish(ServerLevel level, HuskVillager oldEntity, LivingEntity newEntity) {
        if (newEntity instanceof ZombieVillager zv) {
            zv.setVillagerData(oldEntity.getVillagerData());
        }
    }
}
