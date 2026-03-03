package com.vomiter.undeadvariants.data.loot;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public class EntityLootTables implements LootTableSubProvider {

    private static final ResourceLocation DROWNED_TABLE =
            Helpers.id("minecraft", "entities/drowned");
    private static final ResourceLocation ZOMBIE_VILLAGER_TABLE =
            Helpers.id("minecraft", "entities/zombie_villager");
    private static final ResourceLocation HUSK_TABLE =
            Helpers.id("minecraft", "entities/husk");
    private static final ResourceLocation ZOMBIFIED_PIGLIN_TABLE =
            Helpers.id("minecraft", "entities/zombified_piglin");

    @Override
    public void generate(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b) {
        drownedVillager(b);
        drownedPiglin(b);
        huskVillager(b);
        zombifiedPiglinBrute(b);
        zombieWanderingTrader(b);
    }

    void zombieWanderingTrader(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b){
        ResourceLocation mob =
                Helpers.undeadVariantsId( "entities/" + ModEntityTypes.ZOMBIE_WANDERING_TRADER.getId().getPath());
        LootTable.Builder lootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(ZOMBIE_VILLAGER_TABLE))
                );
        b.accept(mob, lootTable);

    }


    void zombifiedPiglinBrute(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b){
        ResourceLocation mob =
                Helpers.undeadVariantsId( "entities/" + ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.getId().getPath());
        LootTable.Builder lootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(HUSK_TABLE))
                );
        b.accept(mob, lootTable);

    }

    void huskVillager(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b) {

        ResourceLocation huskVillagerLoot =
                Helpers.undeadVariantsId( "entities/husk_villager");

        LootTable.Builder huskVillagerLootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(HUSK_TABLE))
                );
        b.accept(huskVillagerLoot, huskVillagerLootTable);
    }



    void drownedPiglin(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b) {

        ResourceLocation drownedPiglinLoot =
                Helpers.undeadVariantsId( "entities/drowned_piglin");
        ResourceLocation drowningZombifiedPiglinLoot =
                Helpers.undeadVariantsId( "entities/drowning_zombified_piglin");

        LootTable.Builder drownedVillagerLootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(ZOMBIFIED_PIGLIN_TABLE))
                );
        LootTable.Builder drowningZombieVillagerLootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(ZOMBIFIED_PIGLIN_TABLE))
                );


        b.accept(drownedPiglinLoot, drownedVillagerLootTable);
        b.accept(drowningZombifiedPiglinLoot, drowningZombieVillagerLootTable);
    }


    void drownedVillager(@NotNull BiConsumer<ResourceLocation, LootTable.Builder> b) {

        ResourceLocation drownedVillagerLoot =
                Helpers.undeadVariantsId( "entities/drowned_villager");
        ResourceLocation drowningZombieVillagerLoot =
                Helpers.undeadVariantsId( "entities/drowning_zombie_villager");

        LootTable.Builder drownedVillagerLootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(DROWNED_TABLE))
                );
        LootTable.Builder drowningZombieVillagerLootTable = LootTable.lootTable()
                .withPool(
                        LootPool.lootPool()
                                .setRolls(ConstantValue.exactly(1.0F))
                                .add(LootTableReference.lootTableReference(ZOMBIE_VILLAGER_TABLE))
                );


        b.accept(drownedVillagerLoot, drownedVillagerLootTable);
        b.accept(drowningZombieVillagerLoot, drowningZombieVillagerLootTable);
    }

}
