package com.vomiter.undeadvariants.common.event;

import com.vomiter.undeadvariants.common.entity.conversion.ConversionRules;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITriggeredTwoPhaseRule;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITwoPhaseConversionRule;
import com.vomiter.undeadvariants.common.entity.group.ZombifiedPiglinBrute;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingConversionEvent;
import net.minecraftforge.event.entity.living.LivingEvent;

public class CustomConversionHandler {

    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        Level level0 = event.getEntity().level();
        if (level0.isClientSide) return;
        if (!(level0 instanceof ServerLevel level)) return;

        if (!(event.getEntity() instanceof Mob mob)) return;

        for (ITwoPhaseConversionRule<?> rule : ConversionRules.twoPhaseRules()) {
            handleTwoStageRule(level, mob, rule);
        }
    }

    static String GUARD_TAG = "UV_PB_2_ZPB";
    public static void onPiglinBruteConversion(LivingConversionEvent.Pre event){
        if(!(event.getEntity() instanceof PiglinBrute piglinBrute)) return;
        if(!event.getOutcome().equals(EntityType.ZOMBIFIED_PIGLIN)) return;
        if(!(piglinBrute.level() instanceof ServerLevelAccessor serverLevelAccessor)) return;
        event.setCanceled(true);
        final CompoundTag pd = piglinBrute.getPersistentData();
        if (pd.getBoolean(GUARD_TAG)) return;
        pd.putBoolean(GUARD_TAG, true);
        final ZombifiedPiglinBrute converted = piglinBrute.convertTo(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get(), false);
        if (converted == null) return;

        // finalizeSpawn：沿用原本的 ZombieGroupData
        SpawnGroupData groupData = new ZombifiedPiglin.ZombieGroupData(false, true);
        converted.finalizeSpawn(
                serverLevelAccessor,
                piglinBrute.level().getCurrentDifficultyAt(converted.blockPosition()),
                MobSpawnType.CONVERSION,
                groupData,
                null
        );

        // Forge Post
        float pitch = piglinBrute.getVoicePitch();
        ForgeEventFactory.onLivingConvert(piglinBrute, converted);
        converted.randomizeReinforcementsChanceAfterConversion();
        converted.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 0));
        converted.playSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED, 1.0F, pitch);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Mob> void handleTwoStageRule(ServerLevel level, Mob mob, ITwoPhaseConversionRule<T> rule) {
        // 這個 cast 是安全的：tickStageB 只會在 converting=true 的情況下繼續，
        // 而 converting 只會由此 rule 的 startConversion 設定（同一條 rule）
        T entity = (T) mob;

        // ===== 先跑 Stage B：不需要 matchesSource，但需要「已在 converting」+「型別合理」 =====
        if (tickStageB(level, entity, rule)) {
            return;
        }

        // ===== Stage A 才需要 source gating =====
        if (!rule.matchesSource(mob)) return;

        // 只排除 final；transitional 不要排除（但此時已不在 Stage B，代表沒 converting）
        if (rule.isAlreadyTarget(entity)) return;

        if (rule instanceof ITriggeredTwoPhaseRule<T> triggered) {
            tickTriggeredStageA(level, entity, triggered);
        }
    }

    /** 通用 two-phase：只處理倒數 -> finish */
    private static <T extends Mob> boolean tickStageB(ServerLevel level, T entity, ITwoPhaseConversionRule<T> rule) {
        CompoundTag pd = entity.getPersistentData();
        if (!pd.getBoolean(rule.tagConverting())) return false;

        // ★ Stage B 型別檢查：只允許 transitional（或 transitional==source 的特例）
        var type = entity.getType();
        var transitional = rule.transitionalType();
        var source = rule.sourceType();
        if (type != transitional && transitional != source) {
            return false;
        }

        int time = pd.getInt(rule.tagConvertTime());
        time--;
        pd.putInt(rule.tagConvertTime(), time);

        rule.onConvertingTick(level, entity);

        if (time < 0) {
            rule.finishConversion(level, entity);
        }
        return true;
    }

    private static <T extends Mob> void tickTriggeredStageA(ServerLevel level, T entity, ITriggeredTwoPhaseRule<T> rule) {
        CompoundTag pd = entity.getPersistentData();

        if (rule.isInTriggerCondition(entity)) {
            int t = pd.getInt(rule.tagTriggerTime());
            t++;
            pd.putInt(rule.tagTriggerTime(), t);

            if (t >= rule.triggerTicksTotal()) {
                rule.startConversion(level, entity);
            }
        } else {
            pd.putInt(rule.tagTriggerTime(), -1);
        }
    }
}
