package com.malignant.iter.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class HobGoblinModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("itermod", "hobgoblin"), "main");
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart leftArm;
    private final ModelPart rightArm;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;

    public HobGoblinModel(ModelPart root) {
        this.head = root.getChild("head");
        this.body = root.getChild("body");
        this.leftArm = root.getChild("leftArm");
        this.rightArm = root.getChild("rightArm");
        this.leftLeg = root.getChild("leftLeg");
        this.rightLeg = root.getChild("rightLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(81, 18).addBox(-5.0F, -2.0F, -8.0F, 10.0F, 3.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 23).addBox(-5.0F, -3.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 20).addBox(3.0F, -3.0F, -8.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(83, 1).addBox(-5.0F, -9.0F, -7.0F, 10.0F, 7.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(42, 8).addBox(-5.0F, -11.0F, -7.0F, 5.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 4).addBox(-9.0F, -6.0F, -2.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(5.0F, -6.0F, -2.0F, 4.0F, 3.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(1, 45).addBox(-7.0F, 3.0F, -4.0F, 14.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(1, 27).addBox(-7.0F, 3.0F, -4.0F, 14.0F, 9.0F, 8.0F, new CubeDeformation(0.25F))
                .texOffs(0, 0).addBox(-8.0F, -6.0F, -5.0F, 16.0F, 10.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(99, 44).addBox(-1.0F, 3.0F, -3.4F, 7.0F, 19.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(73, 70).addBox(-1.0F, -5.0F, -4.5F, 10.0F, 8.0F, 9.0F, new CubeDeformation(0.25F))
                .texOffs(41, 91).addBox(-1.0F, 13.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.25F)), PartPose.offset(9.0F, -2.0F, 1.0F));

        PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(69, 44).addBox(-6.0F, 4.0F, -3.5F, 7.0F, 19.0F, 7.0F, new CubeDeformation(0.0F))
                .texOffs(73, 70).mirror().addBox(-9.0F, -4.0F, -4.5F, 10.0F, 8.0F, 9.0F, new CubeDeformation(0.25F)).mirror(false)
                .texOffs(70, 34).addBox(-12.0F, -9.0F, 0.0F, 7.0F, 8.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(8, 86).addBox(-7.0F, 10.0F, -4.0F, 8.0F, 13.0F, 8.0F, new CubeDeformation(0.25F)), PartPose.offset(-9.0F, -3.0F, 1.0F));

        PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(1, 65).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(26, 65).addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.25F)), PartPose.offset(4.0F, 12.0F, 1.0F));

        PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(1, 65).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(26, 65).mirror().addBox(-3.0F, 0.0F, -3.0F, 6.0F, 12.0F, 6.0F, new CubeDeformation(0.25F)).mirror(false), PartPose.offset(-4.0F, 12.0F, 1.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
                          float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.6F) * 1.0F * limbSwingAmount;
        this.rightArm.xRot = Mth.cos(limbSwing * 0.6F + (float) Math.PI) * limbSwingAmount -2.2F * Mth.sin(attackTime * Mth.PI);
        this.rightArm.yRot = -0.2F * Mth.sin(attackTime * Mth.PI);
        this.leftArm.xRot = Mth.cos(limbSwing * 0.6F) * limbSwingAmount -2.2F * Mth.sin(attackTime * Mth.PI);
        this.leftArm.yRot = 0.2F * Mth.sin(attackTime * Mth.PI);;
        this.leftLeg.xRot = Mth.cos(limbSwing * 0.6F) * -1.0F * limbSwingAmount;
        this.leftArm.zRot = (Mth.sin(ageInTicks/32)/-64) - 0.03F;
        this.rightArm.zRot = (Mth.sin(ageInTicks/32)/64) + 0.03F;

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        head.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}