package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> ROTROOT_GROWABLE = tag("rotroot_growable");
        public static final TagKey<Block> ETHERBLOOM_SOIL = tag("etherbloom_soil");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, name));
        }
    }

    public static class Items {
        public static final TagKey<Item> MAGICAL_ITEM = tag("magical_item");
        public static final TagKey<Item> SPELL_FOCUS = tag("spell_focus");
        public static final TagKey<Item> GIST_SOURCE = tag("gist_source");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, name));
        }
    }
}