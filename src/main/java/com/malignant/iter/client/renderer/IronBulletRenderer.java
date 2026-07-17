package com.malignant.iter.client.renderer;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.projectile.IronBullet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class IronBulletRenderer extends BulletRenderer<IronBullet> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "textures/entity/projectile/iron_bullet.png");

    public IronBulletRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 0.25f;
    }

    @Override
    public ResourceLocation getTextureLocation(IronBullet entity) {
        return TEXTURE;
    }
}