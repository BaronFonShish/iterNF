package com.malignant.iter.common.misc;

import com.malignant.iter.IterMod;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;


public class Pictograms {
    public static final char ID_ARCANE = '\uE000';
    public static final char ID_PRIMAL = '\uE001';
    public static final char ID_OCCULT = '\uE002';
    public static final char IM_FORCE = '\uE003';
    public static final char IM_FORM = '\uE004';
    public static final char IM_BODY = '\uE005';
    public static final char IM_CONVEYANCE = '\uE006';
    public static final char IA_FIRE = '\uE007';
    public static final char IA_FROST = '\uE008';
    public static final char IA_LIGHTNING = '\uE009';
    public static final char IA_WATER = '\uE00a';
    public static final char IA_EARTH = '\uE00b';
    public static final char IA_AIR = '\uE00c';
    public static final char IA_ETHER = '\uE00d';
    public static final char IA_LIFE = '\uE00e';
    public static final char IA_ENTROPY = '\uE00f';

    public static final char AT_BLANK = '\uE0f0';
    public static final char AT_FLINT = '\uE0f1';
    public static final char AT_GOBLIN = '\uE0f2';
    public static final char AT_IRON = '\uE0f3';
    public static final char AT_SEED = '\uE0f4';
    public static final char AT_AMETHYST = '\uE0f5';
    public static final char AT_DIAMOND = '\uE0f6';
    public static final char AT_MAGMANUM = '\uE0f7';
    public static final char AT_FLECHETTE = '\uE0f8';

    public static final ResourceLocation PICTOGRAM_FONT = ResourceLocation.parse(IterMod.MOD_ID + ":iter_pictograms");

    public static MutableComponent getIcon(char icon) {
        return Component.literal(String.valueOf(icon))
                .withStyle(Style.EMPTY.withFont(PICTOGRAM_FONT));
    }
}