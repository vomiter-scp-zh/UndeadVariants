package com.vomiter.undeadvariants.common.registry;

import java.util.Locale;

public enum HeadOwner {
    DROWNED,
    DROWNED_VILLAGER(true),
    HUSK,
    HUSK_VILLAGER(true),
    ZOMBIFIED_PIGLIN,
    ZOMBIFIED_PIGLIN_BRUTE,
    DROWNED_PIGLIN;

    private boolean shouldProcess;

    HeadOwner() {}
    HeadOwner(boolean b) {
        shouldProcess = b;
    }

    public String headName(){
        return name().toLowerCase(Locale.ROOT) + "_head";
    }

    public String wallHeadName(){
        return name().toLowerCase(Locale.ROOT) + "_wall_head";
    }

    public boolean isShouldProcess(){
        return shouldProcess;
    }

}
