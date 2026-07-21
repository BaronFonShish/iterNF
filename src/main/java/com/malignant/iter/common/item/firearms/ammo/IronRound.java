package com.malignant.iter.common.item.firearms.ammo;

import com.malignant.iter.common.registry.ModEntities;

public class IronRound extends AbstractAmmo {

    private static final float DAMAGE = 6;
    private static final float VELOCITY = 1f;
    private static final float SPREAD = 0.05f;
    private static final int PROJECTILES = 1;

    public IronRound(Properties properties) {
        super(properties, DAMAGE, VELOCITY, SPREAD, PROJECTILES, ModEntities.IRON_BULLET.get());
    }
}