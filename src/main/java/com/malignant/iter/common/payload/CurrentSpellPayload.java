package com.malignant.iter.common.payload;
import com.malignant.iter.IterMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public record CurrentSpellPayload(ItemStack spellStack) implements CustomPacketPayload {
    public static final Type<CurrentSpellPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(IterMod.MOD_ID, "current_spell"));

    public static final StreamCodec<FriendlyByteBuf, CurrentSpellPayload> STREAM_CODEC =
            StreamCodec.ofMember(CurrentSpellPayload::write, CurrentSpellPayload::new);

    private CurrentSpellPayload(FriendlyByteBuf buf) {
        this(ItemStack.OPTIONAL_STREAM_CODEC.decode((RegistryFriendlyByteBuf) buf));
    }

    private void write(FriendlyByteBuf buf) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode((RegistryFriendlyByteBuf) buf, spellStack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}