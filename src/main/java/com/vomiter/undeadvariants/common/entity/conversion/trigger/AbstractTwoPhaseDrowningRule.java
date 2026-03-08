package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;

public abstract class AbstractTwoPhaseDrowningRule<F extends Mob> extends AbstractTwoPhaseConversionRule<F> implements IDrowningRule<F> {
    @Override
    public void beforeFinish(ServerLevel level, F entity) {
        super.beforeFinish(level, entity);
        entity.playSound(SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED);
    }
}
