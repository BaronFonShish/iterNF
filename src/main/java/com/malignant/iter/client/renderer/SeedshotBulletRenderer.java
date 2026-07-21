package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.projectile.GoblinBullet;
import com.malignant.iter.common.entity.projectile.SeedshotBullet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class SeedshotBulletRenderer extends BulletRenderer<SeedshotBullet> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/projectile/seedshot_bullet.png");

    public SeedshotBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(SeedshotBullet entity) {
        return TEXTURE;
    }
}