package com.malignant.iter.common.item.firearms.guns;

import net.minecraft.world.item.Rarity;

public class GobsteelRifle extends AbstractGun{

    private static final float DAMAGE = 4;
    private static final float VELOCITY = 2;
    private static final float SPREAD = 5;
    private static final int MAG_SIZE = 1;
    private static final int RELOAD_TIME = 30;
    private static final int FIRE_RATE = 1;

    public GobsteelRifle() {
        super(new Properties()
                        .durability(530)
                        .rarity(Rarity.COMMON)
                        .stacksTo(1),
                DAMAGE, VELOCITY, SPREAD, MAG_SIZE, RELOAD_TIME, FIRE_RATE);
    }
}
