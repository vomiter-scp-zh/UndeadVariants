package com.vomiter.undeadvariants.common.event;

import com.vomiter.undeadvariants.common.entity.spawn.DrownedNearVillageRule;
import com.vomiter.undeadvariants.common.entity.spawn.HuskNearVillageRule;
import com.vomiter.undeadvariants.common.entity.spawn.SpawnReplaceEngine;
import net.minecraftforge.event.entity.living.MobSpawnEvent;

public final class SpawnReplaceHandlers
{
    private static final SpawnReplaceEngine ENGINE = new SpawnReplaceEngine();

    static {
        ENGINE.register(new DrownedNearVillageRule());
        ENGINE.register(new HuskNearVillageRule());
        // ENGINE.register(new HuskNearDesertPyramidRule());
        // ENGINE.register(new JungleTemplePhantomVariantRule());
    }

    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        ENGINE.onFinalizeSpawn(event);
    }
}
