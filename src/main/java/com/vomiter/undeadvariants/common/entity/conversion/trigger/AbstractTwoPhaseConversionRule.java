package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.ForgeEventFactory;

public abstract class AbstractTwoPhaseConversionRule<T extends Mob> implements ITwoPhaseConversionRule<T> {

    protected abstract String prefix();

    @Override public final String tagConverting()  { return prefix() + "Converting"; }
    @Override public final String tagConvertTime() { return prefix() + "ConvertTime"; }
    @Override public final String tagTriggerTime() { return prefix() + "TriggerTime"; }

    @Override public int triggerTicksTotal() { return 600; }
    @Override public int convertTicksTotal() { return 300; }

    @Override public abstract Class<T> sourceClass();
    @Override public abstract boolean isAlreadyTarget(T entity);

    @Override
    public LivingEntity startConversion(ServerLevel level, T entity) {
        return TwoPhase.start(level, entity, this);
    }

    @Override
    public LivingEntity finishConversion(ServerLevel level, T entity) {
        return TwoPhase.finish(level, entity, this);
    }

    @Override
    public void afterStart(ServerLevel level, T oldEntity, LivingEntity newEntity) {
        ITwoPhaseConversionRule.super.afterStart(level, oldEntity, newEntity);

    }

    /**
     * 內建 two-phase runner
     * 用 ITwoPhaseConversionRule 當參數，避免綁死 abstract class。
     */
    public static final class TwoPhase {
        private TwoPhase() {}

        public static <T extends Mob> LivingEntity start(ServerLevel level, T entity, ITwoPhaseConversionRule<T> rule) {
            boolean allowed = ForgeEventFactory.canLivingConvert(entity, rule.finalType(), (timer) -> {});
            if (!allowed) return null;
            var pd = entity.getPersistentData();
            if (pd.getBoolean(rule.tagConverting())) return entity;


            // 若 transitional 與目前 type 相同：不轉實體，只進入「PD tag 狀態」
            if (entity.getType() == rule.transitionalType()) {
                rule.beforeStart(level, entity);
                markConverting(rule, entity);
                rule.afterStart(level, entity, entity);
                return entity;
            }

            rule.beforeStart(level, entity);
            LivingEntity converted = entity.convertTo(rule.transitionalType(), true);
            if (converted == null) return null;
            markConverting(rule, converted);
            rule.afterStart(level, entity, converted);
            return converted;
        }

        public static <T extends Mob> LivingEntity finish(ServerLevel level, T entity, ITwoPhaseConversionRule<T> rule) {
            return Phase2Runner.finishNow(level, entity, rule);
        }

        private static <T extends Mob> void markConverting(ITwoPhaseConversionRule<T> rule, LivingEntity e) {
            CompoundTag pd = e.getPersistentData();
            pd.putBoolean(rule.tagConverting(), true);
            pd.putInt(rule.tagConvertTime(), rule.convertTicksTotal());
            pd.remove(rule.tagTriggerTime());
        }
    }
}
