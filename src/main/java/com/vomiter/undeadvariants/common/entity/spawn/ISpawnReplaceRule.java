package com.vomiter.undeadvariants.common.entity.spawn;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import java.util.Optional;

public interface ISpawnReplaceRule<T extends Mob>
{
    Class<T> targetClass();

    /** 是否要處理這次 spawn（例如 NATURAL/CHUNK_GENERATION、或你想允許 SPAWNER 等） */
    default boolean shouldHandle(ServerLevel level, MobSpawnType reason, T mob, MobSpawnEvent.FinalizeSpawn event) {
        return reason == MobSpawnType.NATURAL || reason == MobSpawnType.CHUNK_GENERATION;
    }

    /**
     * 回傳「命中機率」或 Optional.empty() 表示不適用。
     * 你可以在這裡做結構計數、biome 判斷、距離判斷…等等。
     */
    Optional<Float> getChance(ServerLevel level, T mob, MobSpawnEvent.FinalizeSpawn event);

    /** 命中後要換成哪個 entity type，或乾脆回傳 empty 表示不換 */
    Optional<? extends Mob> createReplacement(ServerLevel level, T original, MobSpawnEvent.FinalizeSpawn event);

    /** 是否真的命中（預設用機率抽） */
    default boolean rollHit(RandomSource rand, float chance) {
        return chance > 0f && rand.nextFloat() < chance;
    }
}
