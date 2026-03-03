package com.vomiter.undeadvariants.common.entity.spawn;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.undeadvariants.common.entity.group.ZombifiedPiglinBrute;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

import java.util.Optional;

public class ZombifiedNearBasionRule implements ISpawnReplaceRule<ZombifiedPiglinBrute>{
    @Override
    public Class<ZombifiedPiglinBrute> targetClass() {
        return ZombifiedPiglinBrute.class;
    }

    @Override
    public Optional<Float> getChance(ServerLevel level, ZombifiedPiglinBrute mob, MobSpawnEvent.FinalizeSpawn event) {
        TagKey<Structure> BASTIONS =
                TagKey.create(Registries.STRUCTURE, Helpers.undeadVariantsId("bastions"));
        final ChunkPos center = new ChunkPos(mob.blockPosition());
        final int basions = StructureQuery.countUniqueStartsInChunkRadiusCached(level, center, BASTIONS, 2);

        float chance = Math.min(0.35f, basions * 0.05f);
        return chance <= 0f ? Optional.empty() : Optional.of(chance);
    }

    @Override
    public Optional<? extends Mob> createReplacement(ServerLevel level, ZombifiedPiglinBrute original, MobSpawnEvent.FinalizeSpawn event) {
        final var type = ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.get();
        final var e = type.create(level);
        return Optional.ofNullable(e);
    }

}
