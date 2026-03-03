package com.vomiter.undeadvariants.data;

import com.vomiter.undeadvariants.data.loot.ModLootTables;
import com.vomiter.undeadvariants.data.models.ModBlockStateProvider;
import com.vomiter.undeadvariants.data.models.ModItemModelProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class UndeadDataGenerator {
    public static void generateData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new ModLootTables(output));
        new ModTagProviders(event);

        for (String locale : ModLangProvider.locales) {
            generator.addProvider(event.includeClient(), new ModLangProvider(output, locale));
        }
        generator.addProvider(event.includeClient(), new ModItemModelProvider(output, helper));
    }
}
