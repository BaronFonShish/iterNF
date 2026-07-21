package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.projectile.GoblinBullet;
import com.malignant.iter.common.entity.projectile.IronBullet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class GoblinBulletRenderer extends BulletRenderer<GoblinBullet> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/projectile/goblin_bullet.png");

    public GoblinBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(GoblinBullet entity) {
        return TEXTURE;
    }
}