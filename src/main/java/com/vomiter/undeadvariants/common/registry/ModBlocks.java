package com.vomiter.undeadvariants.common.registry;

import com.vomiter.mobcivics.common.block.SkullLikeBlock;
import com.vomiter.mobcivics.common.block.WallSkullLikeBlock;
import com.vomiter.mobcivics.common.registry.ModBlockEntities;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS
            = ModRegistries.createRegistry(ForgeRegistries.BLOCKS);

    public static final Map<HeadOwner, RegistryObject<Block>> WALL_HEADS = new EnumMap<>(HeadOwner.class);
    public static final Map<HeadOwner, RegistryObject<Block>> HEADS = new EnumMap<>(HeadOwner.class);
    static {
        var headProperties = BlockBehaviour.Properties
                .copy(Blocks.PLAYER_HEAD)
                .strength(1F);

        for (HeadOwner headOwner : HeadOwner.values()) {
            if(!headOwner.isShouldProcess()) continue;
            HEADS.put(headOwner,
                    BLOCKS.register(
                            headOwner.headName(),
                            () -> new SkullLikeBlock(headProperties)
                    )
            );
            WALL_HEADS.put(headOwner,
                    BLOCKS.register(
                            headOwner.wallHeadName(),
                            () -> new WallSkullLikeBlock(headProperties)
                    )
            );
            ModBlockEntities.validHeads.add(HEADS.get(headOwner));
            ModBlockEntities.validHeads.add(WALL_HEADS.get(headOwner));

        }
    }
}
