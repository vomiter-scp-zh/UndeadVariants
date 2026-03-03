package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import com.vomiter.undeadvariants.common.entity.conversion.ConversionRules;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.extensions.IForgeEntity;

public interface  ITransitionalEntity <T extends Mob> extends IForgeEntity {
    default ITwoPhaseConversionRule<T> getRule(){
        return ConversionRules.ruleForTransitional(this);
    }
}
