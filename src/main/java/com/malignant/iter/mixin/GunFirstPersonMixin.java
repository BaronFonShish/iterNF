package com.malignant.iter.mixin;

import com.malignant.iter.common.item.firearms.guns.AbstractGun;
import com.malignant.iter.common.item.magic.defaults.SpellFocus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class GunFirstPersonMixin {

    @Inject(
            method = "renderArmWithItem",
            at = @At("HEAD")
    )
    private void GunAnim(AbstractClientPlayer player,
                                   float partialTicks,
                                   float pitch,
                                   InteractionHand hand,
                                   float swingProgress,
                                   ItemStack stack,
                                   float equipProgress,
                                   PoseStack poseStack,
                                   MultiBufferSource bufferSource,
                                   int packedLight,
                                   CallbackInfo ci) {

        if (stack.getItem() instanceof AbstractGun gun) {
            int state = gun.getAnimationState(stack);
            long start = gun.getAnimationStart(stack).longValue();
            int duration = gun.getAnimationDuration(stack);

            long elapsed = player.level().getGameTime() - start;
            float elapsedSmooth = elapsed + partialTicks;
            double progress = Math.min(1.0f, (double) elapsedSmooth/duration);

            if (progress >= 1.0f) {
                gun.forceAnimation(stack, 0, player.level().getGameTime(), 0);
            }

            float easeIn = 1.0f - (float) Math.pow(1.0f - progress, 3.0f);

            if (state == 1){
                poseStack.translate
                        (0f,
                        0f,
                                Math.sin(easeIn*Math.PI)*0.15f);
                poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.sin(easeIn*Math.PI) * 5f));
            }

            if (state == 2 && player.isUsingItem()){
                poseStack.translate(0f, (float) Math.sin(easeIn*Math.PI) * 0.15f, Math.sin(easeIn*Math.PI) * 0.25f);
                poseStack.mulPose(Axis.XP.rotationDegrees((float) Math.sin(progress*Math.PI) * -45f));
            }
        }
    }
}
