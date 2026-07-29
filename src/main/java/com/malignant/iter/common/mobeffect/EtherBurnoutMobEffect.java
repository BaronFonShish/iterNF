package com.malignant.iter.common.mobeffect;

import com.malignant.iter.common.registry.ModAttributes;
import com.malignant.iter.common.registry.ModDamageTypes;
import it.unimi.dsi.fastutil.ints.Int2DoubleFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public class EtherBurnoutMobEffect extends MobEffect {
    private static final ResourceLocation EFFECT_ID_SPELLPOWER = ResourceLocation.parse("iter.effect.ether_burnout_penalty_spellpower");
    private static final ResourceLocation EFFECT_ID_CASTTIME = ResourceLocation.parse("iter.effect.ether_burnout_penalty_casttime");


    public EtherBurnoutMobEffect(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(
                ModAttributes.SPELL_POWER.getDelegate(),
                EFFECT_ID_SPELLPOWER,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> {
                    double multiplier = Math.pow(0.8, amplifier+1);
                    double penalty = 1.0 - multiplier;
                    return -penalty;
                }
        );
        this.addAttributeModifier(
                ModAttributes.CASTING_SPEED.getDelegate(),
                EFFECT_ID_CASTTIME,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL,
                amplifier -> {
                    double multiplier = Math.pow(0.8, amplifier+1);
                    double penalty = 1.0 - multiplier;
                    return -penalty;
                }
        );

    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (amplifier >=2) {
            DamageSource damageSource = new DamageSource(
                    entity.level().registryAccess()
                            .registryOrThrow(Registries.DAMAGE_TYPE)
                            .getHolderOrThrow(ModDamageTypes.SPELL));
            entity.hurt(damageSource, (amplifier + 1) * 0.5f);
        }
        return true;
    }

    public boolean shouldApplyEffectTickThisTick(int duration, int multiplier) {
        int i = 80;
        return i > 0 ? duration % i == 0 : true;
    }
}
