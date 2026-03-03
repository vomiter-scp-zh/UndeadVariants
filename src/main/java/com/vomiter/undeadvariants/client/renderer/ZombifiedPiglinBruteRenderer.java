package com.vomiter.undeadvariants.client.renderer;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.client.renderer.AbstractPiglinLikeRenderer;
import com.vomiter.undeadvariants.common.entity.group.ZombifiedPiglinBrute;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

import java.util.Objects;

public class ZombifiedPiglinBruteRenderer
        extends AbstractPiglinLikeRenderer<ZombifiedPiglinBrute, PiglinModel<ZombifiedPiglinBrute>> {

    public ZombifiedPiglinBruteRenderer(EntityRendererProvider.Context ctx) {
        super(ctx,
                new PiglinModel<>(ctx.bakeLayer(ModelLayers.ZOMBIFIED_PIGLIN)),
                ModelLayers.ZOMBIFIED_PIGLIN_INNER_ARMOR,
                ModelLayers.ZOMBIFIED_PIGLIN_OUTER_ARMOR,
                0.5F,
                Helpers.textureId(Objects.requireNonNull(ModEntityTypes.ZOMBIFIED_PIGLIN_BRUTE.getId())),
                true
        );
    }
}


