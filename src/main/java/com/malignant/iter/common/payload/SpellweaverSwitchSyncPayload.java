package com.malignant.iter.common.payload;

import com.malignant.iter.IterMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpellweaverSwitchSyncPayload(boolean state) implements CustomPacketPayload {
    public static final Type<SpellweaverSwitchSyncPayload> TYPE = new Type<>(ResourceLocation.parse(IterMod.MOD_ID + ":spellweaver_switch_sync"));
    public static final StreamCodec<ByteBuf, SpellweaverSwitchSyncPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, SpellweaverSwitchSyncPayload::state,
            SpellweaverSwitchSyncPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}