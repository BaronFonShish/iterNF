package com.malignant.iter.common.entity.projectile;

import com.malignant.iter.common.registry.ModEntities;
import com.malignant.iter.common.registry.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SeedshotBullet extends AbstractBullet{

    public SeedshotBullet(EntityType<? extends AbstractBullet> entityType, Level level) {
        super(entityType, level);
    }

    public SeedshotBullet(Level level, LivingEntity shooter, float baseDamage) {
        super(ModEntities.SEEDSHOT_BULLET.get(), level, shooter, baseDamage);
    }

    public SeedshotBullet(Level level, LivingEntity shooter, Vec3 direction, float velocity, float inaccuracy, float damage) {
        this(level, shooter, damage);
        this.shoot(direction.x, direction.y, direction.z, velocity, inaccuracy);
    }

    public SeedshotBullet(Level level, LivingEntity shooter, float xRot, float yRot, float pitch, float velocity,
                          float inaccuracy, float damage) {
        this(level, shooter, damage);
        this.shootFromRotation(shooter, xRot, yRot, pitch, velocity, inaccuracy);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ModItems.SEEDSHOT_ROUND.get().getDefaultInstance();
    }
}
