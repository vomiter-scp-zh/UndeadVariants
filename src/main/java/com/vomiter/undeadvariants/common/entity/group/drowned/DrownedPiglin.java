package com.vomiter.undeadvariants.common.entity.group.drowned;

import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITransitionalEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DrownedPiglin
        extends ZombifiedPiglin implements IDrownedLike, RangedAttackMob {
    final DrownedTrait trait;
    public DrownedPiglin(EntityType<? extends ZombifiedPiglin> p_34427_, Level p_34428_) {
        super(p_34427_, p_34428_);
        trait = new DrownedTrait(this);
        this.addDrownedGoals();
    }

    @Override
    public boolean isSunSensitive(){
        return false;
    }

    @Override
    public DrownedTrait drownedTrait() {
        return trait;
    }

    @Override
    public void setMoveControl(MoveControl moveControl) {
        this.moveControl = moveControl;
    }

    @Override
    public void setNavigation(PathNavigation pathNavigation) {
        this.navigation = pathNavigation;
    }

    @Override
    public boolean isPushedByFluid() {
        return trait.isPushedByFluid();
    }

    public static class DrowningZombifiedPiglin
            extends ZombifiedPiglin implements ITransitionalEntity<DrowningZombifiedPiglin> {
        public DrowningZombifiedPiglin(EntityType<? extends ZombifiedPiglin> type, Level level) {
            super(type, level);
        }
        @Override
        protected @NotNull Component getTypeName() {
            return Component.translatable(getRule().sourceType().getDescriptionId());
        }
    }

}
