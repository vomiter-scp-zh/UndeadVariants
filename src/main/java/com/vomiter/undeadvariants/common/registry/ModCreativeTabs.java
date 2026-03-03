package com.vomiter.undeadvariants.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            ModRegistries.createRegistry(Registries.CREATIVE_MODE_TAB);

    public static final RegistryObject<CreativeModeTab> UNDEAD_VARIANTS = TABS.register(
            "undead_variants",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.undeadvariants"))
                    .icon(() -> new ItemStack(ModItems.HEADS.get(HeadOwner.DROWNED_VILLAGER).get()))
                    .displayItems((params, output) -> {
                        // 頭顱
                        //output.accept(ModItems.DROWNED_VILLAGER_HEAD.get());

                        // Spawn Eggs
                        output.accept(ModItems.ZOMBIFIED_PIGLIN_BRUTE_SPAWN_EGG.get());
                        output.accept(ModItems.HUSK_VILLAGER_SPAWN_EGG.get());
                        output.accept(ModItems.DROWNED_PIGLIN_SPAWN_EGG.get());
                        output.accept(ModItems.DROWNED_VILLAGER_SPAWN_EGG.get());
                        output.accept(ModItems.ZOMBIE_WANDERING_TRADER_SPAWN_EGG.get());

                        for (HeadOwner headOwner : HeadOwner.values()) {
                            if(!headOwner.isShouldProcess()) continue;
                            output.accept(ModBlocks.HEADS.get(headOwner).get());
                        }

                    })
                    .build()
    );
}
