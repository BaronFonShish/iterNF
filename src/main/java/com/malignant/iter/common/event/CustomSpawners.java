package com.malignant.iter.common.event;

import com.malignant.iter.IterMod;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber(modid = IterMod.MOD_ID)
public class CustomSpawners {
    private static final Map<ServerLevel, GoblinPatrolEvent> WORLD_SPAWNERS = new HashMap<>();

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Pre event) {
        Level level = event.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (serverLevel.dimension() != Level.OVERWORLD) return;

        GoblinPatrolEvent patrol = WORLD_SPAWNERS.computeIfAbsent(serverLevel, k -> new GoblinPatrolEvent());

        patrol.tick(serverLevel, true, false);
    }

    @SubscribeEvent
    public static void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            WORLD_SPAWNERS.remove(serverLevel);
        }
    }
}
