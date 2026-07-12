package com.malignant.iter;

import com.malignant.iter.client.model.HobGoblinModel;
import com.malignant.iter.client.model.ModModelLayers;
import com.malignant.iter.client.renderer.*;
import com.malignant.iter.common.IterModConfig;
import com.malignant.iter.common.entity.*;
import com.malignant.iter.common.payload.*;
import com.malignant.iter.common.registry.*;
import com.mojang.logging.LogUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(IterMod.MOD_ID)
public class IterMod {
    public static final String MOD_ID = "iter";
    public static final ResourceLocation PICTOGRAM_FONT = ResourceLocation.parse(MOD_ID + ":font/iter_pictograms.json");
    private static final Logger LOGGER = LogUtils.getLogger();

    public static RegistryAccess registryAccess;

    public IterMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerScreens);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.REGISTRY.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModTabs.REGISTRY.register(modEventBus);
        ModFeatures.REGISTRY.register(modEventBus);
        ModProcessors.PROCESSORS.register(modEventBus);
        ModRuleTests.RULE_TEST_TYPES.register(modEventBus);
        ModStructures.STRUCTURE_TYPES.register(modEventBus);
        ModAttributes.ATTRIBUTES.register(modEventBus);
        ModParticleTypes.REGISTRY.register(modEventBus);
        modEventBus.addListener(PayloadRegistry::registerPayloads);
        ModMenus.register(modEventBus);

        ModCapabilities.ATTACHMENT_TYPES.register(modEventBus);


        IterModConfig.register(modContainer);
        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void registerScreens(RegisterMenuScreensEvent event) {
        ModScreens.registerScreens(event);
    }

    @SubscribeEvent
    public void onServerAboutToStart(ServerAboutToStartEvent event) {
        registryAccess = event.getServer().registryAccess();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }



    @EventBusSubscriber(modid = MOD_ID)
    public static class ModEventSubscriber {

        @SubscribeEvent
        public static void onEntityAttributeCreate(EntityAttributeCreationEvent event) {
            event.put(ModEntities.SPIDERLING.get(), SpiderlingEntity.createAttributes().build());
            event.put(ModEntities.GIANT_SPIDER.get(), GiantSpiderEntity.createAttributes().build());
            event.put(ModEntities.GHOUL.get(), GhoulEntity.createAttributes().build());
            event.put(ModEntities.DARK_SORCERER.get(), DarkSorcererEntity.createAttributes().build());
            event.put(ModEntities.BEREFT.get(), BereftEntity.createAttributes().build());
            event.put(ModEntities.GOBLIN_WARRIOR.get(), GoblinWarriorEntity.createAttributes().build());
            event.put(ModEntities.GOBLIN.get(), GoblinEntity.createAttributes().build());
            event.put(ModEntities.HOBGOBLIN.get(), HobGoblinEntity.createAttributes().build());
        }

        @SubscribeEvent
        public static void onRegisterSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        }
    }

    @EventBusSubscriber(modid = MOD_ID, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(ModItemProperties::registerItemProperties);
        }

        @SubscribeEvent
        public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
            ModModelLayers.registerLayerDefinitions(event);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(ModEntities.SPIDERLING.get(), SpiderlingRenderer::new);
            event.registerEntityRenderer(ModEntities.GIANT_SPIDER.get(), GiantSpiderRenderer::new);
            event.registerEntityRenderer(ModEntities.GOBLIN_WARRIOR.get(), GoblinWarriorRenderer::new);
            event.registerEntityRenderer(ModEntities.GOBLIN.get(), GoblinRenderer::new);
            event.registerEntityRenderer(ModEntities.HOBGOBLIN.get(), HobGoblinRenderer::new);
            event.registerEntityRenderer(ModEntities.GHOUL.get(), GhoulRenderer::new);
            event.registerEntityRenderer(ModEntities.DARK_SORCERER.get(), DarkSorcererRenderer::new);
            event.registerEntityRenderer(ModEntities.BEREFT.get(), BereftRenderer::new);

            event.registerEntityRenderer(ModEntities.ETHERBOLT.get(), EtherboltRenderer::new);
            event.registerEntityRenderer(ModEntities.FLAMEBOLT.get(), FlameboltRenderer::new);
            event.registerEntityRenderer(ModEntities.FROST_SPIKE.get(), FrostSpikeRenderer::new);
            event.registerEntityRenderer(ModEntities.FIREBALL.get(), FireBallRenderer::new);
            event.registerEntityRenderer(ModEntities.HELLBLAZE_ARROW.get(), HellblazeArrowRenderer::new);
            event.registerEntityRenderer(ModEntities.STRAIGHT_BEAM.get(), StraightBeamRenderer::new);
            event.registerEntityRenderer(ModEntities.JAGGED_BEAM.get(), JaggedBeamRenderer::new);
        }

        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(ModKeyBinds.SPELL_SLOT_SELECT);
        }
    }
}
