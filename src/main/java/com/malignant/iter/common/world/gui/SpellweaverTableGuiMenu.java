package com.malignant.iter.common.world.gui;

import com.malignant.iter.common.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class SpellweaverTableGuiMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public static final HashMap<String, Object> guistate = new HashMap<>();
    public final Level world;
    public final Player entity;
    public int x, y, z;
    private final IItemHandler internal;
    private final Map<Integer, Slot> customSlots = new HashMap<>();

    public SpellweaverTableGuiMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ModMenus.SPELLWEAVER_TABLE_GUI.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();
        this.internal = new ItemStackHandler(8);

        BlockPos pos = null;
        if (extraData != null) {
            pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
        }

        // Slot 0: spell to copy/upgrade
        this.customSlots.put(0, this.addSlot(new SlotItemHandler(internal, 0, 25, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return true;
            }
        }));

        // Slots 1-6: reagent slots (paper, ink, gist, catalysts)
        int[][] positions = {
                {61, 26}, {79, 26}, {97, 26},
                {97, 44}, {61, 44}, {79, 44}
        };
        for (int i = 0; i < 6; i++) {
            final int slotIdx = i + 1;
            this.customSlots.put(slotIdx, this.addSlot(new SlotItemHandler(internal, slotIdx, positions[i][0], positions[i][1])));
        }

        // Slot 7: output slot (read-only)
        this.customSlots.put(7, this.addSlot(new SlotItemHandler(internal, 7, 133, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        }));

        // Player inventory (slots 8-43)
        for (int si = 0; si < 3; ++si) {
            for (int sj = 0; sj < 9; ++sj) {
                this.addSlot(new Slot(inv, sj + (si + 1) * 9, 8 + sj * 18, 84 + si * 18));
            }
        }
        // Player hotbar (slots 44-53? Actually 9 slots)
        for (int si = 0; si < 9; ++si) {
            this.addSlot(new Slot(inv, si, 8 + si * 18, 142));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true; // Not bound to a block entity, always valid
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (index < 8) {
                // Move from custom slots to player inventory
                if (!this.moveItemStackTo(itemstack1, 8, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else {
                // Move from player inventory to custom slots if possible
                if (!this.moveItemStackTo(itemstack1, 0, 8, false)) {
                    // Move between main inventory and hotbar
                    if (index < 35) {
                        if (!this.moveItemStackTo(itemstack1, 35, 44, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (index < 44) {
                        if (!this.moveItemStackTo(itemstack1, 8, 35, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                    return ItemStack.EMPTY;
                }
            }

            if (itemstack1.isEmpty()) {
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
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        // Drop any items left in custom slots when the container is closed (since no block entity)
        if (!playerIn.level().isClientSide && playerIn instanceof ServerPlayer) {
            for (int i = 0; i < internal.getSlots(); i++) {
                ItemStack stack = internal.getStackInSlot(i);
                if (!stack.isEmpty()) {
                    playerIn.drop(stack, false);
                }
            }
        }
    }

    @Override
    public Map<Integer, Slot> get() {
        return customSlots;
    }
}