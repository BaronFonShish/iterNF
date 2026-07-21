package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.client.model.GoblinModel;
import com.malignant.iter.client.model.HobGoblinModel;
import com.malignant.iter.common.entity.GoblinEntity;
import com.malignant.iter.common.entity.HobGoblinEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;


public class HobGoblinRenderer extends MobRenderer<HobGoblinEntity, HobGoblinModel<HobGoblinEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/tech_hobgoblin.png");

    public HobGoblinRenderer(EntityRendererProvider.Context context) {
        super(context, new HobGoblinModel<>(context.bakeLayer(HobGoblinModel.LAYER_LOCATION)), 0.9f);
    }

    @Override
    public ResourceLocation getTextureLocation(HobGoblinEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(HobGoblinEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        poseStack.popPose();
    }
}