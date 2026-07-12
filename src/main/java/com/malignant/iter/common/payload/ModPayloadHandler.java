package com.malignant.iter.common.payload;

import com.malignant.iter.common.registry.ModCapabilities;
import com.malignant.iter.common.variables.IterPlayerData;
import com.malignant.iter.common.world.gui.GnawerFunction;
import com.malignant.iter.common.world.gui.SpellweaverTableFunction;
import com.malignant.iter.common.world.gui.VoidMawFunction;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ModPayloadHandler {

    public static void handleBurnout(BurnoutPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setEtherBurnout(payload.burnout());
                }
            }
        });
    }

    public static void handleSpellSlot(SpellSlotPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSelectedSpellSlot(payload.slot());
                }
            }
        });
    }

    public static void handleSpellBook(SpellBookPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSelectedSpellBook(payload.spellBook());
                }
            }
        });
    }

    public static void handleSpellLuck(SpellLuckPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setSpellLuck(payload.luck());
                }
            }
        });
    }

    public static void handleGnawerButton(GnawerButtonPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && !player.level().isClientSide) {
                GnawerFunction.function(player);
            }
        });
    }

    public static void handleVoidMawButton(VoidMawButtonPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && !player.level().isClientSide) {
                VoidMawFunction.function(player);
            }
        });
    }

    public static void handleSpellweaverExecute(SpellweaverTableExecutePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && !player.level().isClientSide) {
                SpellweaverTableFunction.execute(player);
            }
        });
    }

    public static void handleSpellweaverSwitch(SpellweaverTableSwitchPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && !player.level().isClientSide) {
                SpellweaverTableFunction.flipswitch(player);
            }
        });
    }

    public static void handleFlying(FlyingPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setFlying(payload.flying());
                }
            }
        });
    }

    public static void handleFlightTime(FlightTimePayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null) {
                IterPlayerData data = ModCapabilities.getMageData(player);
                if (data != null) {
                    data.setFlightTime(payload.flightTime());
                }
            }
        });
    }
}