package com.malignant.iter.common.world.gui;

import com.malignant.iter.common.event.SpellBookUtils;
import com.malignant.iter.common.item.magic.defaults.SpellBook;
import com.malignant.iter.common.registry.ModMenus;
import com.malignant.iter.common.variables.SpellbookCapability;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SpellBookGuiMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {

    public final static HashMap<String, Object> guistate = new HashMap<>();
    public final Level world;
    public final Player entity;
    private IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private boolean bound = false;
    private SpellbookCapability capabilityRef;
    private Supplier<Boolean> boundItemMatcher = null;
    private final int handId;
    private final ItemStack heldStack;

    public SpellBookGuiMenu(int id, Inventory inv, int handId, ItemStack heldStack) {
        super(ModMenus.SPELLBOOK_GUI.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();
        this.handId = handId;
        this.heldStack = heldStack;

        SpellbookCapability capability = null;
        if (heldStack.getItem() instanceof SpellBook spellBook) {
            capability = spellBook.getSpellbookCapability(heldStack, entity.registryAccess());
        }
        if (capability == null) {
            capability = new SpellbookCapability();  // fresh empty inventory
        }
        this.capabilityRef = capability;
        this.internal = capability.getCapability(null);   // IItemHandler for slots
        this.bound = true;
        this.boundItemMatcher = () -> {
            ItemStack currentStack = handId == 0 ? this.entity.getMainHandItem() : this.entity.getOffhandItem();
            return ItemStack.isSameItemSameComponents(heldStack, currentStack);
        };

        int[] bottomX = {38, 66, 94, 122, 169, 197, 225, 253};
        int[] bottomY = {131, 114, 127, 111, 111, 127, 114, 131};

        for (int i = 0; i < 8; i++) {
            this.customSlots.put(i + 1, createSlot(internal, i + 1, bottomX[i], bottomY[i]));
        }

        int xStart = 34;
        int yStart = 30;
        int[] yPattern = {0, 0, -1, -2, -3, -4, -4, -3, -2, -1, 0, 0};

        int slotid = 9;
        for (int i = 0; i < 4; i++) {
            int currentXStart = xStart;
            for (int j = 0; j < 12; j++) {
                this.customSlots.put(slotid, createSlot(internal, slotid, currentXStart, (yStart + yPattern[j])));
                slotid++;
                if (j == 5) {
                    currentXStart += 44;
                } else {
                    currentXStart += 18;
                }
            }
            yStart += 18;
        }

        for (int si = 0; si < 3; ++si) {
            for (int sj = 0; sj < 9; ++sj) {
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 67 + 8 + sj * 18, 85 + 84 + si * 18));
            }
        }

        for (int si = 0; si < 9; ++si) {
            this.addSlot(new Slot(inv, si, 67 + 8 + si * 18, 85 + 142));
        }
    }

    private Slot createSlot(IItemHandler internal, int id, int x, int y) {
        Slot slot = new SlotItemHandler(internal, id, x, y) {
            @Override
            public boolean mayPlace(ItemStack itemstack) {
                return SpellBookUtils.isSpellItem(itemstack);
            }
        };
        this.addSlot(slot);
        return slot;
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.bound && this.boundItemMatcher != null) {
            return this.boundItemMatcher.get();
        }
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            int customSlotCount = this.customSlots.size();

            if (index < customSlotCount) {
                if (!this.moveItemStackTo(itemstack1, customSlotCount, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            }
            else if (SpellBookUtils.isSpellItem(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, customSlotCount, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, itemstack1);
        }
        return itemstack;
    }

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (heldStack.getItem() instanceof SpellBook spellBook && capabilityRef != null) {
            spellBook.saveSpellbookInventory(heldStack, capabilityRef, playerIn.registryAccess());
        }
    }

    public Map<Integer, Slot> get() {
        return customSlots;
    }
}