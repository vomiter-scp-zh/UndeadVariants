package com.vomiter.undeadvariants.data.models;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.UndeadVariants;
import com.vomiter.undeadvariants.common.registry.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, UndeadVariants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // ===== Spawn Eggs =====
        spawnEgg(ModItems.ZOMBIFIED_PIGLIN_BRUTE_SPAWN_EGG);
        spawnEgg(ModItems.DROWNED_VILLAGER_SPAWN_EGG);
        spawnEgg(ModItems.HUSK_VILLAGER_SPAWN_EGG);
        spawnEgg(ModItems.DROWNED_PIGLIN_SPAWN_EGG);
        spawnEgg(ModItems.ZOMBIE_WANDERING_TRADER_SPAWN_EGG);
    }

    private void spawnEgg(RegistryObject<? extends Item> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    @SuppressWarnings("unused")
    private void simpleGenerated(RegistryObject<? extends Item> item, String texturePath) {
        // 需要時可用：item/generated + 自訂貼圖
        ModelFile parent = new ModelFile.UncheckedModelFile(mcLoc("item/generated"));
        getBuilder(item.getId().getPath())
                .parent(parent)
                .texture("layer0", Helpers.undeadVariantsId( "item/" + texturePath));
    }
}
