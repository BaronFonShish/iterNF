package com.malignant.iter.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;
import org.joml.Matrix4f;

public abstract class BulletRenderer<T extends Projectile> extends EntityRenderer<T> {

    protected float scale = 0.25f;

    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        poseStack.pushPose();

        poseStack.translate(0.0D, 0.0D, 0.0D);

        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());

        poseStack.scale(scale, scale, scale);

        ResourceLocation texture = this.getTextureLocation(entity);

        VertexConsumer vertexConsumer = buffer.getBuffer(
                net.minecraft.client.renderer.RenderType.entityTranslucent(texture)
        );

        Matrix4f matrix4f = poseStack.last().pose();

        float halfSize = 0.5f;
        float u0 = 0.0f;
        float v0 = 0.0f;
        float u1 = 1.0f;
        float v1 = 1.0f;

        vertexConsumer.addVertex(matrix4f, -halfSize, -halfSize, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(u0, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0.0f, 0.0f, 1.0f);

        vertexConsumer.addVertex(matrix4f, halfSize, -halfSize, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(u1, v1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0.0f, 0.0f, 1.0f);

        vertexConsumer.addVertex(matrix4f, halfSize, halfSize, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(u1, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0.0f, 0.0f, 1.0f);

        vertexConsumer.addVertex(matrix4f, -halfSize, halfSize, 0.0f)
                .setColor(255, 255, 255, 255)
                .setUv(u0, v0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(packedLight)
                .setNormal(0.0f, 0.0f, 1.0f);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public abstract ResourceLocation getTextureLocation(T entity);
}