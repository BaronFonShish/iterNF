package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.client.model.InsatiableModel;
import com.malignant.iter.common.entity.InsatiableEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;


public class InsatiableRenderer extends MobRenderer<InsatiableEntity, InsatiableModel<InsatiableEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/insatiable.png");

    public InsatiableRenderer(EntityRendererProvider.Context context) {
        super(context, new InsatiableModel<>(context.bakeLayer(InsatiableModel.LAYER_LOCATION)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(InsatiableEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(InsatiableEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        poseStack.popPose();
    }
}
