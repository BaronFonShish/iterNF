package com.malignant.iter.common.world.gui;

import com.malignant.iter.common.item.magic.defaults.SpellItem;
import com.malignant.iter.common.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class GuiTooltips {

    public static List<Component> SpellweaverFinalTooltip(Player player) {
        List<Component> list = new ArrayList<>();
        if (SpellweaverTableFunction.getItemFromSlot(player, 0).isEmpty()) {
            list.addAll(SpellweaverIntroduction(player));
            return list;
        }
        if (SpellweaverTableFunction.getswitch(player)) {
            list.addAll(SpellweaverCopy(player));
        } else {
            list.addAll(SpellweaverUpgrade(player));
        }
        return list;
    }

    public static List<Component> SpellweaverIntroduction(Player player) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.intro_1"));
        list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.intro_2"));
        return list;
    }

    public static List<Component> SpellweaverCopy(Player player) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.copy"));
        list.add(Component.literal(""));
        ItemStack spellStack = SpellweaverTableFunction.getItemFromSlot(player, 0);
        if (spellStack.getItem() instanceof SpellItem spell) {
            int gistNeeded = Math.max(1, spell.getTier() * 3);
            int getPaper = SpellweaverTableFunction.skimSlots(player, Items.PAPER.getDefaultInstance());
            int getGist = SpellweaverTableFunction.skimSlots(player, ModItems.GIST.get().getDefaultInstance());
            int getInk = SpellweaverTableFunction.skimSlots(player, ModItems.INK_BOTTLE.get().getDefaultInstance());
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    Items.PAPER.getDefaultInstance().getDisplayName(), getPaper, 1));
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    ModItems.INK_BOTTLE.get().getDefaultInstance().getDisplayName(), getInk, 1));
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    ModItems.GIST.get().getDefaultInstance().getDisplayName(), getGist, gistNeeded));
        }
        return list;
    }

    public static List<Component> SpellweaverUpgrade(Player player) {
        List<Component> list = new ArrayList<>();
        list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.upgrade"));
        list.add(Component.literal(""));
        ItemStack spellStack = SpellweaverTableFunction.getItemFromSlot(player, 0);
        if (spellStack.getItem() instanceof SpellItem spell && spell.getQuality(spellStack) < 10) {
            int level = spell.getTier() + spell.getQuality(spellStack);
            int gistNeeded = Math.max(1, spell.getTier() + spell.getQuality(spellStack) * 2);
            int getGist = SpellweaverTableFunction.skimSlots(player, ModItems.GIST.get().getDefaultInstance());
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    ModItems.GIST.get().getDefaultInstance().getDisplayName(), getGist, gistNeeded));
            Item itemNeeded = SpellweaverTableFunction.getUpgradeCatalyst(level);
            int getItemNeeded = SpellweaverTableFunction.skimSlots(player, itemNeeded.getDefaultInstance());
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    itemNeeded.getDefaultInstance().getDisplayName(), getItemNeeded, 1));
        }
        return list;
    }

    public static List<Component> VoidMaw(Player player) {
        List<Component> list = new ArrayList<>();
        ItemStack item = SpellweaverTableFunction.getItemFromSlot(player, 0);
        if (item.isEmpty()) {
            list.add(Component.translatable("gui.iter.void_maw.tooltip.intro"));
        } else {
            Integer repairCost = item.get(DataComponents.REPAIR_COST);
            int currentCost = repairCost != null ? repairCost : 0;
            int newCost = Math.max(0, ((currentCost + 1) / 2) + 1);
            list.add(Component.translatable("gui.iter.spellweaver_table.tooltip.itemXofX",
                    Component.translatable("gui.iter.void_maw.tooltip.explevel"), "20", player.experienceLevel));
            list.add(Component.translatable("gui.iter.void_maw.tooltip.repaircost",
                    Component.translatable("gui.iter.void_maw.tooltip.explevel"), currentCost, newCost));
        }
        return list;
    }

    public static boolean VoidMawCondition(Player player) {
        ItemStack item = SpellweaverTableFunction.getItemFromSlot(player, 0);
        if (item.isEmpty()) return false;
        if (player.experienceLevel < 20) return false;
        Integer repairCost = item.get(DataComponents.REPAIR_COST);
        if (repairCost == null || repairCost < 7) return false;
        return true;
    }
}