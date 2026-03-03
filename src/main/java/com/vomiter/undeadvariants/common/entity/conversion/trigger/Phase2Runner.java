package com.vomiter.undeadvariants.common.entity.conversion.trigger;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.Nullable;

public final class Phase2Runner {
    private Phase2Runner() {}

    public static <T extends Mob> @Nullable LivingEntity finishNow(
            ServerLevel level,
            T entity,
            ITwoPhaseConversionRule<T> rule
    ) {
        var pd = entity.getPersistentData();

        // 必須已在 converting 狀態才允許 finish（避免誤呼叫）
        if (!pd.getBoolean(rule.tagConverting())) return null;
        if (pd.getInt(rule.tagConvertTime()) >= 0) return null;
        boolean allowed = ForgeEventFactory.canLivingConvert(
                entity,
                rule.finalType(),
                (timer) -> rule.setTimer(entity, timer)
        );
        if (!allowed) return null;

        rule.beforeFinish(level, entity);

        LivingEntity converted = entity.convertTo(rule.finalType(), true);
        if (converted == null) return null;

        rule.afterFinish(level, entity, converted);
        rule.clearAfterFinish(converted);
        return converted;
    }
}
