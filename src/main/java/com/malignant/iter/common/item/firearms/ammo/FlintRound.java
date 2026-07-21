package com.malignant.iter.common.item.firearms.ammo;

import com.malignant.iter.common.registry.ModEntities;

public class FlintRound extends AbstractAmmo {

    private static final float DAMAGE = 4;
    private static final float VELOCITY = 0.95f;
    private static final float SPREAD = 0.075f;
    private static final int PROJECTILES = 1;

    public FlintRound(Properties properties) {
        super(properties, DAMAGE, VELOCITY, SPREAD, PROJECTILES, ModEntities.FLINT_BULLET.get());
    }
}