package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public interface IDrowningRule<T extends Mob> extends ITriggeredTwoPhaseRule<T> {
    @Override
    default boolean isInTriggerCondition(T entity) {
        return entity.isEyeInFluid(FluidTags.WATER);
    }

    @Override
    default void afterFinish(ServerLevel level, T oldEntity, LivingEntity newEntity) {
        level.levelEvent(null, 1040, newEntity.blockPosition(), 0);
    }
}
