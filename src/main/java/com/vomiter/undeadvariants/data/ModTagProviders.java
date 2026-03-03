package com.vomiter.undeadvariants.data;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.UndeadVariants;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModTagProviders {
    DataGenerator generator;
    PackOutput output;
    CompletableFuture<HolderLookup.Provider> lookupProvider;
    ExistingFileHelper helper;
    public ModTagProviders(GatherDataEvent event){
        generator = event.getGenerator();
        output = generator.getPackOutput();
        lookupProvider = event.getLookupProvider();
        helper = event.getExistingFileHelper();

        var blockTags = new BlockTags();
        var itemTags = new ItemTags(blockTags);
        var entityTags = new EntityTags();
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), itemTags);
        generator.addProvider(event.includeClient(), entityTags);

    }

    interface TagUtil<T> {
        ResourceKey<Registry<T>> key();

        default TagKey<T> create(ResourceLocation id){
            return TagKey.create(
                    key(),
                    id
            );
        }

    }

    class EntityTags extends EntityTypeTagsProvider implements TagUtil<EntityType<?>>{
        public EntityTags() {
            super(output, lookupProvider, UndeadVariants.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {
            tag(create(Helpers.minecraftId("axolotl_always_hostiles")))
                    .add(ModEntityTypes.DROWNED_VILLAGER.get())
                    .add(ModEntityTypes.DROWNED_PIGLIN.get());

            tag(create(Helpers.minecraftId("burn_in_daylight")))
                    .add(ModEntityTypes.ZOMBIE_WANDERING_TRADER.get())
                    .add(ModEntityTypes.DROWNED_VILLAGER.get())
                    .add(ModEntityTypes.DROWNING_ZOMBIE_VILLAGER.get());

            tag(create(Helpers.minecraftId("zombies")))
                    .add(ModEntityTypes.ZOMBIE_WANDERING_TRADER.get())
                    .add(ModEntityTypes.DROWNED_VILLAGER.get())
                    .add(ModEntityTypes.DROWNING_ZOMBIE_VILLAGER.get())
                    .add(ModEntityTypes.DROWNED_PIGLIN.get())
                    .add(ModEntityTypes.DROWNING_PIGLIN.get())
                    .add(ModEntityTypes.HUSK_VILLAGER.get())
                    .add(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get())
            ;

            tag(create(Helpers.id("mobcivics", "zombies_that_piglins_fear")))
                    .add(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get())
                    .add(ModEntityTypes.DROWNED_PIGLIN.get())
            ;
        }

        @Override
        public ResourceKey<Registry<EntityType<?>>> key() {
            return Registries.ENTITY_TYPE;
        }
    }


    class BlockTags extends BlockTagsProvider{

        public BlockTags() {
            super(output, lookupProvider, UndeadVariants.MOD_ID, helper);
        }

        @Override
        protected void addTags(HolderLookup.@NotNull Provider lookupProvider) {

        }
    }

    class ItemTags extends ItemTagsProvider {
        public ItemTags(BlockTags blockTags) {
            super(output, lookupProvider, blockTags.contentsGetter(), UndeadVariants.MOD_ID, helper);
        }
        static TagKey<Item> create(ResourceLocation id){
            return TagKey.create(
                    Registries.ITEM,
                    id
            );
        }
        public static TagKey<Item> test = create(Helpers.undeadVariantsId("test"));

        @Override
        protected void addTags(HolderLookup.@NotNull Provider p_256380_) {
            //tag(test).add(Items.ACACIA_BOAT);
        }
    }


}
