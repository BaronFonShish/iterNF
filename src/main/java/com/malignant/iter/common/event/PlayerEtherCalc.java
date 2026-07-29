package com.malignant.iter.common.event;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.registry.ModEffects;
import com.malignant.iter.common.variables.IterPlayerDataUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = IterMod.MOD_ID)
public class PlayerEtherCalc {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            IterPlayerDataUtils.syncOnLogin(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            IterPlayerDataUtils.syncOnDimensionChange(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer serverPlayer) {
            IterPlayerDataUtils.syncOnRespawn(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer newPlayer) {
            if (event.getOriginal() instanceof ServerPlayer oldPlayer) {
                IterPlayerDataUtils.syncOnClone(newPlayer, oldPlayer);
            }
        }
    }

    private static final Holder<MobEffect> EFFECT_HOLDER = ModEffects.ETHER_BURNOUT;

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {

            float dissipation = IterPlayerDataUtils.getDynamicDissipation(player);
            float burnout = IterPlayerDataUtils.getBurnout(player);

            if (burnout > 0) {
                if (dissipation >= burnout) {
                    IterPlayerDataUtils.setBurnout(player, 0);
                } else {
                    IterPlayerDataUtils.addBurnout(player, -dissipation);
                }
            }

            if (burnout < 0) {
                IterPlayerDataUtils.setBurnout(player, 0);
            }

            if (player.level().getGameTime() % 20 == 0) {
                int threshold_1 = (int) IterPlayerDataUtils.getThreshold(player);
                int threshold_2 = (int) (threshold_1 * 1.05f + 3);
                int threshold_3 = (int) (threshold_2 * 1.025f + 2);

                GameType gameMode = player.gameMode.getGameModeForPlayer();
                if (gameMode == GameType.SURVIVAL || gameMode == GameType.ADVENTURE) {
                    float currentBurnout = IterPlayerDataUtils.getBurnout(player);


                    if (currentBurnout >= threshold_3) {
                        if (!player.hasEffect(Holder.direct(ModEffects.ETHER_BURNOUT.get()))) {
                            player.addEffect(new MobEffectInstance(EFFECT_HOLDER, 40, 2, false, true));
                        }
                    } else
                    if (currentBurnout >= threshold_2) {
                        if (!player.hasEffect(Holder.direct(ModEffects.ETHER_BURNOUT.get()))){
                            player.addEffect(new MobEffectInstance(EFFECT_HOLDER, 40, 1, false, true));
                        }
                    } else
                    if (currentBurnout >= threshold_1) {
                        if (!player.hasEffect(Holder.direct(ModEffects.ETHER_BURNOUT.get()))) {
                            player.addEffect(new MobEffectInstance(EFFECT_HOLDER, 40, 0, false, true));
                        }
                    }
                }
            }
        }
    }
}