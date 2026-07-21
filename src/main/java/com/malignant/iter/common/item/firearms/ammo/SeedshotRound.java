package com.malignant.iter.common.item.firearms.ammo;

import com.malignant.iter.common.registry.ModEntities;

public class SeedshotRound extends AbstractAmmo {

    private static final float DAMAGE = 4;
    private static final float VELOCITY = 1f;
    private static final float SPREAD = 10f;
    private static final int PROJECTILES = 6;

    public SeedshotRound(Properties properties) {
        super(properties, DAMAGE, VELOCITY, SPREAD, PROJECTILES, ModEntities.SEEDSHOT_BULLET.get());
    }
}