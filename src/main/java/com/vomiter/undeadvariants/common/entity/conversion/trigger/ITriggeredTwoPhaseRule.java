package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;

public interface ITriggeredTwoPhaseRule<T extends Mob> extends ITwoPhaseConversionRule<T> {
    boolean isInTriggerCondition(T entity);

    default void onTriggerTick(ServerLevel level, T entity, int triggerTime) {}
    default int triggerResetValue() { return -1; } // 你現在用 -1
}
