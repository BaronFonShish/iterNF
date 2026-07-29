package com.malignant.iter.common.item.magic.spells;

import com.malignant.iter.common.event.GoblinPatrolEvent;
import com.malignant.iter.common.item.magic.defaults.SpellItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SpellTestGoblinPatrol extends SpellItem {

    public SpellTestGoblinPatrol() {super(new Properties(), SpellDomain.ARCANE, SpellMethod.FORM, SpellAspect.ETHER, 1, 20, 0, 10);}

    @Override
    public void castSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower) {
        GoblinPatrolEvent.forcePatrol((ServerLevel) level, new BlockPos((int) player.getX(), (int) player.getY(), (int) player.getZ()), player);
    }

    @Override
    public void castContinousSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {

    }
}
