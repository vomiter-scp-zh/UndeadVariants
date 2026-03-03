package com.vomiter.undeadvariants.data.loot;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.UndeadVariants;
import com.vomiter.undeadvariants.common.registry.HeadOwner;
import com.vomiter.undeadvariants.common.registry.ModBlocks;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class ModBlockLootTable implements LootTableSubProvider {
    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> b) {
        for (HeadOwner headOwner : HeadOwner.values()) {
            if(!headOwner.isShouldProcess()) continue;
            b.accept(
                    Helpers.id(UndeadVariants.MOD_ID,  "blocks/" + ModBlocks.HEADS.get(headOwner).getId().getPath())
                    , LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(ModBlocks.HEADS.get(headOwner).get()))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );
            b.accept(
                    Helpers.id(UndeadVariants.MOD_ID,  "blocks/" + ModBlocks.WALL_HEADS.get(headOwner).getId().getPath())
                    , LootTable.lootTable().withPool(
                            LootPool.lootPool()
                                    .setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(ModBlocks.HEADS.get(headOwner).get()))
                                    .when(ExplosionCondition.survivesExplosion())
                    )
            );

        }
    }
}
