package com.malignant.iter.client.model;
import com.malignant.iter.IterMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class InsatiableModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "insatiable"), "main");
    private final ModelPart model;
    private final ModelPart Body;
    private final ModelPart Tie;
    private final ModelPart LeftArm;
    private final ModelPart RightArm;
    private final ModelPart Head;
    private final ModelPart RightLeg;
    private final ModelPart LeftLeg;

    public InsatiableModel(ModelPart root) {
        this.model = root.getChild("model");
        this.Body = model.getChild("Body");
        this.Tie = Body.getChild("Tie");
        this.LeftArm = Body.getChild("LeftArm");
        this.RightArm = Body.getChild("RightArm");
        this.Head = Body.getChild("Head");
        this.RightLeg = model.getChild("RightLeg");
        this.LeftLeg = model.getChild("LeftLeg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition model = partdefinition.addOrReplaceChild("model", CubeListBuilder.create(), PartPose.offset(0.0F, 21.0F, 1.0F));

        PartDefinition Body = model.addOrReplaceChild("Body", CubeListBuilder.create().texOffs(0, 98).addBox(-8.0F, -2.8333F, -7.1F, 16.0F, 5.0F, 13.0F, new CubeDeformation(0.0F))
                .texOffs(40, 0).addBox(-11.0F, -26.8333F, -8.1F, 22.0F, 24.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -10.1667F, 0.1F));

        PartDefinition Tie = Body.addOrReplaceChild("Tie", CubeListBuilder.create(), PartPose.offset(0.0F, 0.1667F, -7.3F));

        PartDefinition tie_r1 = Tie.addOrReplaceChild("tie_r1", CubeListBuilder.create().texOffs(16, 116).addBox(-5.5F, -1.0F, 0.0F, 11.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        PartDefinition LeftArm = Body.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offset(12.0F, -21.8333F, -1.1F));

        PartDefinition arm_r1 = LeftArm.addOrReplaceChild("arm_r1", CubeListBuilder.create().texOffs(94, 38).addBox(-4.5F, -18.0F, -4.0F, 9.0F, 36.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 15.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition RightArm = Body.addOrReplaceChild("RightArm", CubeListBuilder.create().texOffs(84, 109).mirror().addBox(-7.92F, 22.0F, -4.98F, 10.0F, 9.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(86, 103).mirror().addBox(-6.92F, 31.0F, 0.02F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-12.08F, -21.8333F, -1.12F, -0.3927F, 0.0F, 0.0F));

        PartDefinition harpoon_r1 = RightArm.addOrReplaceChild("harpoon_r1", CubeListBuilder.create().texOffs(86, 103).mirror().addBox(-4.0F, -3.0F, 0.0F, 8.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(60, 38).mirror().addBox(-4.5F, -33.0F, -4.0F, 9.0F, 21.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.92F, 34.0F, 0.02F, 0.0F, 1.5708F, 0.0F));

        PartDefinition strap_r1 = RightArm.addOrReplaceChild("strap_r1", CubeListBuilder.create().texOffs(0, 85).mirror().addBox(-4.5F, -1.0F, -4.9F, 10.0F, 4.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-2.92F, -2.0F, 0.42F, 0.0F, 1.5708F, 0.0F));

        PartDefinition Head = Body.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(14, 22).addBox(-1.0F, 3.85F, -8.6F, 2.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(1, 24).addBox(-5.0F, -8.25F, -8.3F, 10.0F, 12.0F, 9.0F, new CubeDeformation(0.0F))
                .texOffs(0, 0).addBox(-5.0F, -8.25F, -8.5F, 10.0F, 12.0F, 10.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, -21.5833F, -6.6F));

        PartDefinition RightLeg = model.addOrReplaceChild("RightLeg", CubeListBuilder.create().texOffs(58, 109).mirror().addBox(-3.0F, -2.5F, -3.5F, 6.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-5.0F, -6.5F, -0.5F));

        PartDefinition LeftLeg = model.addOrReplaceChild("LeftLeg", CubeListBuilder.create().texOffs(58, 109).addBox(-3.0F, -2.5F, -3.5F, 6.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, -6.5F, -0.5F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.Head.xRot = headPitch * Mth.DEG_TO_RAD + Mth.cos(limbSwing * 1.3F) * -0.2F * limbSwingAmount;

        this.RightLeg.xRot = Mth.cos(limbSwing * 0.45F) * 1.3F * limbSwingAmount;
        this.LeftLeg.xRot = Mth.cos(limbSwing * 0.45F) * -1.3F * limbSwingAmount;

        this.RightArm.zRot = 0.0F;
        this.LeftArm.zRot = 0.0F;
        this.RightArm.xRot = Mth.cos(limbSwing * 0.45F + (float) Math.PI) * limbSwingAmount * 0.7F -0.5f;
        this.LeftArm.xRot = Mth.cos(limbSwing * 0.45F) * limbSwingAmount * 1.2F;

        this.model.zRot = Mth.cos(limbSwing * 0.28F) * 0.2F * limbSwingAmount;
        this.model.xRot = Mth.sin(limbSwingAmount) * 0.3f;

        this.Body.xRot = Mth.cos(limbSwing * 1.3F) * 0.2F * limbSwingAmount;

        this.Body.yRot = Mth.cos(limbSwing * 0.45F) * -0.2F * limbSwingAmount;

        this.Tie.xRot = Mth.sin(0.05F * ageInTicks) * 0.1f;
        AnimationUtils.bobArms(this.RightArm, this.LeftArm, ageInTicks);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        model.render(poseStack, vertexConsumer, packedLight, packedOverlay);
    }
}