package com.vomiter.undeadvariants.common.entity.conversion.trigger.rule;

import com.vomiter.undeadvariants.common.entity.conversion.trigger.AbstractTwoPhaseDrowningRule;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedPiglin;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;

public final class SubmergingZombifiedPiglinRule extends AbstractTwoPhaseDrowningRule<ZombifiedPiglin> {

    @Override protected String prefix() { return "UV_ZP2DP_"; }
    @Override public Class<ZombifiedPiglin> sourceClass() { return ZombifiedPiglin.class; }

    // 只排除 final；transitional 不要排除
    @Override public boolean isAlreadyTarget(ZombifiedPiglin entity) {
        return entity instanceof DrownedPiglin;
    }

    @Override public EntityType<? extends Mob> sourceType() {
        return EntityType.ZOMBIFIED_PIGLIN;
    }

    @Override public EntityType<? extends Mob> transitionalType() {
        return ModEntityTypes.DROWNING_PIGLIN.get();
    }

    @Override public EntityType<? extends Mob> finalType() {
        return ModEntityTypes.DROWNED_PIGLIN.get();
    }

    @Override
    public void afterStart(ServerLevel level, ZombifiedPiglin oldEntity, LivingEntity newEntity) {
        super.afterStart(level, oldEntity, newEntity);
    }

    @Override
    public void afterFinish(ServerLevel level, ZombifiedPiglin oldEntity, LivingEntity newEntity) {
        super.afterFinish(level, oldEntity, newEntity);
    }
}
