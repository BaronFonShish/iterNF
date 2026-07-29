package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.client.model.GoblinSharpshooterModel;
import com.malignant.iter.client.model.GoblinWarriorModel;
import com.malignant.iter.common.entity.GoblinSharpshooterEntity;
import com.malignant.iter.common.entity.GoblinWarriorEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;


public class GoblinSharpshooterRenderer extends MobRenderer<GoblinSharpshooterEntity, GoblinSharpshooterModel<GoblinSharpshooterEntity>> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/goblin_sharpshooter.png");

    public GoblinSharpshooterRenderer(EntityRendererProvider.Context context) {
        super(context, new GoblinSharpshooterModel<>(context.bakeLayer(GoblinSharpshooterModel.LAYER_LOCATION)), 0.3f);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(GoblinSharpshooterEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(GoblinSharpshooterEntity entity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferIn, int packedLightIn) {
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        poseStack.popPose();
    }
}