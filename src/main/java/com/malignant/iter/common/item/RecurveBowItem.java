package com.malignant.iter.common.item;

import net.minecraft.world.item.Rarity;

public class RecurveBowItem extends AbstractIterBow {

    public RecurveBowItem() {
        super(new Properties()
                .rarity(Rarity.COMMON)
                .durability(514));
    }

    @Override
    public double flatDamageBonus() {
        return 0.25;
    }

    @Override
    public double powerMult() {
        return 1.05;
    }

    @Override
    public int getDrawDuration() {
        return 20;
    }

    @Override
    public float getVelocityMultiplier() {
        return 3.25f;
    }
}