package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.client.model.ThornbackModel;
import com.malignant.iter.common.entity.ThornbackEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;


public class ThornbackRenderer extends MobRenderer<ThornbackEntity, ThornbackModel<ThornbackEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/thornback.png");

    public ThornbackRenderer(EntityRendererProvider.Context context) {
        super(context, new ThornbackModel<>(context.bakeLayer(ThornbackModel.LAYER_LOCATION)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(ThornbackEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(ThornbackEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        poseStack.popPose();
    }
}