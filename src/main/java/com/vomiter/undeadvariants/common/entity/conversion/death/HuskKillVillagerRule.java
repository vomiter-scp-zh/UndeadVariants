package com.vomiter.undeadvariants.common.entity.conversion.death;

import com.vomiter.undeadvariants.common.entity.group.husk.HuskVillager;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.npc.Villager;

public final class HuskKillVillagerRule implements IKillVillagerConversionRule {
    private static final String GUARD = "UV_ConvGuard_V_to_HV";

    @Override public String guardTag() { return GUARD; }

    @Override
    public EntityType<? extends Mob> outcomeType() {
        return ModEntityTypes.HUSK_VILLAGER.get();
    }

    @Override
    public boolean matchesAttacker(ServerLevel level, Villager villager, LivingEntity attacker) {
        // 這裡用最保守、最不會誤判的條件：必須是 Husk
        return attacker.getType() == EntityType.HUSK || attacker instanceof Husk || attacker instanceof HuskVillager;
    }

    @Override
    public void afterConvert(ServerLevel level, Villager original, LivingEntity converted, LivingEntity attacker) {
        // 沿用你 Drowned 那套：播放轉化音效（1026）
        if (!attacker.isSilent()) {
            level.levelEvent(null, 1026, attacker.blockPosition(), 0);
        }
    }
}
