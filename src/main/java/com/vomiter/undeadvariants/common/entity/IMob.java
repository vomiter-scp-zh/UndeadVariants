package com.vomiter.undeadvariants.common.entity;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.jetbrains.annotations.NotNull;

public interface IMob {
    MoveControl getMoveControl();
    void setMoveControl(MoveControl moveControl);

    PathNavigation getNavigation();
    void setNavigation(PathNavigation pathNavigation);

    static AttributeSupplier.@NotNull Builder createAttributes() {
        return null;
    }
}
