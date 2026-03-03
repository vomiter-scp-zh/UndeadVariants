package com.vomiter.undeadvariants.common.event;

import com.vomiter.undeadvariants.common.entity.conversion.ConversionRules;
import com.vomiter.undeadvariants.common.entity.conversion.death.IKillVillagerConversionRule;
import com.vomiter.undeadvariants.common.entity.group.ZombieWanderingTrader;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public final class KillVillagerConversionHooks {
    private KillVillagerConversionHooks() {}

    public static void onLivingDeath(LivingDeathEvent event){
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if(level.isClientSide()) return;
        if(entity instanceof WanderingTrader wanderingTrader){
            var attacker = event.getSource().getEntity();
            if(attacker instanceof Zombie){
                if ((level.getDifficulty() == Difficulty.NORMAL || level.getDifficulty() == Difficulty.HARD) && ForgeEventFactory.canLivingConvert(entity, ModEntityTypes.ZOMBIE_WANDERING_TRADER.get(), (timer) -> {})) {
                    if (level.getDifficulty() != Difficulty.HARD && level.random.nextBoolean()) {
                        return;
                    }

                    ZombieWanderingTrader zombieWanderingTrader = wanderingTrader.convertTo(ModEntityTypes.ZOMBIE_WANDERING_TRADER.get(), false);
                    if (zombieWanderingTrader != null) {
                        zombieWanderingTrader.finalizeSpawn((ServerLevelAccessor) level, level.getCurrentDifficultyAt(zombieWanderingTrader.blockPosition()), MobSpawnType.CONVERSION, new Zombie.ZombieGroupData(false, true), (CompoundTag)null);
                        net.minecraftforge.event.ForgeEventFactory.onLivingConvert(wanderingTrader, zombieWanderingTrader);
                        if (!attacker.isSilent()) {
                            level.levelEvent((Player)null, 1026, attacker.blockPosition(), 0);
                        }
                    }
                }
            }
        }
    }

    public static void onLivingConvertPre(LivingConversionEvent.Pre event) {
        final LivingEntity entity0 = event.getEntity();
        if (!(entity0.level() instanceof ServerLevel level)) return;

        // 只處理 Villager -> Zombie Villager 這條
        if (!(entity0 instanceof Villager villager)) return;
        if (event.getOutcome() != EntityType.ZOMBIE_VILLAGER) return;

        final Entity attackerEntity = villager.getLastHurtByMob();
        final LivingEntity attacker = (attackerEntity instanceof LivingEntity le) ? le : null;
        if (attacker == null) return;

        // 依序嘗試規則：第一個 match 的就改道（避免多條規則互打）
        for (IKillVillagerConversionRule rule : ConversionRules.killVillagerRules()) {
            if (!rule.additionalChecks(level, villager)) continue;

            final CompoundTag pd = villager.getPersistentData();
            if (pd.getBoolean(rule.guardTag())) continue;

            if (!rule.matchesAttacker(level, villager, attacker)) continue;

            // 命中規則：做改道
            pd.putBoolean(rule.guardTag(), true);

            // 取消原本 ZV
            event.setCanceled(true);

            // 讓其他模組有機會擋（Forge 的 canLivingConvert）
            if (!ForgeEventFactory.canLivingConvert(villager, rule.outcomeType(), (timer) -> {})) {
                return;
            }

            final Mob converted = villager.convertTo(rule.outcomeType(), false);
            if (converted == null) return;

            // finalizeSpawn：沿用原本的 ZombieGroupData
            SpawnGroupData groupData = new Zombie.ZombieGroupData(false, true);
            converted.finalizeSpawn(
                    level,
                    level.getCurrentDifficultyAt(converted.blockPosition()),
                    MobSpawnType.CONVERSION,
                    groupData,
                    null
            );

            // 搬移 Villager 資料（共通）
            copyVillagerData(villager, converted);

            // Forge Post
            ForgeEventFactory.onLivingConvert(villager, converted);

            // 給規則做額外處理（例如播音效、加 tag、給屬性）
            rule.afterConvert(level, villager, converted, attacker);

            // 命中一條就結束
            return;
        }
    }

    private static void copyVillagerData(Villager original, LivingEntity converted) {
        if (converted instanceof net.minecraft.world.entity.monster.ZombieVillager zv) {
            // 若是 ZombieVillager 的子類，這樣寫就很好用。
            zv.setVillagerData(original.getVillagerData());
            zv.setGossips(original.getGossips().store(NbtOps.INSTANCE));
            zv.setTradeOffers(original.getOffers().createTag());
            zv.setVillagerXp(original.getVillagerXp());

            zv.setCustomName(original.getCustomName());
            zv.setCustomNameVisible(original.isCustomNameVisible());
            zv.setInvulnerable(original.isInvulnerable());
        } else {
            // 如果變體不是 ZombieVillager 系
            // 改成讓變體實作一個介面來接這些資料。
            converted.setCustomName(original.getCustomName());
            converted.setCustomNameVisible(original.isCustomNameVisible());
            converted.setInvulnerable(original.isInvulnerable());
        }
    }
}
