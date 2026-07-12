package com.malignant.iter.common.entity;

import com.malignant.iter.common.registry.ModSpawnRestrictions;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

public class GhoulEntity extends Monster {

    private static final EntityDataAccessor<Integer> LEAP_CD = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> LEAP_AFTER = SynchedEntityData.defineId(GhoulEntity.class, EntityDataSerializers.INT);

    public GhoulEntity(EntityType<GhoulEntity> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.225f);
        builder = builder.add(Attributes.MAX_HEALTH, 20);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 7);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        return builder;
    }


    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.6f, true) {
        });

        this.goalSelector.addGoal(1, new FleeSunGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager .class, true, false));
    }


    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound(){
        return SoundEvents.ZOMBIE_DEATH;
    }

    public void aiStep() {
        if (this.isAlive()) {
            boolean flag = this.isSunBurnTick();
            if (flag) {
                ItemStack itemstack = this.getItemBySlot(EquipmentSlot.HEAD);
                if (!itemstack.isEmpty()) {
                    if (itemstack.isDamageableItem()) {
                        itemstack.setDamageValue(itemstack.getDamageValue() + this.random.nextInt(2));
                        if (itemstack.getDamageValue() >= itemstack.getMaxDamage()) {
                            this.onEquippedItemBroken(this.getItemBySlot(EquipmentSlot.HEAD).getItem(), EquipmentSlot.HEAD);
                            this.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
                        }
                    }

                    flag = false;
                }

                if (flag) {
                    this.setRemainingFireTicks(8*20);
                }
            }

            if (this.getLeapCooldown() > 0) {
                this.setLeapCooldown(this.getLeapCooldown() - 1);
            }

            if (this.getLeapAfter() > 0) {
                this.setLeapAfter(this.getLeapAfter() - 1);
            }

            if ((!this.level().isClientSide)&&(this.getLeapCooldown() == 0)&&(this.onGround())){
                LivingEntity target = this.getTarget();
                if (target != null && target.isAlive()) {
                    double dist = this.distanceTo(target);
                    if ((dist > 1) && (dist < 6)){
                        this.setLeapCooldown(Mth.nextInt(random, 100, 250));

                        Vec3 leapvec = target.position().subtract(this.position());
                        leapvec = leapvec.normalize();
                        this.jumpFromGround();
                        this.addDeltaMovement(leapvec);
                        this.setLeapAfter(15);
                        this.getNavigation().stop();
                        this.resetFallDistance();
                        this.hasImpulse = true;
                        this.markHurt();
                    }
                }
            }
        }

        super.aiStep();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(LEAP_CD, 0);
        builder.define(LEAP_AFTER, 0);
    }

    public int getLeapCooldown() {
        return this.entityData.get(LEAP_CD);
    }

    public int getAdjustedLeapAfter() {
        int after = this.entityData.get(LEAP_AFTER);
        if (this.entityData.get(LEAP_AFTER) > 10){
            after = 11 - (this.entityData.get(LEAP_AFTER)-10)*2;
        }
        return after;
    }

    public int getLeapAfter() {
        return this.entityData.get(LEAP_AFTER);
    }

    public void setLeapCooldown(int ticks) {
        this.entityData.set(LEAP_CD, Math.max(0, ticks));
    }

    public void setLeapAfter(int ticks) {
        this.entityData.set(LEAP_AFTER, Math.max(0, ticks));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("LeapCd", this.getLeapCooldown());
        compound.putInt("LeapAfter", this.getLeapAfter());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setLeapCooldown(compound.getInt("LeapCd"));
        this.setLeapAfter(compound.getInt("LeapAfter"));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty,
                                        MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        spawnData = super.finalizeSpawn(world, difficulty, reason, spawnData);
        this.populateDefaultEquipmentSlots(this.random, difficulty);
        this.setLeapCooldown(Mth.nextInt(random, 100, 250));
        this.setLeapAfter(0);
        return spawnData;
    }

    public static boolean GhoulSpawnRules(
            EntityType<GhoulEntity> entityType,
            ServerLevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        if (!(level.getLevel().dimension() == Level.OVERWORLD)) {
            return false;
        }

        if (!ModSpawnRestrictions.defaultMonsterCheck(level, pos)) {
            return false;
        }

        return true;
    }
}
