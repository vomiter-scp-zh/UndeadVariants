package com.vomiter.undeadvariants.common.entity.spawn;

import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import java.util.Optional;

public final class HuskNearVillageRule implements ISpawnReplaceRule<Husk>
{
    @Override public Class<Husk> targetClass() { return Husk.class; }

    @Override
    public Optional<Float> getChance(ServerLevel level, Husk mob, MobSpawnEvent.FinalizeSpawn event)
    {
        final ChunkPos center = new ChunkPos(mob.blockPosition());
        final int villages = StructureQuery.countUniqueStartsInChunkRadiusCached(level, center, StructureTags.VILLAGE, 2);

        float chance = Math.min(0.35f, villages * 0.05f);
        return chance <= 0f ? Optional.empty() : Optional.of(chance);
    }

    @Override
    public Optional<? extends Mob> createReplacement(ServerLevel level, Husk original, MobSpawnEvent.FinalizeSpawn event)
    {
        final var type = ModEntityTypes.HUSK_VILLAGER.get();
        final var e = type.create(level);
        return Optional.ofNullable(e);
    }
}
