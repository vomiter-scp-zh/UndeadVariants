package com.vomiter.undeadvariants.common.entity.spawn;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.StructureManager;

import java.util.*;

public final class StructureQuery
{
    private static final LinkedHashMap<Long, Integer> LRU = new LinkedHashMap<>(256, 0.75f, true);

    private StructureQuery() {}

    public static int countUniqueStartsInChunkRadiusCached(ServerLevel level, ChunkPos center, TagKey<Structure> tag, int chunkRadius)
    {
        final MinecraftServer server = level.getServer();
        final int playerCount = server.getPlayerList().getPlayerCount();
        final int maxSize = Math.max(64, playerCount * 64);

        final long key = makeKey(level, center, tag, chunkRadius);

        final Integer cached = LRU.get(key);
        if (cached != null) {
            trimToMax(maxSize);
            return cached;
        }

        final int computed = countUniqueStartsInChunkRadius(level, center, tag, chunkRadius);
        LRU.put(key, computed);

        trimToMax(maxSize);
        return computed;
    }

    public static int countUniqueStartsInChunkRadius(ServerLevel level, ChunkPos center, TagKey<Structure> tag, int chunkRadius)
    {
        final StructureManager sm = level.structureManager();
        final Set<Long> uniqueStarts = new HashSet<>();

        for (int dz = -chunkRadius; dz <= chunkRadius; dz++) {
            for (int dx = -chunkRadius; dx <= chunkRadius; dx++) {
                final ChunkPos cp = new ChunkPos(center.x + dx, center.z + dz);

                final int x = cp.getMiddleBlockX();
                final int z = cp.getMiddleBlockZ();
                final int y = level.getSeaLevel();
                final BlockPos probe = new BlockPos(x, y, z);

                final StructureStart start = sm.getStructureWithPieceAt(probe, tag);
                if (start != StructureStart.INVALID_START) {
                    uniqueStarts.add(start.getChunkPos().toLong());
                }
            }
        }
        return uniqueStarts.size();
    }

    private static void trimToMax(int maxSize)
    {
        while (LRU.size() > maxSize) {
            final Iterator<Map.Entry<Long, Integer>> it = LRU.entrySet().iterator();
            if (!it.hasNext()) break;
            it.next();
            it.remove();
        }
    }

    private static long makeKey(ServerLevel level, ChunkPos center, TagKey<Structure> tag, int chunkRadius)
    {
        // dimension
        final long dimHash = level.dimension().location().hashCode();

        // tag mobCivicsId（TagKey#location 1.20.1 可用）
        final ResourceLocation tagId = tag.location();
        final long tagHash = tagId.hashCode();

        // 把 (dim, chunk, tag, radius) 混成一個 long
        long k = 1469598103934665603L; // FNV offset basis
        k = fnv1a64(k, dimHash);
        k = fnv1a64(k, center.toLong());
        k = fnv1a64(k, tagHash);
        k = fnv1a64(k, chunkRadius);
        return k;
    }

    private static long fnv1a64(long hash, long v) {
        hash ^= v;
        hash *= 1099511628211L;
        return hash;
    }
}
