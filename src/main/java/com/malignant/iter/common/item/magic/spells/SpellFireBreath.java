package com.malignant.iter.common.item.magic.spells;

import com.malignant.iter.common.item.magic.defaults.ContinousSpellItem;
import com.malignant.iter.common.registry.ModDamageTypes;
import com.malignant.iter.common.registry.ModParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
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

public class SpellFireBreath extends ContinousSpellItem {

    public SpellFireBreath() {super(new Properties(), SpellDomain.PRIMAL, SpellMethod.FORCE, SpellAspect.FIRE,1, 1f, 2);}

    @Override
    public void castSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower) {
    }

    @Override
    public void castContinousSpell(Level level, Player player, ItemStack wand, ItemStack spellStack, float spellpower, int ticks) {
        int iterations = (int) (20 + (10 * spellpower));

        Set<Entity> hitEntities = new HashSet<>();

        float dist = 0;
        double yheight = player.getEyePosition().y;
        double xdir = player.getLookAngle().x;
        double ydir = player.getLookAngle().y;
        double zdir = player.getLookAngle().z;
        boolean flag = true;

        if (ticks % 5 == 0) {
            level.playSound(null, player.blockPosition(),
                    SoundEvents.FIRECHARGE_USE,
                    SoundSource.PLAYERS, 0.5f, 1f);
        }

        for (int i = 0; i < iterations; i++) {

            if (level.getBlockState(BlockPos.containing(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist)).isSolid()) {
                break;
            }

            if (i % 2 == 0) {

                float spread = ((float) i / (float) iterations)+0.05f;
                RandomSource random = level.getRandom();

                Vec3 coneDir = player.getLookAngle().add((random.nextFloat() - 0.5) * spread, (random.nextFloat() - 0.5) * spread, (random.nextFloat() - 0.5) * spread);

                if (level.isClientSide()) {
                    ParticleOptions particle = ModParticleTypes.FLAME.get();
                    if (i % 4 == 0){
                        particle = ParticleTypes.SMOKE;
                    }
                    level.addParticle(particle, player.getX() + xdir * dist*0.5 + (random.nextFloat() - 0.5) * spread * 2,
                            player.getEyeY() + ydir * dist*0.5 + (random.nextFloat() - 0.5) * spread * 2,
                            player.getZ() + zdir * dist*0.5 + (random.nextFloat() - 0.5) * spread * 2,
                            coneDir.x() * 0.5, coneDir.y() * 0.5, coneDir.z() * 0.5);
                }
            }

            if (ticks % 5 == 0) {
                final Vec3 center = new Vec3(player.getX() + xdir * dist, yheight + ydir * dist, player.getZ() + zdir * dist);
                List<Entity> entfound = level.getEntitiesOfClass(Entity.class,
                        new AABB(center, center).inflate(1 + dist / 15f),
                        e -> e instanceof LivingEntity && e != player);

                hitEntities.addAll(entfound);
            }

            dist = dist + 0.2f;
        }

        if (ticks % 5 == 0) {
            double pierce = 1;

            Vec3 playerPos = player.position();
            List<Entity> sortedEntities = hitEntities.stream()
                    .sorted(Comparator.comparingDouble(e -> e.distanceToSqr(playerPos)))
                    .collect(Collectors.toList());

            for (Entity entityiterator : sortedEntities) {
                if (entityiterator instanceof LivingEntity) {
                    if (entityiterator != player) {
                        DamageSource damageSource = new DamageSource(
                                level.registryAccess()
                                        .registryOrThrow(Registries.DAMAGE_TYPE)
                                        .getHolderOrThrow(ModDamageTypes.SPELL),
                                player
                        );
                        int flamecap = (int) (5*spellpower*pierce * 20 + 50);
                        if (entityiterator.getRemainingFireTicks() < flamecap) {
                            entityiterator.setRemainingFireTicks(entityiterator.getRemainingFireTicks() + 20 + (int) (10 * spellpower * pierce));
                        }
                        entityiterator.hurt(damageSource, 1.25f * spellpower * (float) pierce);
                        pierce = pierce * 0.8;
                    }
                }
            }
        }
    }
}