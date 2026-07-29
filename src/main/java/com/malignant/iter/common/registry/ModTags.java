package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
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
        public static final TagKey<Item> COPPER_ROUNDS = tag("copper_rounds");
        public static final TagKey<Item> DEEPSTEEL_ROUNDS = tag("deepsteel_rounds");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> GOBLINS = tag("goblins");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_WIZARD_TOWER = tag("has_wizard_tower");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, name));
        }
    }
}