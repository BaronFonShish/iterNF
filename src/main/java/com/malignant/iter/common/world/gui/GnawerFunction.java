package com.malignant.iter.common.world.gui;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;

import java.util.function.Consumer;

public class GnawerFunction {

    public static ItemStack getItemFromSlot(Player player, int slot){
        if (player.containerMenu == null) return ItemStack.EMPTY;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null) {
                return menuSlot.getItem();
            }
        }
        return ItemStack.EMPTY;
    }

    public static void RemoveItemFromSlot(Player player, int slot, int amount){
        if (player.containerMenu == null) return;
        if (slot >= 0 && slot < player.containerMenu.slots.size()) {
            Slot menuSlot = player.containerMenu.getSlot(slot);
            if (menuSlot != null && menuSlot.hasItem()) {
                menuSlot.remove(amount);
                player.containerMenu.broadcastChanges();
            }
        }
    }

    public static void SetItemToSlot(Player player, ItemStack item, int slot, int amount){
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

    public static void function(Player player){
        if (player.level().isClientSide) return;

        ItemStack book = getItemFromSlot(player, 1);
        ItemStack donor = getItemFromSlot(player, 0);
        ItemStack output = getItemFromSlot(player, 2);

        if (!output.isEmpty()) return;
        if (book.isEmpty() || donor.isEmpty()) return;
        if (!donor.isEnchanted()) return;
        if (player.experienceLevel < 5) return;
        if (book.getItem() != Items.BOOK) return;

        ItemEnchantments donorEnchantments = donor.getEnchantments();
        if (donorEnchantments.isEmpty()) return;

        Holder<Enchantment> firstEnchantment = donorEnchantments.keySet().iterator().next();
        int enchantLevel = donorEnchantments.getLevel(firstEnchantment);

        ItemStack enchantbook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(firstEnchantment, enchantLevel));

        ItemEnchantments.Mutable remainingEnchantments = new ItemEnchantments.Mutable(donorEnchantments);
        remainingEnchantments.removeIf(holder -> holder.equals(firstEnchantment));
        donor.set(DataComponents.ENCHANTMENTS, remainingEnchantments.toImmutable());

        if (donor.isDamageableItem()) {
            int damageAmount = Math.max(1, donor.getMaxDamage() / 4);
            int currentDamage = donor.getDamageValue();
            int newDamage = currentDamage + damageAmount;

            if (newDamage >= donor.getMaxDamage()) {
                RemoveItemFromSlot(player, 0, 1);
            } else {
                donor.setDamageValue(newDamage);
                if (player.containerMenu != null && player.containerMenu.slots.size() > 0) {
                    Slot donorSlot = player.containerMenu.getSlot(0);
                    if (donorSlot != null) {
                        donorSlot.setChanged();
                    }
                }
            }
        }

        RemoveItemFromSlot(player, 1, 1);
        player.giveExperienceLevels(-5);

        SetItemToSlot(player, enchantbook, 2, 1);

        player.containerMenu.broadcastChanges();
    }
}