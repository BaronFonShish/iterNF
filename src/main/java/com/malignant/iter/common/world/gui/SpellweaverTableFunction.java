package com.malignant.iter.common.world.gui;

import com.malignant.iter.common.item.magic.defaults.SpellItem;
import com.malignant.iter.common.registry.ModItems;
import com.malignant.iter.common.variables.IterPlayerDataUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class SpellweaverTableFunction {

    public static boolean getswitch(Player player) {
        return IterPlayerDataUtils.getSpellweaverSwitch(player);
    }

    public static void flipswitch(Player player) {
        boolean currentState = getswitch(player);
        IterPlayerDataUtils.setSpellweaverSwitch(player, !currentState);
        player.containerMenu.broadcastChanges();
    }

    public static ItemStack getItemFromSlot(Player player, int slot) {
        if (player.containerMenu == null) return ItemStack.EMPTY;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null) {
                return menuSlot.getItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static void RemoveItemFromSlot(Player player, int slot, int amount) {
        if (player.containerMenu == null) return;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null && menuSlot.hasItem()) {
                menuSlot.remove(amount);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    public static void SetItemToSlot(Player player, ItemStack item, int slot, int amount) {
        if (player.containerMenu == null) return;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null) {
                item.setCount(amount);
                menuSlot.set(item);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    public static int skimSlots(Player player, ItemStack itemStack) {
        int count = 0;
        for (int i = 1; i <= 6; i++) {
            ItemStack itemfromslot = getItemFromSlot(player, i);
            if (itemfromslot.getItem() == itemStack.getItem()) {
                count += itemfromslot.getCount();
            }
        }
        return count;
    }

    public static void RemoveXItem(Player player, ItemStack itemStack, int amount) {
        if (amount <= 0) return;
        for (int i = 1; i <= 6; i++) {
            ItemStack itemfromslot = getItemFromSlot(player, i);
            if (!itemfromslot.isEmpty() && itemfromslot.getItem() == itemStack.getItem()) {
                int toRemove = Math.min(amount, itemfromslot.getCount());
                RemoveItemFromSlot(player, i, toRemove);
                amount -= toRemove;
                if (amount <= 0) break;
            }
        }
        player.containerMenu.broadcastChanges();
    }

    public static Item getUpgradeCatalyst(int level) {
        return switch (level) {
            case 1 -> Items.COPPER_INGOT;
            case 2 -> Items.AMETHYST_SHARD;
            case 3 -> ModItems.ETHERDUST.get();
            case 4 -> Items.GOLD_INGOT;
            case 5 -> ModItems.NOSTELON.get();
            case 6 -> Items.DIAMOND;
            case 7 -> Items.ENDER_EYE;
            case 8 -> ModItems.MAGMANUM.get();
            case 9 -> Items.ECHO_SHARD;
            case 10 -> Items.NETHERITE_INGOT;
            case 11 -> Items.NETHER_STAR;
            default -> ModItems.ETHERDUST.get();
        };
    }

    public static void execute(Player player) {
        if (getswitch(player)) CopySpell(player);
        else UpgradeSpell(player);
    }

    public static void CopySpell(Player player) {
        ItemStack SpellToCopy = getItemFromSlot(player, 0);
        if (SpellToCopy.getItem() instanceof SpellItem spell) {
            int tier = spell.getTier();
            int gistCost = Math.max(1, tier * 3);
            int gistAtHand = skimSlots(player, ModItems.GIST.get().getDefaultInstance());
            int paperAtHand = skimSlots(player, Items.PAPER.getDefaultInstance());
            int inkAtHand = skimSlots(player, ModItems.INK_BOTTLE.get().getDefaultInstance());

            if (gistAtHand >= gistCost && paperAtHand > 0 && inkAtHand > 0 && getItemFromSlot(player, 7).isEmpty()) {
                RemoveXItem(player, ModItems.GIST.get().getDefaultInstance(), gistCost);
                RemoveXItem(player, ModItems.INK_BOTTLE.get().getDefaultInstance(), 1);
                RemoveXItem(player, Items.PAPER.getDefaultInstance(), 1);
                ItemStack copiedSpell = SpellToCopy.copy();
                spell.setQuality(copiedSpell, 0);
                SetItemToSlot(player, copiedSpell, 7, 1);
                player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    public static void UpgradeSpell(Player player) {
        ItemStack SpellToUpgrade = getItemFromSlot(player, 0);
        if (SpellToUpgrade.getItem() instanceof SpellItem spell) {
            int tier = spell.getTier();
            ItemStack SpellStack = getItemFromSlot(player, 0);
            int quality = spell.getQuality(SpellStack);
            int gistCost = Math.max(1, tier + quality * 2);
            int level = tier + quality;
            int getGist = skimSlots(player, ModItems.GIST.get().getDefaultInstance());
            Item itemNeeded = getUpgradeCatalyst(level);
            int getitemNeeded = skimSlots(player, itemNeeded.getDefaultInstance());

            if (quality < 10 && getGist >= gistCost && getitemNeeded >= 1) {
                RemoveXItem(player, ModItems.GIST.get().getDefaultInstance(), gistCost);
                RemoveXItem(player, itemNeeded.getDefaultInstance(), 1);
                ItemStack newSpell = SpellToUpgrade.copy();
                spell.setQuality(newSpell, quality + 1);
                SetItemToSlot(player, newSpell, 7, 1);
                RemoveItemFromSlot(player, 0, 1);
                player.playSound(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT);
                player.containerMenu.broadcastChanges();
            }
        }
    }
}