package com.malignant.iter.common.payload;

import com.malignant.iter.IterMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpellweaverTableSwitchPayload(BlockPos pos, boolean state) implements CustomPacketPayload {
    public static final Type<SpellweaverTableSwitchPayload> TYPE = new Type<>(ResourceLocation.parse(IterMod.MOD_ID + ":spellweaver_switch"));
    public static final StreamCodec<ByteBuf, SpellweaverTableSwitchPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SpellweaverTableSwitchPayload::pos,
            ByteBufCodecs.BOOL, SpellweaverTableSwitchPayload::state,
            SpellweaverTableSwitchPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}