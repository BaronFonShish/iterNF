package com.malignant.iter.common.item.firearms.guns;

import com.malignant.iter.common.entity.projectile.AbstractBullet;
import com.malignant.iter.common.item.firearms.ammo.*;
import com.malignant.iter.common.misc.Pictograms;
import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.registry.ModDataComponents;
import com.malignant.iter.common.registry.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
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
import net.neoforged.neoforge.common.extensions.IItemExtension;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGun extends Item implements IItemExtension {

    private final float basedamage;
    private final float velocity;
    private final float spread;
    private final int magsize;
    private final int reloadtime;
    private final float firerate;

    public AbstractGun(Properties properties, float basedamage, float velocity, float spread,
                       int magsize, int reloadtime, float firerate) {
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
        return UseAnim.NONE;
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

    @Override
    public void appendHoverText(ItemStack itemstack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);
        List<ItemStack> magazine = getMagazine(itemstack);
        MutableComponent ammoDisplay = Component.literal("");

        for (int i = 0; i < this.magsize; i++) {
            if (i < magazine.size() && !magazine.get(i).isEmpty()) {
                ItemStack ammoStack = magazine.get(i);
                if (ammoStack.getItem() instanceof AbstractAmmo ammo) {
                    char pictogram = getAmmoPictogram(ammo);
                    ammoDisplay.append(Pictograms.getIcon(pictogram));
                }
            } else {
                ammoDisplay.append(Pictograms.getIcon(Pictograms.AT_BLANK));
            }
        }

        list.add(ammoDisplay);
    }

    private char getAmmoPictogram(AbstractAmmo ammo) {
        if (ammo instanceof FlintRound) {
            return Pictograms.AT_FLINT;
        } else if (ammo instanceof GoblinRound) {
            return Pictograms.AT_GOBLIN;
        } else if (ammo instanceof IronRound) {
            return Pictograms.AT_IRON;
        } else if (ammo instanceof SeedshotRound) {
            return Pictograms.AT_SEED;
        }
        return Pictograms.AT_BLANK;
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
            ItemStack ammo = findAmmo(player);
            if (ammo.isEmpty()) break;

            magazine.add(ammo.copyWithCount(1));

            if (!player.isCreative()) {
                ammo.shrink(1);
            }
        }

        reloadFinish(player.level(), player, stack);

        setMagazine(stack, magazine);
        setReloaded(stack, 1);
        player.getCooldowns().addCooldown(this, 5);
    }

    public boolean isValidAmmo(ItemStack ammo) {
        return ammo.getItem() instanceof AbstractAmmo;
    }

    public boolean hasAmmo(Player player, ItemStack gun) {
        if (!HasMag()) return false;
        if (!isMagazineFull(gun)) {
            for (ItemStack item : player.getInventory().items) {
                if (isValidAmmo(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public ItemStack findAmmo(Player player) {
        if (!HasMag()) return ItemStack.EMPTY;
        for (ItemStack item : player.getInventory().items) {
            if (isValidAmmo(item)) {
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
        totalDamage = totalDamage/round.getProjectiles();

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

        shootEffects(level, player, gun, round);

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

    public void shootEffects(Level level, Player entity, ItemStack gun, Item round){
    }

    public void reloadStart(Level level, Player player, ItemStack gun){
        forceAnimation(gun, 2, level.getGameTime(), Math.max(1, getReloadtime(gun, player)-1));
    }

    public void reloadFinish(Level level, Player player, ItemStack gun){
        level.playSound(player, player.getX(), player.getY(), player.getZ(),
                ModSounds.RELOAD_GUN.get(), SoundSource.PLAYERS, 1F, 1.F);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack stack, int remainingTicks) {

        if (getAmmoCount(stack) == magsize) return;

        if (entity instanceof Player player) {

            int useTime = this.getUseDuration(stack, entity) - remainingTicks;
            if (!this.hasAmmo(player, stack)) {
                return;
            }

            if (useTime >= this.getReloadtime(stack, player)) {
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
            fire(level, player, stack);
            return InteractionResultHolder.consume(stack);
        }

        if ((getAmmoCount(stack) == magsize) || !hasAmmo(player, stack)){
            return InteractionResultHolder.fail(stack);
        }

        reloadStart(level, player, stack);
        player.startUsingItem(hand);
        return InteractionResultHolder.pass(stack);
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }
}