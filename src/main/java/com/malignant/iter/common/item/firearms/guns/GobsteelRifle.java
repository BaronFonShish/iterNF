package com.malignant.iter.common.item.firearms.guns;

import com.malignant.iter.common.registry.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class GobsteelRifle extends AbstractGun{

    private static final float DAMAGE = 4;
    private static final float VELOCITY = 2;
    private static final float SPREAD = 2;
    private static final int MAG_SIZE = 1;
    private static final int RELOAD_TIME = 20;
    private static final float FIRE_RATE = 1f;

    public GobsteelRifle() {
        super(new Properties()
                        .durability(530)
                        .rarity(Rarity.COMMON)
                        .stacksTo(1),
                DAMAGE, VELOCITY, SPREAD, MAG_SIZE, RELOAD_TIME, FIRE_RATE);
    }

    @Override
    public void shootEffects(Level level, Player player, ItemStack gun, Item round){
        //recoil on fire anim
        forceAnimation(gun, 1, level.getGameTime(), 7);
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.RIFLE_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F);
    }


}
