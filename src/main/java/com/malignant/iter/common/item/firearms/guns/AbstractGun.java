package com.malignant.iter.common.item.firearms.guns;

import com.malignant.iter.common.item.firearms.ammo.AbstractAmmo;
import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.registry.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class AbstractGun extends Item {

    private final float basedamage;
    private final float velocity;
    private final float spread;
    private final int magsize;
    private final int reloadtime;
    private final int firerate;
    private boolean reloadState = false;
    private int reloadProgress = 0;

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
        return UseAnim.CUSTOM;
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
            magazine = new ArrayList<>(this.magsize);
            setMagazine(stack, magazine);
            return magazine;
        }

        if (magazine.size() > this.magsize) {
            magazine = new ArrayList<>(magazine.subList(0, this.magsize));
            setMagazine(stack, magazine);
        }

        return magazine;
    }

    public void setMagazine(ItemStack stack, List<ItemStack> magazine) {
        stack.set(ModDataComponents.GUN_MAGAZINE.get(), magazine);
    }

    public int getAmmoCount(ItemStack stack) {
        return getMagazine(stack).size();
    }

    public boolean isMagazineFull(ItemStack stack) {
        return getAmmoCount(stack) >= magsize;
    }

    public boolean isMagazineEmpty(ItemStack stack) {
        return getAmmoCount(stack) == 0;
    }

    public int getReloadtime(ItemStack stack, Player player){
        AttributeInstance Reload = player.getAttribute(ModAttributes.RANGED_RELOAD_SPEED);
        float ReloadMod = Reload != null ? (float) Reload.getValue() : 1f;
        return (int) Math.max((int)1, (20*this.reloadtime)/ReloadMod);
    }

    public int getFireTime(ItemStack stack, Player player){
        AttributeInstance FireTime = player.getAttribute(ModAttributes.RANGED_FIRE_RATE);
        float FireTimeMod = FireTime != null ? (float) FireTime.getValue() : 1f;
        return (int) Math.max((int)1, 20/(this.firerate*FireTimeMod));
    }

    public boolean isReloading(ItemStack stack) {
        return stack.getOrDefault(ModDataComponents.RELOAD_PROGRESS, 0) > 0;
    }

    public void startReload(ItemStack stack, Player player) {
        if (isMagazineFull(stack) || isReloading(stack) || !hasAmmo(player, stack)) return;

        stack.set(ModDataComponents.RELOAD_PROGRESS.get(), 0);
    }

    public void abortReload(ItemStack stack) {
        stack.remove(ModDataComponents.RELOAD_PROGRESS.get());
    }

    public void reloadTick(ItemStack stack, Level level, Player player) {
        if (!isReloading(stack)) return;
        if (!HasMag()){
            abortReload(stack);
            return;
        }

        if (player.getMainHandItem() != stack) {
            abortReload(stack);
            return;
        }

        int progress = stack.getOrDefault(ModDataComponents.RELOAD_PROGRESS.get(), 0) + 1;
        stack.set(ModDataComponents.RELOAD_PROGRESS.get(), progress);

        if (progress >= getReloadtime(stack, player)) {
            finishReload(stack, player);
        }
    }

    public void finishReload(ItemStack stack, Player player) {
        if (!HasMag()) return;

        List<ItemStack> magazine = getMagazine(stack);

        for (int i = 0; i < this.magsize; i++) {
            if (getMagazine(stack).get(1) != ItemStack.EMPTY){break;}

            ItemStack ammo = findAmmo(player, stack);
            if (ammo.isEmpty()) break;
            magazine.add(ammo.copyWithCount(1));
            ammo.shrink(1);
        }

        setMagazine(stack, magazine);
        abortReload(stack);
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

    public boolean HasMag(){
        return (this.magsize > 0);
    }

    public boolean isValidAmmo(ItemStack ammo, ItemStack gun) {
        return ammo.getItem() instanceof AbstractAmmo;
    }

    public void tick(ItemStack stack, Level level, Player player) {
        if (isReloading(stack)) {
            reloadTick(stack, level, player);
        }
    }
}