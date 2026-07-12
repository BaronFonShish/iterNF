package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, IterMod.MOD_ID);

    public static final Supplier<DataComponentType<String>> FLAYING_UUID =
            DATA_COMPONENTS.registerComponentType("flaying_uuid",
                    builder -> builder.persistent(Codec.STRING));


    public static final Supplier<DataComponentType<Integer>> FLAYING_STACK =
            DATA_COMPONENTS.registerComponentType("flaying_stack",
                    builder -> builder.persistent(Codec.INT));

    public static final Supplier<DataComponentType<Integer>> SPELL_QUALITY =
            DATA_COMPONENTS.registerComponentType("spell_quality",
                    builder -> builder.persistent(Codec.INT));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<List<ItemStack>>> SPELL_BOOK_SPELLS =
            DATA_COMPONENTS.registerComponentType("spell_book_spells",
                    builder -> builder.persistent(Codec.list(ItemStack.CODEC)));

    public static final Supplier<DataComponentType<List<ItemStack>>> GUN_MAGAZINE =
            DATA_COMPONENTS.registerComponentType("gun_magazine",
                    builder -> builder.persistent(Codec.list(ItemStack.CODEC)));

    public static final Supplier<DataComponentType<Integer>> RELOAD_PROGRESS =
            DATA_COMPONENTS.registerComponentType("reload_progress",
                    builder -> builder.persistent(Codec.INT));

    public static final Supplier<DataComponentType<String>> STAFF_SHAFT =
            DATA_COMPONENTS.registerComponentType("staff_shaft",
                    builder -> builder.persistent(Codec.STRING));
    public static final Supplier<DataComponentType<String>> STAFF_POMMEL =
            DATA_COMPONENTS.registerComponentType("staff_pommel",
                    builder -> builder.persistent(Codec.STRING));
    public static final Supplier<DataComponentType<String>> STAFF_FETISH =
            DATA_COMPONENTS.registerComponentType("staff_fetish",
                    builder -> builder.persistent(Codec.STRING));
    public static final Supplier<DataComponentType<String>> STAFF_CORE =
            DATA_COMPONENTS.registerComponentType("staff_core",
                    builder -> builder.persistent(Codec.STRING));
}
