package com.malignant.iter.common.item.firearms.ammo;

import com.malignant.iter.common.entity.projectile.AbstractBullet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;

public class AbstractAmmo extends Item {
    private final float basedamage;
    private final float velocity;
    private final float spread;
    private final int projectiles;
    private final EntityType<? extends Projectile> bulletType;

    public AbstractAmmo(Properties properties, float basedamage, float velocity, float spread, int projectiles,
                        EntityType<? extends AbstractBullet> bulletType) {
        super(properties);
        this.basedamage = basedamage;
        this.velocity = velocity;
        this.spread = spread;
        this.projectiles = projectiles;
        this.bulletType = bulletType;
    }

    public float getBasedamage() { return basedamage; }
    public float getVelocity() { return velocity; }
    public float getSpread() { return spread; }
    public int getProjectiles() { return projectiles; }

    public EntityType<? extends Projectile> getBulletType() {
        return bulletType;
    }
}