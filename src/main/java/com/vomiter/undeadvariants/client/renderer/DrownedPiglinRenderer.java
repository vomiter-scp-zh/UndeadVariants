package com.vomiter.undeadvariants.client.renderer;

import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.client.renderer.AbstractPiglinLikeRenderer;
import com.vomiter.undeadvariants.client.model.DrownedPiglinModel;
import com.vomiter.undeadvariants.common.entity.group.drowned.DrownedPiglin;
import com.vomiter.undeadvariants.common.registry.ModEntityTypes;
import net.minecraft.client.model.PiglinModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DrownedPiglinRenderer
        extends AbstractPiglinLikeRenderer<DrownedPiglin, DrownedPiglinModel> {

    public DrownedPiglinRenderer(EntityRendererProvider.Context ctx) {
        super(ctx,
                new DrownedPiglinModel(ctx.bakeLayer(ModelLayers.ZOMBIFIED_PIGLIN)),
                ModelLayers.ZOMBIFIED_PIGLIN_INNER_ARMOR,
                ModelLayers.ZOMBIFIED_PIGLIN_OUTER_ARMOR,
                0.5F,
                Helpers.textureId(Objects.requireNonNull(ModEntityTypes.DROWNED_PIGLIN.getId())),
                true
        );
    }

    public static class DrowningZombifiedPiglinRenderer extends PiglinRenderer {
        public DrowningZombifiedPiglinRenderer(EntityRendererProvider.Context ctx) {
            super(ctx,
                    ModelLayers.ZOMBIFIED_PIGLIN,
                    ModelLayers.ZOMBIFIED_PIGLIN_INNER_ARMOR,
                    ModelLayers.ZOMBIFIED_PIGLIN_OUTER_ARMOR,
                    true
            );
            this.model = new PiglinModel<>(ctx.bakeLayer(ModelLayers.ZOMBIFIED_PIGLIN));
        }

        @Override
        protected boolean isShaking(@NotNull Mob entity) {
            // 不用同步狀態：過渡態存在期間永遠抖就好
            return true;
        }

        @Override
        public @NotNull ResourceLocation getTextureLocation(@NotNull Mob p_114482_) {
            return Helpers.textureId(
                    Helpers.minecraftId("piglin/zombified_piglin")
            );
        }

    }

}


