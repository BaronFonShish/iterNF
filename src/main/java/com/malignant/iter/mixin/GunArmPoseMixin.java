package com.malignant.iter.mixin;

import com.malignant.iter.common.item.firearms.guns.AbstractGun;
import com.malignant.iter.common.item.magic.defaults.SpellFocus;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class GunArmPoseMixin<T extends LivingEntity> extends EntityModel<T> {

    @Shadow
    @Final
    public ModelPart leftArm;

    @Shadow
    @Final
    public ModelPart rightArm;

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void applyGunPose(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                                  float netHeadYaw, float headPitch, CallbackInfo ci) {

        ItemStack stack = entity.getMainHandItem();
        ItemStack offstack = entity.getOffhandItem();
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;

        if (stack.getItem() instanceof AbstractGun) {
            HumanoidArm gunArm = entity.getMainArm();

            if (gunArm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;
                model.rightArm.yRot = model.head.yRot;
                model.rightArm.zRot = 0.0F;
            } else {
                model.leftArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;
                model.leftArm.yRot = model.head.yRot;
                model.leftArm.zRot = 0.0F;
            }
        }

        if (offstack.getItem() instanceof AbstractGun) {
            HumanoidArm gunArm = entity.getMainArm().getOpposite();

            if (gunArm == HumanoidArm.RIGHT) {
                model.rightArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;
                model.rightArm.yRot = model.head.yRot;
                model.rightArm.zRot = 0.0F;
            } else {
                model.leftArm.xRot = (-(float) Math.PI / 2F) + model.head.xRot;
                model.leftArm.yRot = model.head.yRot;
                model.leftArm.zRot = 0.0F;
            }
        }
    }
}
