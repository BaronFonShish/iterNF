package com.malignant.iter.common.item.firearms.guns;

import com.malignant.iter.common.entity.projectile.AbstractBullet;
import com.malignant.iter.common.item.firearms.ammo.AbstractAmmo;
import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.registry.ModDataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGun extends Item {

    private final float basedamage;
    private final float velocity;
    private final float spread;
    private final int magsize;
    private final int reloadtime;
    private final int firerate;

    public AbstractGun(Properties properties, float basedamage, float velocity, float spread,
                       int magsize, int reloadtime, int firerate) {
        super(properties);
        this.basedamage = basedamage;
        this.velocity = velocity;
        this.spread = spread;
        this.magsize = magsize;
        this.reloadtime = reloadtime;
        this.firerate = firerate;
    }

    @Override
    public int getEnchantmentValue() {
        return 16;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull ItemAttributeModifiers getDefaultAttributeModifiers() {
        ItemAttributeModifiers.Builder builder = ItemAttributeModifiers.builder();

        builder.add(ModAttributes.RANGED_DAMAGE,
                new AttributeModifier(ResourceLocation.parse("iter:ranged_damage"), (this.basedamage + 1) - 1, AttributeModifier.Operation.ADD_VALUE),
                EquipmentSlotGroup.MAINHAND);

        return builder.build();
    }

    public List<ItemStack> getMagazine(ItemStack stack) {
        List<ItemStack> magazine = stack.get(ModDataComponents.GUN_MAGAZINE.get());

        if (magazine == null) {
            magazine = new ArrayList<>();
            setMagazine(stack, magazine);
            return magazine;
        }

        magazine = new ArrayList<>(magazine);

        magazine.removeIf(ItemStack::isEmpty);

        while (magazine.size() > this.magsize) {
            magazine.removeLast();
        }

        setMagazine(stack, magazine);

        return magazine;
    }

    public void setMagazine(ItemStack stack, List<ItemStack> magazine) {
        stack.set(ModDataComponents.GUN_MAGAZINE.get(), magazine);
    }

    public int getAmmoCount(ItemStack stack) {
        List<ItemStack> magazine = getMagazine(stack);
        return magazine.size();
    }

    public boolean isMagazineFull(ItemStack stack) {
        return getAmmoCount(stack) >= magsize;
    }

    public boolean isMagazineEmpty(ItemStack stack) {
        return getAmmoCount(stack) == 0;
    }

    public int getReloadtime(ItemStack stack, Player player) {
        AttributeInstance Reload = player.getAttribute(ModAttributes.RANGED_RELOAD_SPEED);
        float ReloadMod = Reload != null ? (float) Reload.getValue() : 1f;
        return (int) Math.max(1, (this.reloadtime) / ReloadMod);
    }

    public int getFireTime(ItemStack stack, Player player) {
        AttributeInstance FireTime = player.getAttribute(ModAttributes.RANGED_FIRE_RATE);
        float FireTimeMod = FireTime != null ? (float) FireTime.getValue() : 1f;
        return (int) Math.max(1, 20 / (this.firerate * FireTimeMod));
    }

    public int isReloaded(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RELOAD_STATE.get(), 0);
    }

    public void setReloaded(ItemStack stack, int reloadstate) {
        stack.set(ModDataComponents.RELOAD_STATE.get(), reloadstate);
    }

    public void forceAnimation(ItemStack stack, int state, long start, int duration) {
        stack.set(ModDataComponents.ANIMATION_STATE.get(), state);
        stack.set(ModDataComponents.ANIMATION_START_TICK.get(), start);
        stack.set(ModDataComponents.ANIMATION_DURATION.get(), duration);
    }

    public int getAnimationState(ItemStack stack){
        return stack.getOrDefault(ModDataComponents.ANIMATION_STATE.get(), 0);
    }

    public Number getAnimationStart(ItemStack stack){
        return stack.getOrDefault(ModDataComponents.ANIMATION_START_TICK.get(), 0);
    }

    public int getAnimationDuration(ItemStack stack){
        return stack.getOrDefault(ModDataComponents.ANIMATION_DURATION.get(), 0);
    }

    public void finishReload(ItemStack stack, Player player) {
        if (!HasMag()) return;

        List<ItemStack> magazine = getMagazine(stack);

        magazine.removeIf(ItemStack::isEmpty);

        while (magazine.size() < this.magsize) {
            ItemStack ammo = findAmmo(player, stack);
            if (ammo.isEmpty()) break;

            magazine.add(ammo.copyWithCount(1));

            if (!player.isCreative()) {
                ammo.shrink(1);
            }
        }

        setMagazine(stack, magazine);
        setReloaded(stack, 1);
        int cooldownTicks = (int) getFireTime(stack, player);
        player.getCooldowns().addCooldown(this, cooldownTicks);
    }

    public boolean isValidAmmo(ItemStack ammo, ItemStack gun) {
        return ammo.getItem() instanceof AbstractAmmo;
    }

    public boolean hasAmmo(Player player, ItemStack gun) {
        if (!HasMag()) return false;
        if (!isMagazineFull(gun)) {
            for (ItemStack item : player.getInventory().items) {
                if (isValidAmmo(item, gun)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack findAmmo(Player player, ItemStack gun) {
        if (!HasMag()) return ItemStack.EMPTY;
        for (ItemStack item : player.getInventory().items) {
            if (isValidAmmo(item, gun)) {
                return item;
            }
        }
        return ItemStack.EMPTY;
    }

    public boolean HasMag() {
        return (this.magsize > 0);
    }

    public void fire(Level level, Player player, ItemStack gun) {
        List<ItemStack> magazine = getMagazine(gun);

        ItemStack ammoStack = magazine.getFirst();
        if (!(ammoStack.getItem() instanceof AbstractAmmo round)) {
            magazine.removeFirst();
            setMagazine(gun, magazine);
            return;
        }

        AttributeInstance rangedDamage = player.getAttribute(ModAttributes.RANGED_DAMAGE);
        float totalDamage = (float) (rangedDamage.getValue() + round.getBasedamage());

        float totalVelocity = this.velocity + round.getVelocity();
        float totalSpread = this.spread + round.getSpread();


        for (int i = 0; i < round.getProjectiles(); i++) {
            EntityType<? extends Projectile> bulletType = round.getBulletType();
            if (!(bulletType.create(level) instanceof AbstractBullet bullet)) {
                return;
            }

            bullet.setPos(player.getX(), player.getEyeY(), player.getZ());
            bullet.shootWithDamage(player, player.getLookAngle(), totalVelocity, totalSpread, totalDamage);
            level.addFreshEntity(bullet);
        }

        //recoil on fire anim
        forceAnimation(gun, 1, level.getGameTime(), Math.max(1, (getFireTime(gun, player)/2)));

        magazine.removeFirst();
        setMagazine(gun, magazine);
        if (isMagazineEmpty(gun)){
            setReloaded(gun, 0);
        }

        int cooldownTicks = getFireTime(gun, player);
        player.getCooldowns().addCooldown(this, cooldownTicks);

        if (!player.isCreative()) {
            gun.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
        }
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {

        if (getAmmoCount(stack) == magsize) return;

        if (entity instanceof Player player) {

            int useTime = this.getUseDuration(stack, entity) - remainingTicks;
            if (!this.hasAmmo(player, stack)) {
                System.out.println("no ammo");
                return;
            }

            System.out.println("reloading: " + useTime + "/" + this.getReloadtime(stack, player));

            if (useTime >= this.getReloadtime(stack, player)) {
                System.out.println("reloaded");
                this.finishReload(stack, player);
                entity.stopUsingItem();
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (hand != InteractionHand.MAIN_HAND) {
            return InteractionResultHolder.fail(stack);
        }

        if (isReloaded(stack) == 1 && !player.isShiftKeyDown()) {
            System.out.println("gun fired");
            fire(level, player, stack);
            return InteractionResultHolder.consume(stack);
        }

        if ((getAmmoCount(stack) == magsize)){
            return InteractionResultHolder.fail(stack);
        }

        System.out.println("gun used");
        //reload anim
        forceAnimation(stack, 2, level.getGameTime(), Math.max(1, getReloadtime(stack, player)-1));
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(stack);
    }
}