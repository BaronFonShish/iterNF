package com.malignant.iter.common.event;

import com.malignant.iter.common.item.magic.defaults.SpellFocus;
import com.malignant.iter.common.registry.ModTags;
import com.malignant.iter.common.variables.IterPlayerDataUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = "iter", value = Dist.CLIENT)
public class MageOverlay {

    private static final ResourceLocation MANA_OVERLAY = ResourceLocation.parse("iter:textures/gui/mana_overlay.png");
    private static final ResourceLocation ETHERBAR_FULL = ResourceLocation.parse("iter:textures/gui/etherbar_full.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player == null) return;

        boolean display = (player.getMainHandItem().getItem() instanceof SpellFocus) ||
                player.getMainHandItem().is(ModTags.Items.MAGICAL_ITEM);

        if (!display) return;

        float burnout = IterPlayerDataUtils.getBurnout(player);
        float threshold = IterPlayerDataUtils.getThreshold(player);

        float perc = 1f - (burnout / threshold);
        int progress = Math.min(Math.max(0, (int)(perc * 83)), 84);

        event.getGuiGraphics().blit(MANA_OVERLAY, 6, 5, 0, 0, 120, 50, 120, 50);

        String burnoutText = (int) burnout + "/" + (int) threshold;
        event.getGuiGraphics().drawString(mc.font, burnoutText, 11, 25, -16750900, false);

        String spellNumber = MageOverlayUtils.SlotNumber(player);
        event.getGuiGraphics().drawString(mc.font, spellNumber, 11, 42, -16737844, false);

        String spellName = MageOverlayUtils.SpellName(player);
        event.getGuiGraphics().drawString(mc.font, spellName, 23, 42, -3381505, false);

        event.getGuiGraphics().blit(ETHERBAR_FULL, 9, 8, 0, 0, 83 - progress, 9, 83, 9);
    }
}