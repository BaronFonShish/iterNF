package com.malignant.iter.common.event;

import com.malignant.iter.common.entity.GoblinEntity;
import com.malignant.iter.common.entity.GoblinSharpshooterEntity;
import com.malignant.iter.common.entity.GoblinWarriorEntity;
import com.malignant.iter.common.entity.HobGoblinEntity;
import com.malignant.iter.common.misc.PlayerSeverityMultiplier;
import com.malignant.iter.common.registry.ModEntities;
import com.malignant.iter.common.registry.ModTags;
import com.malignant.iter.common.variables.ModWorldData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.HashSet;
import java.util.Set;

public class GoblinPatrolEvent implements CustomSpawner {

    @Override
    public int tick(ServerLevel level, boolean enemies, boolean friendlies) {
        ModWorldData data = ModWorldData.get(level);
        long nextTick = data.getgoblinPatrolNextTick();

        if (!level.getGameRules().getRule(GameRules.RULE_DO_PATROL_SPAWNING).get()) return 0;

        if (!level.getGameRules().getRule(GameRules.RULE_DOMOBSPAWNING).get()) return 0;

        if (!enemies) return 0;

        if (level.isNight()) return 0;

        if (goblinPatrolsCount(level) > 10) return 0;

        long gameTime = level.getGameTime();
        if (gameTime < (15 * 24000)) return 0;

        if (nextTick > gameTime) return 0;

        Player player = level.getRandomPlayer();
        if (player == null) return 0;

        BlockPos spawnPos = findSpawnPosition(level, player.blockPosition());

        if (spawnPos == null) return 0;


        if (Math.random() > 0.5) {
            spawnPatrol(level, spawnPos, player);
            data.setgoblinPatrolNextTick((long) (gameTime + (14 + Math.random() * 12) * 24000));
        } else {
            data.setgoblinPatrolNextTick((long) (gameTime + (0.5 + Math.random()) * 24000));
        }
        return 0;
    }

    public static void forcePatrol(ServerLevel level, BlockPos center, Player targetPlayer) {
        BlockPos spawnPos = findSpawnPosition(level, center);
        if (spawnPos == null) return;
        spawnPatrol(level, spawnPos, targetPlayer);
    }

    private int goblinPatrolsCount(ServerLevel level) {
        var entities = level.getEntities().getAll();
        int goblinCount = 0;
        for (var entity : entities) {
            if (entity.getType().is(ModTags.EntityTypes.GOBLINS) && ((Mob) entity).isPersistenceRequired()) {
                goblinCount++;
            }
        }
        return goblinCount;
    }

    private static void spawnPatrol(ServerLevel level, BlockPos center, Player player) {

        float severity = PlayerSeverityMultiplier.compute((ServerPlayer) player);
        Set<Mob> goblins = new HashSet<>();
        goblins.add(new GoblinEntity(ModEntities.GOBLIN.get(), level));
        goblins.add(new GoblinWarriorEntity(ModEntities.GOBLIN_WARRIOR.get(), level));
        if (severity >= 0.7){goblins.add(new GoblinEntity(ModEntities.GOBLIN.get(), level));}
        if (severity >= 0.8){goblins.add(new HobGoblinEntity(ModEntities.HOBGOBLIN.get(), level));}
        if (severity >= 1) {goblins.add(new GoblinSharpshooterEntity(ModEntities.GOBLIN_SHARPSHOOTER.get(), level));
            goblins.add(new GoblinWarriorEntity(ModEntities.GOBLIN_WARRIOR.get(), level));}
        if (severity >= 1.2){goblins.add(new GoblinEntity(ModEntities.GOBLIN.get(), level));}
        if (severity >= 1.3){goblins.add(new GoblinWarriorEntity(ModEntities.GOBLIN_WARRIOR.get(), level));}
        if (severity >= 1.5){goblins.add(new GoblinSharpshooterEntity(ModEntities.GOBLIN_SHARPSHOOTER.get(), level));}
        if (severity >= 1.6){goblins.add(new HobGoblinEntity(ModEntities.HOBGOBLIN.get(), level));}

        for (Mob goblin : goblins){
            RandomSource random = level.random;
            goblin.setPos(center.getX() + (random.nextFloat()-0.5) * 2,
                    center.getY(),
                    center.getZ() + (random.nextFloat()-0.5) * 2);
            goblin.setPersistenceRequired();
            goblin.finalizeSpawn(level, level.getCurrentDifficultyAt(center), MobSpawnType.PATROL, null);
            goblin.setTarget(player);
            level.addFreshEntity(goblin);
        }
    }

    private static BlockPos findSpawnPosition(ServerLevel level, BlockPos near) {
        for (int attempt = 0; attempt < 16; attempt++) {
            double angle = level.random.nextDouble() * 2 * Math.PI;
            int distance = 32 + level.random.nextInt(64 - 32);

            BlockPos pos = near.offset(
                    (int) (Math.cos(angle) * distance),
                    0,
                    (int) (Math.sin(angle) * distance)
            );

            pos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, pos);

            if (validSpawn(level, pos)) {
                return pos;
            }
        }
        return null;
    }

    private static boolean validSpawn(ServerLevel level, BlockPos pos) {

        if (!level.getBlockState(pos.below()).isSolid()) return false;

        if (!level.getBlockState(pos).isAir()) return false;
        if (!level.getBlockState(pos.above()).isAir()) return false;

        if (!level.getWorldBorder().isWithinBounds(pos)) return false;

        return true;
    }
}
