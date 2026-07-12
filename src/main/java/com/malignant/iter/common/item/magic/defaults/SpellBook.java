package com.malignant.iter.common.item.magic.defaults;

import com.malignant.iter.common.variables.SpellbookCapability;
import com.malignant.iter.common.world.gui.SpellBookGuiMenu;
import io.netty.buffer.Unpooled;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpellBook extends Item {
    private static final String CAPABILITY_DATA_KEY = "spellbook_inventory";

    public SpellBook() {
        super(new Properties()
                .stacksTo(1)
                .rarity(Rarity.UNCOMMON)
                .component(DataComponents.CUSTOM_DATA, CustomData.EMPTY)
        );
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        ItemStack stack = entity.getItemInHand(hand);

        if (entity instanceof ServerPlayer serverPlayer) {
            final int handId = hand == InteractionHand.MAIN_HAND ? 0 : 1;
            final ItemStack heldStack = stack;

            serverPlayer.openMenu(new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.translatable("container.iter.spell_book");
                }

                @Override
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return new SpellBookGuiMenu(id, inventory, handId, heldStack);
                }
            });
            return InteractionResultHolder.consume(stack);
        }

        return InteractionResultHolder.success(stack);
    }

    @Nullable
    public IItemHandler getSpellbookInventory(ItemStack stack, HolderLookup.Provider lookupProvider) {
        SpellbookCapability cap = getSpellbookCapability(stack, lookupProvider);
        return cap != null ? cap.getCapability(null) : null;
    }

    public void saveSpellbookInventory(ItemStack stack, SpellbookCapability capability, HolderLookup.Provider lookupProvider) {
        CompoundTag capabilityData = capability.serializeNBT(lookupProvider);
        if (!capabilityData.isEmpty()) {
            CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            CompoundTag newTag = customData.copyTag();
            newTag.put(CAPABILITY_DATA_KEY, capabilityData);
            stack.set(DataComponents.CUSTOM_DATA, CustomData.of(newTag));
        }
    }

    @Nullable
    public SpellbookCapability getSpellbookCapability(ItemStack stack, HolderLookup.Provider lookupProvider) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) return null;

        SpellbookCapability capability = new SpellbookCapability();
        if (customData.contains(CAPABILITY_DATA_KEY)) {
            CompoundTag capabilityData = customData.copyTag().getCompound(CAPABILITY_DATA_KEY);
            capability.deserializeNBT(lookupProvider, capabilityData);
        }
        return capability;
    }
}