package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.entity.*;
import com.malignant.iter.common.entity.projectile.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModEntities {


    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, IterMod.MOD_ID);


    public static final DeferredHolder<EntityType<?>, EntityType<SpiderlingEntity>> SPIDERLING =
            ENTITY_TYPES.register("spiderling",
                    () -> EntityType.Builder.of(SpiderlingEntity::new, MobCategory.MONSTER)
                            .sized(0.6f, 0.5f).eyeHeight(0.35f)
                            .build("spiderling"));

    public static final DeferredHolder<EntityType<?>, EntityType<GiantSpiderEntity>> GIANT_SPIDER =
            ENTITY_TYPES.register("giant_spider",
                    () -> EntityType.Builder.of(GiantSpiderEntity::new, MobCategory.MONSTER)
                            .sized(1.5f, 1.4f)
                            .build("giant_spider"));

    public static final DeferredHolder<EntityType<?>, EntityType<ThornbackEntity>> THORNBACK =
            ENTITY_TYPES.register("thornback",
                    () -> EntityType.Builder.of(ThornbackEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(2f, 2.25f).eyeHeight(1.5f)
                            .build("thornback"));

    public static final DeferredHolder<EntityType<?>, EntityType<GhoulEntity>> GHOUL =
            ENTITY_TYPES.register("ghoul",
                    () -> EntityType.Builder.of(GhoulEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.6f, 1.975f)
                            .build("ghoul"));

    public static final DeferredHolder<EntityType<?>, EntityType<DarkSorcererEntity>> DARK_SORCERER =
            ENTITY_TYPES.register("dark_sorcerer",
                    () -> EntityType.Builder.of(DarkSorcererEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.6f, 1.975f)
                            .build("dark_sorcerer"));

    public static final DeferredHolder<EntityType<?>, EntityType<BereftEntity>> BEREFT =
            ENTITY_TYPES.register("bereft",
                    () -> EntityType.Builder.of(BereftEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.6f, 2.05f)
                            .build("bereft"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinWarriorEntity>> GOBLIN_WARRIOR =
            ENTITY_TYPES.register("goblin_warrior",
                    () -> EntityType.Builder.of(GoblinWarriorEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.5f, 1f)
                            .build("goblin_warrior"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinEntity>> GOBLIN =
            ENTITY_TYPES.register("goblin",
                    () -> EntityType.Builder.of(GoblinEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.5f, 0.9f)
                            .build("goblin"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinSharpshooterEntity>> GOBLIN_SHARPSHOOTER =
            ENTITY_TYPES.register("goblin_sharpshooter",
                    () -> EntityType.Builder.<GoblinSharpshooterEntity>of(GoblinSharpshooterEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.5f, 1f)
                            .build("goblin_sharpshooter"));

    public static final DeferredHolder<EntityType<?>, EntityType<HobGoblinEntity>> HOBGOBLIN =
            ENTITY_TYPES.register("hobgoblin",
                    () -> EntityType.Builder.<HobGoblinEntity>of(HobGoblinEntity::new, MobCategory.MONSTER)
                            .setTrackingRange(64)
                            .sized(0.9f, 2.5f)
                            .build("hobgoblin"));

///// Снаряды

    public static final DeferredHolder<EntityType<?>, EntityType<EtherboltEntity>> ETHERBOLT =
            ENTITY_TYPES.register("etherbolt",
                    () -> EntityType.Builder.<EtherboltEntity>of(EtherboltEntity::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.25f, 0.25f)
                            .build("etherbolt"));

    public static final DeferredHolder<EntityType<?>, EntityType<StraightBeam>> STRAIGHT_BEAM =
            ENTITY_TYPES.register("straight_beam",
                    () -> EntityType.Builder.<StraightBeam>of(StraightBeam::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .clientTrackingRange(256)
                            .updateInterval(1)
                            .build("straight_beam"));

    public static final DeferredHolder<EntityType<?>, EntityType<JaggedBeam>> JAGGED_BEAM =
            ENTITY_TYPES.register("jagged_beam",
                    () -> EntityType.Builder.<JaggedBeam>of(JaggedBeam::new, MobCategory.MISC)
                            .sized(1F, 1F)
                            .clientTrackingRange(256)
                            .updateInterval(1)
                            .build("jagged_beam"));

    public static final DeferredHolder<EntityType<?>, EntityType<FrostSpikeEntity>> FROST_SPIKE =
            ENTITY_TYPES.register("frost_spike",
                    () -> EntityType.Builder.<FrostSpikeEntity>of(FrostSpikeEntity::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.25f, 0.25f)
                            .build("frost_spike"));

    public static final DeferredHolder<EntityType<?>, EntityType<FlameboltEntity>> FLAMEBOLT =
            ENTITY_TYPES.register("flamebolt",
                    () -> EntityType.Builder.<FlameboltEntity>of(FlameboltEntity::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.25f, 0.25f)
                            .build("flamebolt"));


    public static final DeferredHolder<EntityType<?>, EntityType<FireballEntity>> FIREBALL =
            ENTITY_TYPES.register("fireball",
                    () -> EntityType.Builder.<FireballEntity>of(FireballEntity::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.5f, 0.5f)
                            .build("fireball"));

    public static final DeferredHolder<EntityType<?>, EntityType<HellblazeArrowEntity>> HELLBLAZE_ARROW =
            ENTITY_TYPES.register("hellblaze_arrow",
                    () -> EntityType.Builder.<HellblazeArrowEntity>of(HellblazeArrowEntity::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.25f, 0.25f)
                            .build("hellblaze_arrow"));


    public static final DeferredHolder<EntityType<?>, EntityType<IronBullet>> IRON_BULLET =
            ENTITY_TYPES.register("iron_bullet",
                    () -> EntityType.Builder.<IronBullet>of(IronBullet::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.05f, 0.05f)
                            .build("iron_bullet"));

    public static final DeferredHolder<EntityType<?>, EntityType<GoblinBullet>> GOBLIN_BULLET =
            ENTITY_TYPES.register("goblin_bullet",
                    () -> EntityType.Builder.<GoblinBullet>of(GoblinBullet::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.05f, 0.05f)
                            .build("goblin_bullet"));

    public static final DeferredHolder<EntityType<?>, EntityType<SeedshotBullet>> SEEDSHOT_BULLET =
            ENTITY_TYPES.register("seedshot_bullet",
                    () -> EntityType.Builder.<SeedshotBullet>of(SeedshotBullet::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.05f, 0.05f)
                            .build("seedshot_bullet"));

    public static final DeferredHolder<EntityType<?>, EntityType<FlintBullet>> FLINT_BULLET =
            ENTITY_TYPES.register("flint_bullet",
                    () -> EntityType.Builder.<FlintBullet>of(FlintBullet::new, MobCategory.MISC)
                            .setShouldReceiveVelocityUpdates(true)
                            .setTrackingRange(64).setUpdateInterval(1)
                            .sized(0.05f, 0.05f)
                            .build("flint_bullet"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
