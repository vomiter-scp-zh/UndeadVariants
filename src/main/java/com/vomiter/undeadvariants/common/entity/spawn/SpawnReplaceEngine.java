package com.vomiter.undeadvariants.common.entity.spawn;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SpawnReplaceEngine
{
    private final List<ISpawnReplaceRule<?>> rules = new ArrayList<>();

    public <T extends Mob> void register(ISpawnReplaceRule<T> rule) {
        rules.add(rule);
    }

    public void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        final Mob mob = event.getEntity();
        final MobSpawnType reason = event.getSpawnType();

        for (ISpawnReplaceRule<?> raw : rules)
        {
            if (!raw.targetClass().isInstance(mob)) continue;
            handleOneRule(raw, level, reason, mob, event);
            // 一次 spawn 通常只想套用一條規則；命中或不命中都可以 break
            // 這裡選擇：只要有「適用且命中」就停止
            if (event.isSpawnCancelled()) return;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Mob> void handleOneRule(ISpawnReplaceRule<?> raw, ServerLevel level, MobSpawnType reason, Mob mob, MobSpawnEvent.FinalizeSpawn event)
    {
        final ISpawnReplaceRule<T> rule = (ISpawnReplaceRule<T>) raw;
        final T typed = (T) mob;

        if (!rule.shouldHandle(level, reason, typed, event)) return;

        final Optional<Float> chanceOpt = rule.getChance(level, typed, event);
        if (chanceOpt.isEmpty()) return;

        final float chance = chanceOpt.get();
        if (chance <= 0f) return;

        if (!rule.rollHit(typed.getRandom(), chance)) return;

        final Optional<? extends Mob> replacementOpt = rule.createReplacement(level, typed, event);
        if (replacementOpt.isEmpty()) return;

        final Mob replacement = replacementOpt.get();
        doReplace(level, reason, typed, replacement, event);
    }

    private void doReplace(ServerLevel level, MobSpawnType reason, Mob original, Mob replacement, MobSpawnEvent.FinalizeSpawn event)
    {
        event.setSpawnCancelled(true);

        // 位置、旋轉、速度
        replacement.moveTo(original.getX(), original.getY(), original.getZ(), original.getYRot(), original.getXRot());
        replacement.setDeltaMovement(original.getDeltaMovement());

        // 沿用同一套 spawn 初始化資料
        final DifficultyInstance difficulty = event.getDifficulty();
        final SpawnGroupData spawnData = event.getSpawnData();
        replacement.finalizeSpawn(event.getLevel(), difficulty, reason, spawnData, null);
        // 裝備
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            replacement.setItemSlot(slot, original.getItemBySlot(slot));
        }


        level.addFreshEntity(replacement);
        original.discard();
    }
}
