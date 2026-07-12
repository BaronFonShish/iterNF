package com.malignant.iter.common.world.gui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class VoidMawFunction {

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

    public static void setItemToSlot(Player player, ItemStack item, int slot) {
        if (player.containerMenu == null) return;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null) {
                menuSlot.set(item);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    public static void function(Player player) {
        if (player.level().isClientSide) return;

        ItemStack item = getItemFromSlot(player, 0);
        if (item.isEmpty()) return;

        // Check condition: player has ≥20 XP, item has a repair cost > 0
        if (player.experienceLevel < 20) return;
        Integer currentCost = item.get(DataComponents.REPAIR_COST);
        if (currentCost == null || currentCost <= 0) return;

        // Calculate new repair cost: ((oldCost+1)/2)+1, then max 0
        int newCost = Math.max(0, ((currentCost + 1) / 2) + 1);
        item.set(DataComponents.REPAIR_COST, newCost);

        // Update the slot and consume XP
        setItemToSlot(player, item, 0);
        player.giveExperienceLevels(-20);

        player.containerMenu.broadcastChanges();
    }
}