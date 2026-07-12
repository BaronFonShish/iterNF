package com.malignant.iter.common.payload;

import com.malignant.iter.IterMod;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record GnawerButtonPayload(BlockPos pos) implements CustomPacketPayload {
    public static final Type<GnawerButtonPayload> TYPE = new Type<>(ResourceLocation.parse(IterMod.MOD_ID + ":gnawer_button"));
    public static final StreamCodec<RegistryFriendlyByteBuf, GnawerButtonPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, GnawerButtonPayload::pos,
            GnawerButtonPayload::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}