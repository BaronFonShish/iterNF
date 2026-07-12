package com.malignant.iter.common.item;

import net.minecraft.world.item.Rarity;


public class NetheriteWarbowItem extends AbstractIterBow {

    public NetheriteWarbowItem() {
        super(new Properties()
                .rarity(Rarity.COMMON)
                .durability(1028)
                .fireResistant());
    }

    public boolean isFireResistant() {
        return true;
    }

    @Override
    public double flatDamageBonus() {
        return 0.5;
    }

    @Override
    public double powerMult() {
        return 1.075;
    }

    @Override
    public int getDrawDuration() {
        return 20;
    }

    @Override
    public float getVelocityMultiplier() {
        return 3.5f;
    }
}
