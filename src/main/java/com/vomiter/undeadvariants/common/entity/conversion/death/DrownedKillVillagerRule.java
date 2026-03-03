package com.vomiter.undeadvariants.common.entity.conversion.death;

import com.vomiter.undeadvariants.common.entity.group.drowned.IDrownedLike;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.monster.Drowned;

public final class DrownedKillVillagerRule implements IKillVillagerConversionRule {
    private static final String GUARD = "UV_ConvGuard_ZV_to_DV";

    @Override public String guardTag() { return GUARD; }

    @Override
    public EntityType<? extends Mob> outcomeType() {
        return ModEntityTypes.DROWNED_VILLAGER.get();
    }

    @Override
    public boolean matchesAttacker(ServerLevel level, Villager villager, LivingEntity attacker) {
        return attacker.getType() == EntityType.DROWNED
                || attacker instanceof Drowned
                || attacker instanceof IDrownedLike;
    }

    @Override
    public void afterConvert(ServerLevel level, Villager original, LivingEntity converted, LivingEntity attacker) {
        if (!attacker.isSilent()) {
            level.levelEvent(null, 1026, attacker.blockPosition(), 0);
        }
    }
}
