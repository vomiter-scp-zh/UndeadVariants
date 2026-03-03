package com.vomiter.undeadvariants.common.entity.conversion.death;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;

public interface IKillVillagerConversionRule {
    /** 用來做 persistent guard，避免同一隻村民重入/遞迴 */
    String guardTag();

    /** 這條規則想把村民轉成哪個 outcome entity type（例如 DROWNED_VILLAGER / HUSK_VILLAGER） */
    EntityType<? extends Mob> outcomeType();

    /** 判斷擊殺者是否符合這條規則（例：Drowned 或 IDrownedLike） */
    boolean matchesAttacker(ServerLevel level, Villager villager, LivingEntity attacker);

    /** （可選）限制難度、維度、biome 等，就在這裡擋 */
    default boolean additionalChecks(ServerLevel level, Villager villager) {
        return true;
    }

    /** （可選）某些變體想要不同的 spawn finalize 行為，可以覆寫 */
    default void afterConvert(ServerLevel level, Villager original, LivingEntity converted, LivingEntity attacker) {}
}
