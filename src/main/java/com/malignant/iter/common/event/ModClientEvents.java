package com.malignant.iter.common.event;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.item.AbstractIterBow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ComputeFovModifierEvent;

@EventBusSubscriber(modid = IterMod.MOD_ID, value = Dist.CLIENT)
public class ModClientEvents {

    @SubscribeEvent
    public static void onComputeFovModifier(ComputeFovModifierEvent event) {
        Player player = event.getPlayer();

        if (!player.isUsingItem()) return;

        Item item = player.getUseItem().getItem();

        if (item instanceof AbstractIterBow customBow) {
            float fovModifier = 1.0F;

            int ticksUsing = player.getTicksUsingItem();
            int drawDuration = customBow.getDrawDuration();

            float pullProgress = (float) ticksUsing / drawDuration;
            if (pullProgress > 1.0F) {
                pullProgress = 1.0F;
            }

            float f = 1.0F;
            if (pullProgress > 0.0F) {
                float pullAmount = BowItem.getPowerForTime(ticksUsing);
                f = 1.0F - pullAmount * 0.15F;
            }

            event.setNewFovModifier(f);
        }
    }
}