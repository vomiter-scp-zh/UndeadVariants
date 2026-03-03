package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * 通用兩段式轉化規則介面：
 * Stage A 達標後進入 transitional，Stage B 倒數後進入 final。
 *
 * 這裡只描述「狀態標記/時間/目標型別/Hook」，
 * 不包含「如何判定達標」（那是 drowning 或其他子介面做的事）。
 */
public interface ITwoPhaseConversionRule<F extends Mob> {
    // 用於持久化狀態的 tag key（務必每個 rule 唯一）
    String tagConverting();
    String tagConvertTime();
    String tagTriggerTime();

    int triggerTicksTotal();
    int convertTicksTotal();

    /**
     * 避免重複處理。
     * 建議語意：只排除「final」，讓 transitional 仍能進入 Stage B 倒數。
     */
    boolean isAlreadyTarget(F entity);

    Class<F> sourceClass();
    EntityType<? extends Mob> sourceType();
    default boolean matchesSource(Mob entity) {
        // 預設：只吃「精準的 EntityType」，避免 subclass 因 isInstance 被誤傷
        // 同時用 sourceClass 做 safety check，避免有人 override sourceType 指到不相容 class 時直接炸
        return sourceClass().isInstance(entity) && entity.getType() == sourceType();
    }


    /**
     * 過渡態（預設=final → 不使用過渡態）
     */
    default EntityType<? extends Mob> transitionalType() { return finalType(); }

    /**
     * 最終態（必填）
     */
    EntityType<? extends Mob> finalType();

    // ---- Hooks ----

    /** Stage B 倒數期間每 tick（粒子等） */
    default void onConvertingTick(ServerLevel level, F entity) {}

    /** Stage A -> transitional 後（搬資料、初始化倒數顯示等） */
    default void afterStart(ServerLevel level, F oldEntity, LivingEntity newEntity) {}

    LivingEntity startConversion(ServerLevel level, F entity);

    LivingEntity finishConversion(ServerLevel level, F entity);

    /** Stage B -> final 前（音效事件等） */
    default void beforeFinish(ServerLevel level, F entity) {}

    /** Stage B -> final 後（搬資料、清理等） */
    default void afterFinish(ServerLevel level, F oldEntity, LivingEntity newEntity) {}

    default void setTimer(F from, int time) {
        from.getPersistentData().putInt(tagConvertTime(), time);
    }

    default void clearAfterFinish(LivingEntity e) {
        var pd = e.getPersistentData();
        pd.remove(tagConverting());
        pd.remove(tagConvertTime());
        pd.remove(tagTriggerTime());
    }
}
