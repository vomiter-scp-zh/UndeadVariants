package com.vomiter.undeadvariants.data.loot;

import com.vomiter.mobcivics.Helpers;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ModLootTables extends LootTableProvider {

    public ModLootTables(PackOutput packOutput) {
        super(packOutput,
                Set.of(
                ),
                List.of(
                        new SubProviderEntry(EntityLootTables::new, LootContextParamSets.ENTITY),
                        new SubProviderEntry(ModBlockLootTable::new, LootContextParamSets.BLOCK)
                )
        );
    }

    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationcontext) {
    }
}
