package com.vomiter.undeadvariants.common.event;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.HashMap;
import java.util.Map;

public final class UVMobHunterHook {
    private static final ResourceLocation KILL_A_MOB =
            Helpers.minecraftId("adventure/kill_a_mob");

    public static void init(){
        PROXY_CRITERION_MAP.put(ModEntityTypes.ZOMBIE_WANDERING_TRADER.get(), "minecraft:zombie_villager");
        PROXY_CRITERION_MAP.put(ModEntityTypes.DROWNED_VILLAGER.get(), "minecraft:drowned");
        PROXY_CRITERION_MAP.put(ModEntityTypes.DROWNING_ZOMBIE_VILLAGER.get(), "minecraft:zombie_villager");
        PROXY_CRITERION_MAP.put(ModEntityTypes.DROWNED_PIGLIN.get(), "minecraft:zombified_piglin");
        PROXY_CRITERION_MAP.put(ModEntityTypes.DROWNING_PIGLIN.get(), "minecraft:zombified_piglin");
        PROXY_CRITERION_MAP.put(ModEntityTypes.HUSK_VILLAGER.get(), "minecraft:husk");
        PROXY_CRITERION_MAP.put(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get(), "minecraft:piglin_brute");
    }

    private static final Map<EntityType<?>, String> PROXY_CRITERION_MAP = new HashMap<>();

    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity victim = event.getEntity();
        if (!(victim.level() instanceof ServerLevel level)) return;

        if (victim.getType() != ModEntityTypes.DROWNED_VILLAGER.get()) return;

        Entity killer = event.getSource().getEntity();
        if (!(killer instanceof ServerPlayer player)) return;

        Advancement adv = player.server.getAdvancements().getAdvancement(KILL_A_MOB);
        if (adv == null) return;

        var progress = player.getAdvancements().getOrStartProgress(adv);
        var proxy = PROXY_CRITERION_MAP.get(victim.getType());
        if (progress.isDone()) return;
        if (isRemaining(progress, proxy)) {
            player.getAdvancements().award(adv, proxy);
        }
    }

    private static boolean isRemaining(AdvancementProgress progress, String criterion) {
        for (String c : progress.getRemainingCriteria()) {
            if (c.equals(criterion)) return true;
        }
        return false;
    }

}
