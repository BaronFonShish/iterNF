package com.malignant.iter.client.model;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.DarkSorcererEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;


public class ThornbackModel<T extends LivingEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "thornback"), "main");

    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart thorns;
    private final ModelPart left_thorns;
    private final ModelPart right_thorns;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart beard;
    private final ModelPart left_ear;
    private final ModelPart right_ear;
    private final ModelPart left_arm;
    private final ModelPart right_arm;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public ThornbackModel(ModelPart root) {
        this.body = root.getChild("body");
        this.thorns = this.body.getChild("thorns");
        this.left_thorns = this.body.getChild("left_thorns");
        this.right_thorns = this.body.getChild("right_thorns");
        this.head = this.body.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.beard = this.jaw.getChild("beard");
        this.left_ear = this.head.getChild("left_ear");
        this.right_ear = this.head.getChild("right_ear");
        this.left_arm = this.body.getChild("left_arm");
        this.right_arm = this.body.getChild("right_arm");
        this.left_leg = this.body.getChild("left_leg");
        this.right_leg = this.body.getChild("right_leg");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-13.0F, -28.0F, -17.0F, 26.0F, 28.0F, 22.0F, new CubeDeformation(0.0F))
                .texOffs(73, 96).addBox(-9.5F, -24.0F, 5.0F, 19.0F, 22.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 13.0F, -2.0F));

        PartDefinition thorns = body.addOrReplaceChild("thorns", CubeListBuilder.create().texOffs(0, 51).addBox(0.0F, -14.0F, -18.0F, 0.0F, 14.0F, 36.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, -24.0F, 4.0F));

        PartDefinition left_thorns = body.addOrReplaceChild("left_thorns", CubeListBuilder.create().texOffs(73, 51).addBox(0.0F, -11.0F, -16.5F, 0.0F, 11.0F, 33.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(5.0F, -24.0F, 2.5F, 0.0F, 0.0F, 0.3927F));

        PartDefinition right_thorns = body.addOrReplaceChild("right_thorns", CubeListBuilder.create().texOffs(73, 51).mirror().addBox(0.0F, -11.0F, -16.5F, 0.0F, 11.0F, 33.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(-5.0F, -24.0F, 2.5F, 0.0F, 0.0F, -0.3927F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 102).addBox(-8.0F, -5.0F, -11.0F, 16.0F, 7.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(98, 135).addBox(-10.0F, -5.1F, -9.0F, 20.0F, 0.0F, 6.0F, new CubeDeformation(0.001F))
                .texOffs(53, 135).addBox(-6.0F, -5.0F, -21.0F, 12.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 121).addBox(-6.0F, 2.0F, -21.0F, 12.0F, 3.0F, 14.0F, new CubeDeformation(0.001F))
                .texOffs(138, 25).addBox(3.0F, -1.0F, -19.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(55, 112).addBox(9.0F, -5.0F, -19.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(138, 25).mirror().addBox(-12.0F, -1.0F, -19.0F, 9.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(55, 112).mirror().addBox(-12.0F, -5.0F, -19.0F, 3.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -16.0F, -17.0F, 0.7854F, 0.0F, 0.0F));

        PartDefinition jaw = head.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(97, 35).addBox(-7.0F, 0.0F, -9.0F, 14.0F, 3.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 139).addBox(-4.0F, 0.0F, -19.0F, 8.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(140, 50).addBox(-4.0F, -2.0F, -19.0F, 8.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, -2.0F, 0.2182F, 0.0F, 0.0F));

        PartDefinition beard = jaw.addOrReplaceChild("beard", CubeListBuilder.create().texOffs(140, 63).addBox(0.0F, -11.0F, -38.0F, 0.0F, 6.0F, 12.0F, new CubeDeformation(0.001F)), PartPose.offset(0.0F, 14.0F, 19.0F));

        PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(53, 121).addBox(-2.5F, -7.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.001F)), PartPose.offsetAndRotation(7.5F, -5.0F, -2.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(53, 121).mirror().addBox(-2.5F, -7.0F, 0.0F, 5.0F, 7.0F, 0.0F, new CubeDeformation(0.001F)).mirror(false), PartPose.offsetAndRotation(-7.5F, -5.0F, -2.0F, -0.3927F, 0.0F, 0.0F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(97, 0).addBox(-5.0F, -2.0F, -5.0F, 10.0F, 24.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(12.0F, -11.0F, -10.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(97, 0).mirror().addBox(-5.0F, -2.0F, -5.0F, 10.0F, 24.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-12.0F, -11.0F, -10.0F));

        PartDefinition left_leg = body.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(138, 0).addBox(-4.0F, -0.5F, -2.5F, 8.0F, 17.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -5.5F, 18.5F));

        PartDefinition right_leg = body.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(138, 0).mirror().addBox(-4.0F, -0.5F, -2.5F, 8.0F, 17.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -5.5F, 18.5F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(55, 102).addBox(-3.0F, -3.5F, -1.0F, 6.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -19.5F, 22.0F));

        return LayerDefinition.create(meshdefinition, 256, 256);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.body.getAllParts().forEach(ModelPart::resetPose);

        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD + (45 * Mth.PI / 180) -1.6F * Mth.sin(attackTime * Mth.PI) - (Mth.cos(ageInTicks/12.0F))/18F;
        this.jaw.xRot = ((12.5f * Mth.PI / 180) - (Mth.sin(ageInTicks/12.0F))/18F);

        this.body.zRot = Mth.cos(limbSwing * 0.65F) * 0.05F * limbSwingAmount;

        this.right_leg.xRot = Mth.cos(limbSwing * 0.65F) * 1.0F * limbSwingAmount;
        this.left_leg.xRot = Mth.cos(limbSwing * 0.65F) * -1.0F * limbSwingAmount;

        this.right_arm.xRot = Mth.cos(limbSwing * 0.65F) * -1.0F * limbSwingAmount;
        this.left_arm.xRot = Mth.cos(limbSwing * 0.65F) * 1.0F * limbSwingAmount;

        AnimationUtils.bobModelPart(tail, ageInTicks, 0.2f);

        AnimationUtils.bobModelPart(left_thorns, ageInTicks, 0.2f);
        AnimationUtils.bobModelPart(right_thorns, ageInTicks, -0.2f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}
