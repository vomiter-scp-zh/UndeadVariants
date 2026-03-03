package com.vomiter.undeadvariants.common.registry;

import com.vomiter.mobcivics.common.item.SkullLikeItem;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class ModItems {
    static DeferredRegister<Item> ITEMS
            = ModRegistries.createRegistry(ForgeRegistries.ITEMS);

    public static final Map<HeadOwner, RegistryObject<Item>> HEADS = new EnumMap<>(HeadOwner.class);
    static {
        for (HeadOwner headOwner : HeadOwner.values()) {
            if(!headOwner.isShouldProcess()) continue;
            HEADS.put(headOwner, ITEMS.register(headOwner.headName(),
                    () -> new SkullLikeItem(
                            ModBlocks.HEADS.get(headOwner).get(),
                            ModBlocks.WALL_HEADS.get(headOwner).get(),
                            new Item.Properties().rarity(Rarity.UNCOMMON), Direction.DOWN)));

        }
    }

    /*
    public static final RegistryObject<Item> DROWNED_VILLAGER_HEAD =
            ITEMS.register("drowned_villager_head", () -> new Item(new Item.Properties()));

     */

    public static final RegistryObject<Item> ZOMBIE_WANDERING_TRADER_SPAWN_EGG =
            ITEMS.register("zombie_wandering_trader_spawn_egg", () ->
                    new ForgeSpawnEggItem(
                            ModEntityTypes.ZOMBIE_WANDERING_TRADER,
                            0x243A3A,
                            15377456,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> ZOMBIFIED_PIGLIN_BRUTE_SPAWN_EGG =
            ITEMS.register("zombified_piglin_brute_spawn_egg", () ->
                    new ForgeSpawnEggItem(
                            ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE,
                            15373203,
                            0xB38A2E,
                            new Item.Properties()
                    )
            );

    public static final RegistryObject<Item> HUSK_VILLAGER_SPAWN_EGG =
            ITEMS.register("husk_villager_spawn_egg", () ->
                    new ForgeSpawnEggItem(
                            ModEntityTypes.HUSK_VILLAGER,
                            0x6B5A3A,
                            0xB8A06A,
                            new Item.Properties()
                    )
            );


    public static final RegistryObject<Item> DROWNED_PIGLIN_SPAWN_EGG =
            ITEMS.register("drowned_piglin_spawn_egg", () ->
                    new ForgeSpawnEggItem(
                            ModEntityTypes.DROWNED_PIGLIN,
                            0x243A3A,
                            0x6F8F8F,
                            new Item.Properties()
                    )
            );


    public static final RegistryObject<Item> DROWNED_VILLAGER_SPAWN_EGG =
            ITEMS.register("drowned_villager_spawn_egg", () ->
                    new ForgeSpawnEggItem(
                            ModEntityTypes.DROWNED_VILLAGER,
                            0x2F3E3E,
                            0x8AA6A6,
                            new Item.Properties()
                    )
            );

}
