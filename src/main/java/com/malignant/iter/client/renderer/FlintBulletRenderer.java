package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.projectile.FlintBullet;
import com.malignant.iter.common.entity.projectile.GoblinBullet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FlintBulletRenderer extends BulletRenderer<FlintBullet> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/projectile/flint_bullet.png");

    public FlintBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(FlintBullet entity) {
        return TEXTURE;
    }
}