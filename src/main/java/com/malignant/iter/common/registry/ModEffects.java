package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.mobeffect.EtherBurnoutMobEffect;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, IterMod.MOD_ID);

    public static final DeferredHolder<MobEffect, EtherBurnoutMobEffect> ETHER_BURNOUT =
            EFFECTS.register("ether_burnout",
                    () -> new EtherBurnoutMobEffect(MobEffectCategory.HARMFUL, 0x6a4c94)
            );
}