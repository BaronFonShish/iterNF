package com.malignant.iter.client.model;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.GoblinSharpshooterEntity;
import com.malignant.iter.common.registry.ModItems;
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
import net.minecraft.world.entity.HumanoidArm;

public class GoblinSharpshooterModel<T extends Entity> extends EntityModel<T> implements ArmedModel {

    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "goblin_sharpshooter"), "main");
    private final ModelPart body;
	private final ModelPart head;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;
	private final ModelPart rightArm;
	private final ModelPart leftArm;

	public GoblinSharpshooterModel(ModelPart root) {
		this.body = root.getChild("body");
		this.head = this.body.getChild("head");
		this.rightLeg = root.getChild("rightLeg");
		this.leftLeg = root.getChild("leftLeg");
		this.rightArm = root.getChild("rightArm");
		this.leftArm = root.getChild("leftArm");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 25).addBox(-2.5F, -6.0F, -1.5F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 40).addBox(-2.5F, -6.0F, -1.5F, 5.0F, 6.0F, 3.0F, new CubeDeformation(0.3F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 13).addBox(-3.5F, -6.0F, -2.25F, 7.0F, 6.0F, 5.0F, new CubeDeformation(0.0F))
		.texOffs(15, 34).addBox(0.5F, -4.0F, -5.25F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(37, 11).addBox(-4.5F, -1.0F, -2.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-3.5F, -7.0F, -2.25F, 7.0F, 7.0F, 5.0F, new CubeDeformation(0.3F))
		.texOffs(26, 25).addBox(3.5F, -4.0F, -0.25F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F))
		.texOffs(26, 28).addBox(-6.5F, -4.0F, -0.25F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -0.25F));

		PartDefinition rightLeg = partdefinition.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(25, 18).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.5F, 20.0F, 0.0F));

		PartDefinition leftLeg = partdefinition.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(25, 18).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 20.0F, 0.0F));

		PartDefinition rightArm = partdefinition.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(25, 9).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(33, 12).mirror().addBox(-2.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.28F)).mirror(false), PartPose.offset(-2.5F, 15.5F, 0.0F));

		PartDefinition leftArm = partdefinition.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(25, 9).addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(33, 20).mirror().addBox(0.0F, -1.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.28F)).mirror(false), PartPose.offset(2.5F, 15.5F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw / (180F / (float) Math.PI);
        this.head.xRot = headPitch / (180F / (float) Math.PI);
        this.rightLeg.xRot = Mth.cos(limbSwing * 0.65F) * 1.0F * limbSwingAmount;
        this.rightArm.xRot = 0;
        this.leftArm.xRot = 0;
        this.rightArm.yRot = 0;
        this.leftArm.yRot = 0;
        if (entity instanceof GoblinSharpshooterEntity goblin){
            if (goblin.getMainHandItem().is(ModItems.GOBSTEEL_RIFLE.get())){
                if (goblin.getMainArm() == HumanoidArm.RIGHT){
                    this.rightArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
                    this.rightArm.yRot = this.head.yRot;
                } else{
                    this.leftArm.xRot = -((float) Math.PI / 2F) + this.head.xRot;
                    this.leftArm.yRot = this.head.yRot;
                }
            } else
            {
                this.rightArm.xRot = Mth.cos(limbSwing * 0.65F + (float) Math.PI) * limbSwingAmount;
                this.leftArm.xRot = Mth.cos(limbSwing * 0.65F) * limbSwingAmount;
            }
        }

        this.leftLeg.xRot = Mth.cos(limbSwing * 0.65F) * -1.0F * limbSwingAmount;

        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;

        AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
	}

    protected ModelPart getArm(HumanoidArm pSide){
        return pSide == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    public void translateToHand(HumanoidArm Side, PoseStack PoseStack){
        ModelPart arm = this.getArm(Side);
        arm.translateAndRotate(PoseStack);
        PoseStack.scale(0.95f, 0.95f,0.95f);

        float handOffset = Side == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        PoseStack.translate(handOffset * 0.05F, -0.225F, 0.0F);
    }

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		rightLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		leftLeg.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		rightArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
		leftArm.render(poseStack, vertexConsumer, packedLight, packedOverlay);
	}
}