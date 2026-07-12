package com.malignant.iter.common.payload;

import com.malignant.iter.IterMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SpellweaverTableExecutePayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<SpellweaverTableExecutePayload> TYPE = new Type<>(ResourceLocation.parse(IterMod.MOD_ID + ":spellweaver_execute"));
    public static final StreamCodec<ByteBuf, SpellweaverTableExecutePayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SpellweaverTableExecutePayload::pos,
            SpellweaverTableExecutePayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}