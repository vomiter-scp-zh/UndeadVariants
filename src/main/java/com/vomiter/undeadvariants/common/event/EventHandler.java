package com.vomiter.undeadvariants.common.event;

import com.vomiter.undeadvariants.common.command.ModCommand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class EventHandler {
    public static void init(){
        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(KillVillagerConversionHooks::onLivingConvertPre);
        bus.addListener(KillVillagerConversionHooks::onLivingDeath);
        bus.addListener(EventHandler::onRegisterCommands);
        bus.addListener(CustomConversionHandler::onLivingTick);
        bus.addListener(VillagerFearHandler::onLivingTick);
        bus.addListener(SpawnReplaceHandlers::onFinalizeSpawn);
        bus.addListener(UVMobHunterHook::onLivingDeath);
        bus.addListener(CustomConversionHandler::onPiglinBruteConversion);
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommand.register(event.getDispatcher());
    }


}
