package com.malignant.iter.common.item.magic.spells;

import com.malignant.iter.common.item.magic.defaults.ContinousSpellItem;
import com.malignant.iter.common.variables.IterPlayerDataUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SpellHeal extends ContinousSpellItem {

    public SpellHeal() {super(new Properties(), SpellDomain.ARCANE, SpellMethod.BODY, SpellAspect.LIFE,2, 4, 2);}

    @Override
    public void castSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower) {
    }

    @Override
    public void castContinousSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {

        if (player.isShiftKeyDown() && (player.getHealth() < player.getMaxHealth())) {
            player.heal(0.05f * spellpower);
            IterPlayerDataUtils.addBurnout(player, (this.getManaCost(player, spellStack) / 20));
            if (ticks % 5 == 0 && level instanceof ServerLevel serverLevel){
                level.playSound(null, player.blockPosition(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                        SoundSource.PLAYERS, 0.5f, 1f);
                serverLevel.sendParticles(ParticleTypes.HEART,
                        player.getX(), player.getY() + player.getBbHeight()/2, player.getZ(),
                        1, player.getBbWidth()/2, player.getBbHeight()/2, player.getBbWidth()/2, 0);
            }
        } else {

            int iterations = (int) (5 + (5 * spellpower));

            Set<Entity> healedEntity = new HashSet<>();

            float dist = 0;
            double yheight = player.getEyePosition().y;
            double xdir = player.getLookAngle().x;
            double ydir = player.getLookAngle().y;
            double zdir = player.getLookAngle().z;
            boolean flag = true;

            for (int i = 0; i < iterations; i++) {

                if (level.getBlockState(BlockPos.containing(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist)).isSolid()) {
                    break;
                }

                    final Vec3 center = new Vec3(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist);
                    List<Entity> entfound = level.getEntitiesOfClass(Entity.class,
                            new AABB(center, center).inflate(1 + dist / 15f),
                            e -> e instanceof LivingEntity && e != player);
                    healedEntity.addAll(entfound);

                dist = dist + 0.2f;
            }


                Vec3 playerPos = player.position();
                List<Entity> sortedEntities = healedEntity.stream()
                        .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(playerPos)))
                        .collect(Collectors.toList());

                for (Entity entityiterator : sortedEntities) {
                    if (flag) {
                        if (entityiterator instanceof LivingEntity livingEntity && livingEntity.isAlive() && (livingEntity.getHealth() < livingEntity.getMaxHealth())) {
                            flag = false;
                            livingEntity.heal(0.05f * spellpower);
                            IterPlayerDataUtils.addBurnout(player, (this.getManaCost(player, spellStack) / 20));
                            if (ticks % 5 == 0 && level instanceof ServerLevel serverLevel){
                                level.playSound(null, player.blockPosition(),
                                        SoundEvents.EXPERIENCE_ORB_PICKUP,
                                        SoundSource.PLAYERS, 0.5f, 1f);
                                serverLevel.sendParticles(ParticleTypes.HEART,
                                        livingEntity.getX(), livingEntity.getY() + livingEntity.getBbHeight()/2, livingEntity.getZ(),
                                        1, livingEntity.getBbWidth(), livingEntity.getBbHeight(), livingEntity.getBbWidth(), 0);
                            }
                        }
                    }
                }
        }
    }

    @Override
    public void spellTick(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {
        if (ticks >= 5) {
            castContinousSpell(level, player, wand, spellStack, spellpower, ticks);
        }
    }
}