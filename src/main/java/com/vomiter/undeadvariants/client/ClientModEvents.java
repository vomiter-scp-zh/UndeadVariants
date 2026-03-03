package com.vomiter.undeadvariants.client;

import com.vomiter.undeadvariants.client.renderer.*;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ClientModEvents {
    public static void init(IEventBus modBus){
        modBus.addListener(ClientModEvents::onRegisterRenderers);
    }

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        huskVillager(event);
        drownedVillager(event);
        drownedPiglin(event);
        zombifiedPiglinBrute(event);
        zombieWanderingTrader(event);
    }

    static void zombieWanderingTrader(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(
                ModEntityTypes.ZOMBIE_WANDERING_TRADER.get(),
                ZombieWanderingTraderRenderer::new
        );
    }

    static void zombifiedPiglinBrute(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(
                ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get(),
                ZombifiedPiglinBruteRenderer::new
        );
    }

    static void huskVillager(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(
                ModEntityTypes.HUSK_VILLAGER.get(),
                HuskVillagerRenderer::new
        );
    }

    static void drownedPiglin(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(
                ModEntityTypes.DROWNED_PIGLIN.get(),
                DrownedPiglinRenderer::new
        );
        event.registerEntityRenderer(
                ModEntityTypes.DROWNING_PIGLIN.get(),
                DrownedPiglinRenderer.DrowningZombifiedPiglinRenderer::new
        );
    }

    static void drownedVillager(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(
                ModEntityTypes.DROWNED_VILLAGER.get(),
                DrownedVillagerRenderer::new
        );
        event.registerEntityRenderer(
                ModEntityTypes.DROWNING_ZOMBIE_VILLAGER.get(),
                DrownedVillagerRenderer.DrowningZombieVillagerRenderer::new
        );
    }
}
