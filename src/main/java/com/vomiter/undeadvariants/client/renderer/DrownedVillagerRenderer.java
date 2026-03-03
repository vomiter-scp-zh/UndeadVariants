package com.vomiter.undeadvariants.client.renderer;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.client.renderer.AbstractVillagerLikeHumanoidRenderer;
import com.vomiter.undeadvariants.client.model.DrownedVillagerModel;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedVillager;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ZombieVillagerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.jetbrains.annotations.NotNull;

public class DrownedVillagerRenderer
        extends AbstractVillagerLikeHumanoidRenderer<DrownedVillager, DrownedVillagerModel> {
    private static final ResourceLocation TEXTURE;

    static {
        assert ModEntityTypes.DROWNED_VILLAGER.getId() != null;
        TEXTURE = Helpers.textureId(ModEntityTypes.DROWNED_VILLAGER.getId());
    }

    public DrownedVillagerRenderer(EntityRendererProvider.Context ctx) {
        super(ctx,
                new DrownedVillagerModel(ctx.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)),
                0.5F,
                TEXTURE,
                "undeadvariants",
                true,
                DrownedVillager.class,
                e -> "drowned");
        addClothesLayer();
    }

    public static class DrowningZombieVillagerRenderer extends ZombieVillagerRenderer {
        public DrowningZombieVillagerRenderer(EntityRendererProvider.Context ctx) {
            super(ctx);
        }

        @Override
        protected boolean isShaking(@NotNull ZombieVillager entity) {
            return true;
        }
    }
}
