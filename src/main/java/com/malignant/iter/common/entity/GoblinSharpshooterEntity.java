package com.malignant.iter.common.entity;

import com.malignant.iter.common.entity.projectile.*;
import com.malignant.iter.common.misc.StrafeMovementGoal;
import com.malignant.iter.common.registry.ModEntities;
import com.malignant.iter.common.registry.ModItems;
import com.malignant.iter.common.registry.ModSounds;
import com.malignant.iter.common.registry.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GoblinSharpshooterEntity extends Monster {

    public GoblinSharpshooterEntity(EntityType<GoblinSharpshooterEntity> type, Level world) {
        super(type, world);
    }

    private static final EntityDataAccessor<Integer> SHOT_COOLDOWN = SynchedEntityData.defineId(GoblinSharpshooterEntity.class, EntityDataSerializers.INT);

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.22);
        builder = builder.add(Attributes.MAX_HEALTH, 12);
        builder = builder.add(Attributes.ARMOR, 4);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        return builder;
    }

    public int getShotCooldown(){
        return this.entityData.get(SHOT_COOLDOWN);
    }
    public void setShotCooldown(int ticks){
        this.entityData.set(SHOT_COOLDOWN, ticks);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new StrafeMovementGoal<>(this, 0.25f, 20));

        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                LivingEntity attacker = this.mob.getLastAttacker();
                if (attacker != null) {
                    if (attacker.getType().is(ModTags.EntityTypes.GOBLINS)) {
                        return false;
                    }
                }
                return super.canUse();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager .class, true, false));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.GOBLIN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return ModSounds.GOBLIN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.GOBLIN_DEATH.get();
    }


    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);

        ItemStack weapon = new ItemStack(ModItems.GOBSTEEL_RIFLE.get());

        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        this.armorDropChances[EquipmentSlot.MAINHAND.getIndex()] = 0.125F;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(world, difficulty, reason, spawnData);
        this.populateDefaultEquipmentSlots(this.random, difficulty);
        return spawnData;
    }

    private void shoot(){
        LivingEntity target = this.getTarget();
        Vec3 shootvec = this.getLookAngle();

        this.setShotCooldown(Mth.nextInt(random, 50, 80));

        if (target != null){
            shootvec = new Vec3(target.getX(), target.getY() + target.getBbHeight()*0.75f, target.getZ())
                    .subtract(this.getX(), this.getEyeY(), this.getZ());
            shootvec = shootvec.normalize();
        }

        for (int i = 0; i < 2; i++) {
            float damage = 2;
            GoblinBullet projectile = new GoblinBullet(this.level(), this, damage);

            projectile.setPos(this.getX(), this.getEyeY(), this.getZ());

            float velocity = 2.5f;
            float inaccuracy = 5f;
            projectile.shoot(shootvec.x, shootvec.y, shootvec.z, velocity, inaccuracy);

            this.level().addFreshEntity(projectile);
        }

        float power = 0.25f;

        Vec3 motion = new Vec3((this.getDeltaMovement().x() + this.getLookAngle().x * -power),
                this.getDeltaMovement().y() + 0.15 + (this.getLookAngle().y * -power)*0.5f,
                this.getDeltaMovement().z() + (this.getLookAngle().z * -power));

        this.setDeltaMovement(motion);
        this.hurtMarked = true;

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                ModSounds.RIFLE_SHOOT.get(), SoundSource.HOSTILE, 1F, 1.0F);
    }

    public void aiStep() {

        if (this.isAlive()) {
            LivingEntity target = this.getTarget();

            if (target != null && target.isAlive() && this.getShotCooldown() <= 0) {

                boolean canSee = this.getSensing().hasLineOfSight(target);

                if (canSee) {
                    shoot();
                }
            }
        }

        if (this.getShotCooldown() > 0){
            this.setShotCooldown(this.getShotCooldown() - 1);
        }

        super.aiStep();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SHOT_COOLDOWN, 20);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setShotCooldown(compound.getInt("ShotCooldown"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("ShotCooldown", this.getShotCooldown());
    }
}
