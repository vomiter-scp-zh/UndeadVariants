package com.vomiter.undeadvariants.common.registry;

import com.vomiter.undeadvariants.common.entity.conversion.ConversionRules;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.ITwoPhaseConversionRule;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.rule.SubmergingHuskVillagerRule;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.rule.SubmergingZombieVillagerRule;
import com.vomiter.undeadvariants.common.entity.conversion.trigger.rule.SubmergingZombifiedPiglinRule;
import com.vomiter.undeadvariants.common.entity.group.ZombieWanderingTrader;
import com.vomiter.undeadvariants.common.entity.group.ZombifiedPiglinBrute;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedPiglin;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedVillager;
import com.vomiter.undeadvariants.common.entity.group.husk.HuskVillager;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public class ModEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES
            = ModRegistries.createRegistry(ForgeRegistries.ENTITY_TYPES);

    private static final Map<RegistryObject<? extends EntityType<? extends LivingEntity>>,
            java.util.function.Supplier<AttributeSupplier.Builder>> ATTRIBUTE_MAP = new HashMap<>();

    public static <T extends LivingEntity> RegistryObject<EntityType<T>> registerLiving(
            String name,
            EntityType.Builder<T> builder,
            java.util.function.Supplier<AttributeSupplier.Builder> attributes
    ) {
        RegistryObject<EntityType<T>> type = ENTITY_TYPES.register(
                name,
                () -> builder.build(name)
        );

        ATTRIBUTE_MAP.put(type, attributes);
        return type;
    }

    public static <T extends LivingEntity> RegistryObject<EntityType<T>> registerTransitional(
            String name,
            EntityType.Builder<T> builder,
            java.util.function.Supplier<AttributeSupplier.Builder> attributes,
            ITwoPhaseConversionRule<?> rule
    ) {
        RegistryObject<EntityType<T>> type = ENTITY_TYPES.register(
                name,
                () -> builder.build(name)
        );

        ATTRIBUTE_MAP.put(type, attributes);
        ConversionRules.register2phase(rule, type.getId());
        return type;
    }

    public static final RegistryObject<EntityType<ZombieWanderingTrader>> ZOMBIE_WANDERING_TRADER =
            registerLiving(
                    "zombie_wandering_trader",
                    EntityType.Builder.of(ZombieWanderingTrader::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                    , ZombieWanderingTrader::createAttributes
            );

    public static final RegistryObject<EntityType<ZombifiedPiglinBrute>> ZOMBIFIED_PIGLIN_BRUTE =
            registerLiving(
                    "zombified_piglin_brute",
                    EntityType.Builder.of(ZombifiedPiglinBrute::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .fireImmune()
                    , ZombifiedPiglinBrute::createAttributes
            );



    public static final RegistryObject<EntityType<HuskVillager>> HUSK_VILLAGER =
            registerLiving(
                    "husk_villager",
                    EntityType.Builder.of(HuskVillager::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                    , HuskVillager::createAttributes
            );
    static {
        ConversionRules.register2phase(new SubmergingHuskVillagerRule());
    }


    public static final RegistryObject<EntityType<DrownedPiglin>> DROWNED_PIGLIN =
            registerLiving(
                    "drowned_piglin",
                    EntityType.Builder.of(DrownedPiglin::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                    , DrownedPiglin::createAttributes
            );

    public static final RegistryObject<EntityType<DrownedPiglin.DrowningZombifiedPiglin>> DROWNING_PIGLIN =
            registerTransitional(
                    "drowning_piglin",
                    EntityType.Builder.of(DrownedPiglin.DrowningZombifiedPiglin::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                            .fireImmune(),
                    ZombifiedPiglin::createAttributes,
                    new SubmergingZombifiedPiglinRule()
            );


    public static final RegistryObject<EntityType<DrownedVillager>> DROWNED_VILLAGER =
            registerLiving(
                    "drowned_villager",
                    EntityType.Builder.of(DrownedVillager::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f)
                    , DrownedVillager::createAttributes
            );

    public static final RegistryObject<EntityType<DrownedVillager.DrowningZombieVillager>> DROWNING_ZOMBIE_VILLAGER =
            registerTransitional(
                    "drowning_zombie_villager",
                    EntityType.Builder.of(DrownedVillager.DrowningZombieVillager::new, MobCategory.MONSTER)
                            .sized(0.6f, 1.95f),
                    DrownedVillager.DrowningZombieVillager::createAttributes,
                    new SubmergingZombieVillagerRule()
            );

    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        ATTRIBUTE_MAP.forEach((type, supplier) -> {
            event.put(type.get(), supplier.get().build());
        });
    }
}
