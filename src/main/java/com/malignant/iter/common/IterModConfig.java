package com.malignant.iter.common;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class IterModConfig {

    public static class Common {
        private static final ModConfigSpec SPEC;
        private static final Common INSTANCE;

        static {
            Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
            INSTANCE = pair.getLeft();
            SPEC = pair.getRight();
        }

        private final ModConfigSpec.BooleanValue ancientVases;
        private final ModConfigSpec.BooleanValue abyssQuartz;
        private final ModConfigSpec.BooleanValue spiderEggs;
        private final ModConfigSpec.BooleanValue etherBlooms;
        private final ModConfigSpec.BooleanValue rotroots;
        private final ModConfigSpec.BooleanValue fociDurability;
        private final ModConfigSpec.BooleanValue abyssquartzGrowth;
        private final ModConfigSpec.BooleanValue giantSpiders;
        private final ModConfigSpec.BooleanValue ghouls;
        private final ModConfigSpec.BooleanValue goblinPatrols;

        private Common(ModConfigSpec.Builder builder) {
            builder.comment("Common configuration for Iter").push("iter");

            builder.comment("World Generation Settings").push("world_generation");
            ancientVases = builder
                    .comment("Enable ancient vases generation")
                    .define("ancientVases", true);
            abyssQuartz = builder
                    .comment("Enable abyssquartz clusters generation")
                    .define("abyssQuartz", true);
            spiderEggs = builder
                    .comment("Enable spider eggs generation")
                    .define("spiderEggs", true);
            etherBlooms = builder
                    .comment("Enable etherblooms generation")
                    .define("etherBlooms", true);
            rotroots = builder
                    .comment("Enable rotroots generation")
                    .define("rotroots", true);
            builder.pop();

            builder.comment("Function Settings").push("functions");
            fociDurability = builder
                    .comment("Enable foci item damage from spellcasting")
                    .define("fociDurability", false);
            abyssquartzGrowth = builder
                    .comment("Enable abyssquartz blocks growing new clusters")
                    .define("abyssquartzGrowth", true);
            builder.pop();

            builder.comment("Mob Settings").push("mobs");
            giantSpiders = builder
                    .comment("Enable Giant Spiders spawning")
                    .define("giantSpiders", true);
            ghouls = builder
                    .comment("Enable Ghouls spawning")
                    .define("ghouls", true);
            goblinPatrols = builder
                    .comment("Enable Goblin patrols")
                    .define("goblinPatrols", true);
            builder.pop();

            builder.pop();
        }

        public static ModConfigSpec getSpec() { return SPEC; }

        public static boolean ancientVases() { return INSTANCE.ancientVases.get(); }
        public static boolean abyssQuartz() { return INSTANCE.abyssQuartz.get(); }
        public static boolean spiderEggs() { return INSTANCE.spiderEggs.get(); }
        public static boolean etherBlooms() { return INSTANCE.etherBlooms.get(); }
        public static boolean rotroots() { return INSTANCE.rotroots.get(); }
        public static boolean fociDurability() { return INSTANCE.fociDurability.get(); }
        public static boolean abyssquartzGrowth() { return INSTANCE.abyssquartzGrowth.get(); }
        public static boolean giantSpiders() { return INSTANCE.giantSpiders.get(); }
        public static boolean ghouls() { return INSTANCE.ghouls.get(); }
        public static boolean goblinPatrols() { return INSTANCE.goblinPatrols.get(); }
    }

    public static void register(ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Common.getSpec(), "iter.toml");
    }
}