package com.malignant.iter.common.item;

import com.malignant.iter.common.entity.projectile.HellblazeArrowEntity;
import com.malignant.iter.common.registry.ModEntities;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class HellblazeArrowItem extends ArrowItem {

    public HellblazeArrowItem() {
        super(new Properties().stacksTo(64).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, TooltipContext context, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, context, list, flag);
        list.add(Component.translatable("iter.desc.hellblaze_arrow"));
    }

    @Override
    public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction dir) {
        HellblazeArrowEntity arrow = new HellblazeArrowEntity(ModEntities.HELLBLAZE_ARROW.get(), level);
        arrow.pickup = AbstractArrow.Pickup.ALLOWED;
        return arrow;
    }

    public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
        return new HellblazeArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
    }
}