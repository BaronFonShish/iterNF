package com.malignant.iter.common.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class StingerItem extends AbstractIterBow {


    public StingerItem() {
        super(new Properties()
                .rarity(Rarity.COMMON)
                .durability(378));
    }

    @Override
    public double flatDamageBonus() {
        return 0;
    }

    @Override
    public double powerMult() {
        return 0.75;
    }

    @Override
    public int getDrawDuration() {
        return 10;
    }

    @Override
    public float getVelocityMultiplier() {
        return 2f;
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {
        if (entity instanceof Player player) {
            int useTime = player.getTicksUsingItem();
            if (useTime >= 12) {
                entity.releaseUsingItem();
            }
        }
    }
}
