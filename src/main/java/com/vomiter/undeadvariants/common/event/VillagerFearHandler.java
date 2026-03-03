package com.vomiter.undeadvariants.common.event;

import com.vomiter.mobcivics.api.common.entity.IVillagerThreat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent;

public class VillagerFearHandler {

    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof IVillagerThreat threat)) return;
        if (!threat.villagerFearEnabled(entity)) return;

        if (!(entity.level() instanceof ServerLevel level)) return;

        int scanInterval = Math.max(1, threat.villagerFearScanIntervalTicks());
        if ((entity.tickCount % scanInterval) != 0) return;

        double radius = Math.max(0.0D, threat.villagerFearRadius());
        if (radius <= 0.0D) return;

        int ttl = Math.max(1, threat.villagerFearMemoryTtlTicks());
        boolean requireLos = threat.villagerFearRequiresLineOfSight();

        AABB box = entity.getBoundingBox().inflate(radius, 4.0D, radius);
        double radiusSqr = radius * radius;

        for (Villager villager : level.getEntitiesOfClass(Villager.class, box)) {
            if (villager.distanceToSqr(entity) > radiusSqr) continue;

            if (requireLos && !villager.getSensing().hasLineOfSight(entity)) continue;

            Brain<Villager> brain = villager.getBrain();

            var currentOpt = brain.getMemory(MemoryModuleType.NEAREST_HOSTILE);
            if (currentOpt.isPresent()) {
                LivingEntity current = currentOpt.get();

                if(current.isAlive()){
                    double curDist = villager.distanceToSqr(current);
                    double newDist = villager.distanceToSqr(entity);
                    if (curDist <= newDist) continue;
                }

            }

            brain.setMemoryWithExpiry(MemoryModuleType.NEAREST_HOSTILE, entity, ttl);
        }
    }
}
