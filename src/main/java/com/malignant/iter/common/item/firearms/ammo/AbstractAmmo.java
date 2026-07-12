package com.malignant.iter.common.item.firearms.ammo;

import net.minecraft.world.item.Item;

public class AbstractAmmo extends Item {
    private final float basedamage;
    private final float velocity;
    private final float spread;

    public AbstractAmmo(Properties properties, float basedamage, float velocity, float spread) {
        super(properties);
        this.basedamage = basedamage;
        this.velocity = velocity;
        this.spread = spread;
    }

    public float getBasedamage() { return basedamage; }
    public float getVelocity() { return velocity; }
    public float getSpread() { return spread; }
}