package com.vomiter.undeadvariants.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vomiter.mobcivics.Helpers;
import com.vomiter.mobcivics.client.renderer.AbstractWanderingTraderHumanoidRenderer;
import com.vomiter.undeadvariants.client.model.ZombieWanderingTraderModel;
import com.vomiter.undeadvariants.common.entity.group.ZombieWanderingTrader;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ZombieWanderingTraderRenderer
        extends AbstractWanderingTraderHumanoidRenderer<ZombieWanderingTrader, ZombieWanderingTraderModel> {
    public ZombieWanderingTraderRenderer(
            EntityRendererProvider.Context ctx
    ) {
        super(
                ctx,
                new ZombieWanderingTraderModel(ctx.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)),
                0.5F,
                Helpers.textureId(Helpers.minecraftId("zombie_villager/zombie_villager")),
                Helpers.textureId(Helpers.undeadVariantsId("zombie_wandering_trader")),
                ZombieWanderingTrader.class
        );
        addClothesLayer();
    }

    @Override
    public void render(ZombieWanderingTrader mob, float p_115456_, float p_115457_, PoseStack p_115458_, MultiBufferSource p_115459_, int p_115460_) {
        if(mob.isInvisible()) return;
        super.render(mob, p_115456_, p_115457_, p_115458_, p_115459_, p_115460_);
    }
}
