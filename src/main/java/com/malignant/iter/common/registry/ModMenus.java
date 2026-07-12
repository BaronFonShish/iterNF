package com.malignant.iter.common.registry;

import com.malignant.iter.IterMod;
import com.malignant.iter.common.world.gui.GnawerGuiMenu;
import com.malignant.iter.common.world.gui.SpellBookGuiMenu;
import com.malignant.iter.common.world.gui.SpellweaverTableGuiMenu;
import com.malignant.iter.common.world.gui.VoidMawGuiMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> REGISTRY =
            DeferredRegister.create(Registries.MENU, IterMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SpellBookGuiMenu>> SPELLBOOK_GUI =
            REGISTRY.register("spellbook_gui",
                    () -> IMenuTypeExtension.create((IContainerFactory<SpellBookGuiMenu>) (windowId, inv, data) -> {

                        int handId = 0;
                        if (data != null && data.readableBytes() > 0) {
                            handId = data.readByte();
                        }
                        return new SpellBookGuiMenu(windowId, inv, handId,
                                inv.player.getItemInHand(handId == 0 ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));
                    })
            );

    public static final DeferredHolder<MenuType<?>, MenuType<GnawerGuiMenu>> GNAWER_GUI =
            REGISTRY.register("gnawer_gui",
                    () -> IMenuTypeExtension.create((id, inv, data) -> {
                        return new GnawerGuiMenu(id, inv, data);
                    })
            );

    public static final DeferredHolder<MenuType<?>, MenuType<VoidMawGuiMenu>> VOID_MAW_GUI =
            REGISTRY.register("void_maw_gui", () -> IMenuTypeExtension.create(VoidMawGuiMenu::new));

    public static final DeferredHolder<MenuType<?>, MenuType<SpellweaverTableGuiMenu>> SPELLWEAVER_TABLE_GUI =
            REGISTRY.register("spellweaver_table_gui", () -> IMenuTypeExtension.create(SpellweaverTableGuiMenu::new));

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}