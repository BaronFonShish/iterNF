package com.malignant.iter.common.item;

import com.malignant.iter.common.registry.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;
import org.jetbrains.annotations.Nullable;

public class MagmanumItem extends Item {

    public MagmanumItem(Properties pProperties) {
        super(pProperties.rarity(Rarity.UNCOMMON).fireResistant());
    }

    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType)
    {
        return 6400;
    }

    @SubscribeEvent
    public static void furnaceFuelBurnTimeEvent(FurnaceFuelBurnTimeEvent event) {
        ItemStack itemstack = event.getItemStack();
        if (itemstack.getItem() == ModItems.MAGMANUM.get())
            event.setBurnTime(6400);

    }
}