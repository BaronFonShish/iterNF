package com.malignant.iter.common.item.magic.spells;

import com.malignant.iter.common.item.magic.defaults.ContinousSpellItem;
import com.malignant.iter.common.item.magic.defaults.SpellItem;
import com.malignant.iter.common.registry.ModDamageTypes;
import com.malignant.iter.common.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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

public class SpellGust extends ContinousSpellItem {

    public SpellGust() {super(new Properties(), SpellDomain.PRIMAL, SpellMethod.FORCE, SpellAspect.AIR, 1, 1f, 2);}

    @Override
    public void castSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower) {
    }

    @Override
    public void castContinousSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {

        if (ticks % getRate() == 0) {
            int iterations = (int) (15 + (5 * spellpower));
            Set<Entity> hitEntities = new HashSet<>();

            float dist = 0;
            double yheight = player.getEyePosition().y;
            double xdir = player.getLookAngle().x;
            double ydir = player.getLookAngle().y;
            double zdir = player.getLookAngle().z;
            boolean flag = true;

            level.playSound(null, player.blockPosition(),
                    SoundEvents.PHANTOM_FLAP,
                    SoundSource.PLAYERS, 0.8f, 1.2f);

            for (int i = 0; i < iterations; i++) {

                if (level.getBlockState(BlockPos.containing(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist)).isSolid()) {
                    break;
                }

                if (i % 4 == 0 && ticks % 2 == 0) {

                    float spread = ((float) i / (float) iterations) + 0.05f;
                    RandomSource random = level.getRandom();

                    Vec3 coneDir = player.getLookAngle().add((random.nextFloat() - 0.5) * spread, (random.nextFloat() - 0.5) * spread, (random.nextFloat() - 0.5) * spread);

                    if (level.isClientSide()) {
                        level.addParticle(ParticleTypes.POOF, player.getX() + xdir * dist + (random.nextFloat() - 0.5) * spread * 2,
                                player.getEyeY() + ydir * dist + (random.nextFloat() - 0.5) * spread * 2,
                                player.getZ() + zdir * dist + (random.nextFloat() - 0.5) * spread * 2,
                                coneDir.x() * 0.5, coneDir.y() * 0.75, coneDir.z() * 0.75);
                    }
                }

                final Vec3 center = new Vec3(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist);

                List<Entity> entfound = level.getEntitiesOfClass(Entity.class,
                        new AABB(center, center).inflate(1 + dist / 15f),
                        e -> e instanceof Entity && e != player);

                hitEntities.addAll(entfound);

                dist = dist + 0.2f;
            }
            double pierce = 1;

            Vec3 playerPos = player.position();
            List<Entity> sortedEntities = hitEntities.stream()
                    .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(playerPos)))
                    .collect(Collectors.toList());

            for (Entity entityiterator : sortedEntities) {
                if ((entityiterator instanceof LivingEntity) || (entityiterator instanceof ItemEntity)) {
                    if (entityiterator != player) {
                        Vec3 newMotion = entityiterator.getDeltaMovement().add(new Vec3(
                                xdir * spellpower * 0.2 * pierce,
                                ydir * spellpower * 0.2 * pierce,
                                zdir * spellpower * 0.2 * pierce));
                        entityiterator.setDeltaMovement(newMotion);
                        pierce = pierce * 0.8;
                    }
                }
            }
        }
    }
}