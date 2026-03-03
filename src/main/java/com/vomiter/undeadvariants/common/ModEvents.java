package com.vomiter.undeadvariants.common;

import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ModEvents {

    public static void init(IEventBus modBus){
        modBus.addListener(ModEntityTypes::onEntityAttributes);
    }

}
