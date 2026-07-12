package com.malignant.iter.common.item;

import com.malignant.iter.common.registry.ModEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public abstract class AbstractIterBow extends BowItem {

    public AbstractIterBow(Properties properties) {
        super(properties);
    }

    public abstract double flatDamageBonus();        // 0
    public abstract double powerMult();             // 1
    public abstract int getDrawDuration();         // 20
    public abstract float getVelocityMultiplier(); // 3

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int timeLeft) {
        if (!(entity instanceof Player player)) return;

        boolean isCreative = player.getAbilities().instabuild;
        ItemStack ammoStack = player.getProjectile(stack);

        if (ammoStack.isEmpty() && !isCreative) return;

        // Calculate charge time and power using custom draw duration
        int charge = this.getUseDuration(stack, entity) - timeLeft;
        float power = getPowerForTime(charge, getDrawDuration());

        if (power < 0.1f) return;

        // Get arrow type
        ArrowItem arrowItem = (ArrowItem) (ammoStack.getItem() instanceof ArrowItem ? ammoStack.getItem() : Items.ARROW);
        boolean hasInfinity = (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.getHolder(player.registryAccess(), Enchantments.INFINITY), stack) > 0);
        boolean hasInfiniteArrows = isCreative || (hasInfinity && arrowItem == Items.ARROW);

        if (!level.isClientSide) {
            AbstractArrow arrow = arrowItem.createArrow((ServerLevel) level, ammoStack, player, null);

            // Apply damage: vanilla base (2.0) + power enchant + custom bonus
            double vanillaDamage = arrow.getBaseDamage(); // Usually 2.0
            int powerLevel = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.getHolder(player.registryAccess(), Enchantments.POWER), stack);
            double powerBonus = powerLevel > 0 ? powerLevel * 0.5 + 0.5 : 0.0;
            powerBonus *= powerMult();
            double finalDamage = vanillaDamage + powerBonus + flatDamageBonus();
            arrow.setBaseDamage(finalDamage);

            // Apply flame
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.getHolder(player.registryAccess(), Enchantments.FLAME), stack) > 0) {
                arrow.setRemainingFireTicks(100);
            }

            // Critical hit on full draw
            if (power == 1.0f) {
                arrow.setCritArrow(true);
            }

            // Pickup behavior
            if (hasInfiniteArrows) {
                arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            // Shoot with custom velocity
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0f,
                    power * getVelocityMultiplier(), 1.0f);
            level.addFreshEntity(arrow);
        }

        // Play sound
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS,
                1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + power * 0.5f);

        // Damage bow
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));

        // Consume arrow
        if (!hasInfiniteArrows && !isCreative) {
            ammoStack.shrink(1);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
    }

    private static float getPowerForTime(int charge, int maxDrawDuration) {
        float f = (float) charge / maxDrawDuration;
        f = (f * f + f * 2.0f) / 3.0f;
        return Math.min(f, 1.0f);
    }
}