package com.malignant.iter.common.item.firearms.ammo;

import com.malignant.iter.common.registry.ModEntities;

public class GoblinRound extends AbstractAmmo {

    private static final float DAMAGE = 5;
    private static final float VELOCITY = 1f;
    private static final float SPREAD = 2f;
    private static final int PROJECTILES = 2;

    public GoblinRound(Properties properties) {
        super(properties, DAMAGE, VELOCITY, SPREAD, PROJECTILES, ModEntities.GOBLIN_BULLET.get());
    }
}