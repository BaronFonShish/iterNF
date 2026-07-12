package com.malignant.iter.common.payload;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.registry.ModCapabilities;
import com.malignant.iter.common.variables.IterPlayerData;
import com.malignant.iter.common.world.gui.GnawerFunction;
import com.malignant.iter.common.world.gui.SpellweaverTableFunction;
import com.malignant.iter.common.world.gui.VoidMawFunction;
import net.minecraft.world.entity.player.Player;

public class PayloadRegistry {

    public static void registerPayloads(final net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
        final net.neoforged.neoforge.network.registration.PayloadRegistrar registrar = event.registrar(IterMod.MOD_ID).versioned("1.0.0");;

        registrar.playBidirectional(BurnoutPayload.TYPE, BurnoutPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setEtherBurnout(payload.burnout());
                }
            });
        });

        registrar.playBidirectional(SpellBookPayload.TYPE, SpellBookPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSelectedSpellBook(payload.spellBook());
                }
            });
        });

        registrar.playBidirectional(SpellSlotPayload.TYPE, SpellSlotPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSelectedSpellSlot(payload.slot());
                }
            });
        });

        registrar.playBidirectional(SpellLuckPayload.TYPE, SpellLuckPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSpellLuck(payload.luck());
                }
            });
        });

        registrar.playBidirectional(SpellweaverTableSwitchPayload.TYPE, SpellweaverTableSwitchPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && !player.level().isClientSide) {
                    SpellweaverTableFunction.flipswitch(player);
                }
            });
        });

        registrar.playBidirectional(SpellweaverTableExecutePayload.TYPE, SpellweaverTableExecutePayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null && !player.level().isClientSide) {
                    SpellweaverTableFunction.execute(player);
                }
            });
        });

        registrar.playBidirectional(SpellweaverSwitchSyncPayload.TYPE, SpellweaverSwitchSyncPayload.STREAM_CODEC, (payload, context) -> {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    IterPlayerData data = ModCapabilities.getMageData(player);
                    if (data != null) {
                        data.setSpellweaverSwitch(payload.state());
                    }
                }
            });
        });

        registrar.playToServer(GnawerButtonPayload.TYPE, GnawerButtonPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    if (player != null && !player.level().isClientSide) {
                        GnawerFunction.function(player);
                    }
                })
        );

        registrar.playToServer(VoidMawButtonPayload.TYPE, VoidMawButtonPayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    if (player != null && !player.level().isClientSide) {
                        VoidMawFunction.function(player);
                    }
                })
        );

    }
}
