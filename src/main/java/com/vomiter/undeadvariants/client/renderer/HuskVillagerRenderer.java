package com.vomiter.undeadvariants.client.renderer;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.client.renderer.AbstractVillagerLikeHumanoidRenderer;
import com.vomiter.undeadvariants.client.model.HuskVillagerModel;
import com.vomiter.undeadvariants.common.entity.group.husk.HuskVillager;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HuskVillagerRenderer extends AbstractVillagerLikeHumanoidRenderer<HuskVillager, HuskVillagerModel> {
    private static final ResourceLocation TEXTURE;

    static {
        assert ModEntityTypes.HUSK_VILLAGER.getId() != null;
        TEXTURE = Helpers.textureId(ModEntityTypes.HUSK_VILLAGER.getId());
    }

    public HuskVillagerRenderer(EntityRendererProvider.Context ctx) {
        super(
                ctx,
                new HuskVillagerModel(ctx.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)),
                0.5F,
                TEXTURE,
                "undeadvariants",
                true,
                HuskVillager.class,
                e -> "husk"
        );
        addClothesLayer();
    }

    @Override
    protected boolean isShaking(@NotNull HuskVillager entity) {
        return entity.getEntityData().get(HuskVillager.UV_CONVERTING);
    }

}