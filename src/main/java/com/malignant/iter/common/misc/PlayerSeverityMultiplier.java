package com.malignant.iter.common.misc;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class PlayerSeverityMultiplier {

    private static final ResourceLocation MINE_STONE =
            ResourceLocation.withDefaultNamespace("story/mine_stone");
    private static final ResourceLocation SMELT_IRON =
            ResourceLocation.withDefaultNamespace("story/smelt_iron");
    private static final ResourceLocation IRON_TOOLS =
            ResourceLocation.withDefaultNamespace("story/iron_tools");
    private static final ResourceLocation IRON_ARMOR =
            ResourceLocation.withDefaultNamespace("story/obtain_armor");
    private static final ResourceLocation DIAMOND =
            ResourceLocation.withDefaultNamespace("story/mine_diamond");
    private static final ResourceLocation DIAMOND_ARMOR =
            ResourceLocation.withDefaultNamespace("story/shiny_gear");
    private static final ResourceLocation ENCHANT =
            ResourceLocation.withDefaultNamespace("story/enchant_item");
    private static final ResourceLocation NETHER =
            ResourceLocation.withDefaultNamespace("story/enter_the_nether");
    private static final ResourceLocation BLAZE_ROD =
            ResourceLocation.withDefaultNamespace("nether/obtain_blaze_rod");
    private static final ResourceLocation WITHER_SKULL =
            ResourceLocation.withDefaultNamespace("nether/get_wither_skull");
    private static final ResourceLocation NETHERITE =
            ResourceLocation.withDefaultNamespace("nether/obtain_ancient_debris");
    private static final ResourceLocation NETHERITE_ARMOR =
            ResourceLocation.withDefaultNamespace("nether/netherite_armor");
    private static final ResourceLocation SUMMON_WITHER =
            ResourceLocation.withDefaultNamespace("nether/summon_wither");
    private static final ResourceLocation END =
            ResourceLocation.withDefaultNamespace("story/enter_the_end");
    private static final ResourceLocation ENDER_DRAGON =
            ResourceLocation.withDefaultNamespace("end/kill_dragon");
    private static final ResourceLocation ELYTRA =
            ResourceLocation.withDefaultNamespace("end/elytra");

    public static float compute(ServerPlayer player){
        float severity = 0.5f;
        if (hasAdvancement(player, MINE_STONE)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, SMELT_IRON)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, IRON_TOOLS)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, IRON_ARMOR)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, DIAMOND)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, DIAMOND_ARMOR)){
            severity += 0.2f;
        }
        if (hasAdvancement(player, ENCHANT)){
            severity += 0.05f;
        }
        if (hasAdvancement(player, NETHER)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, BLAZE_ROD)){
            severity += 0.05f;
        }
        if (hasAdvancement(player, WITHER_SKULL)){
            severity += 0.05f;
        }
        if (hasAdvancement(player, NETHERITE)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, NETHERITE_ARMOR)){
            severity += 0.2f;
        }
        if (hasAdvancement(player, SUMMON_WITHER)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, END)){
            severity += 0.1f;
        }
        if (hasAdvancement(player, ENDER_DRAGON)){
            severity += 0.25f;
        }
        if (hasAdvancement(player, ELYTRA)){
            severity += 0.1f;
        }
        System.out.println("Severity of " + player.getDisplayName() + ": " + severity);
        return severity;
    }

    private static boolean hasAdvancement(ServerPlayer player, ResourceLocation advancementId) {
        AdvancementHolder advancement = player.serverLevel()
                .getServer()
                .getAdvancements()
                .get(advancementId);

        if (advancement == null) return false;
        return player.getAdvancements().getOrStartProgress(advancement).isDone();
    }
}
