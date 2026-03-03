package com.vomiter.undeadvariants.common.entity.conversion;

import com.vomiter.undeadvariants.common.entity.conversion.death.DrownedKillVillagerRule;
import com.vomiter.undeadvariants.common.entity.conversion.death.HuskKillVillagerRule;
import com.vomiter.undeadvariants.common.entity.conversion.death.IKillVillagerConversionRule;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITransitionalEntity;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITwoPhaseConversionRule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public final class ConversionRules {
    private ConversionRules() {}
    private static final Map<ResourceLocation, ITwoPhaseConversionRule<?>> TWO_PHASE_BY_TRANSITIONAL = new IdentityHashMap<>();
    private static final List<ITwoPhaseConversionRule<?>> TWO_PHASE_RULES = new ArrayList<>();
    private static final List<IKillVillagerConversionRule> KILL_VILLAGER_RULES = new ArrayList<>();

    private static boolean initialized = false;

    /** 在 mod common setup（或主類 static init）呼叫一次 */
    public static void init() {
        if (initialized) return;
        initialized = true;

        // === 在這裡集中註冊 ===
        registerKillVillager(new DrownedKillVillagerRule());
        registerKillVillager(new HuskKillVillagerRule());
    }

    public static void register2phase(ITwoPhaseConversionRule<?> rule, ResourceLocation id) {
        if (rule == null) throw new NullPointerException("2phase rule");
        TWO_PHASE_RULES.add(rule);
        TWO_PHASE_BY_TRANSITIONAL.put(id, rule);
    }

    public static void register2phase(ITwoPhaseConversionRule<?> rule) {
        if (rule == null) throw new NullPointerException("2phase rule");
        TWO_PHASE_RULES.add(rule);
    }


    public static void registerKillVillager(IKillVillagerConversionRule rule) {
        if (rule == null) throw new NullPointerException("kill villager rule");
        KILL_VILLAGER_RULES.add(rule);
    }

    public static List<ITwoPhaseConversionRule<?>> twoPhaseRules() {
        return Collections.unmodifiableList(TWO_PHASE_RULES);
    }

    public static List<IKillVillagerConversionRule> killVillagerRules() {
        return Collections.unmodifiableList(KILL_VILLAGER_RULES);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mob> ITwoPhaseConversionRule<T> ruleForTransitional(ITransitionalEntity<T> entity) {
        if(entity instanceof Mob mob) return (ITwoPhaseConversionRule<T>) TWO_PHASE_BY_TRANSITIONAL.get(ForgeRegistries.ENTITY_TYPES.getKey(mob.getType()));
        else{
            throw new IllegalArgumentException("transitional entity must be a Mob! Illegal instance:" + entity.getClass().getName());
        }
    }


    // private static void sortAll() { ... } // 需要優先度再做
}
