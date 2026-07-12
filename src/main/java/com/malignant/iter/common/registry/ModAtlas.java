package com.malignant.iter.common.registry;

import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.ResourceLocation;

public class ModAtlas {
    public static final ResourceLocation GUI_ATLAS =
            ResourceLocation.parse("iter:gui_atlas");

    public static void registerAtlases() {
    }


    public static final WidgetSprites SPELLWEAVER_SWITCH_OFF = new WidgetSprites(
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_switch0"),
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_switch0_hover")
    );

    public static final WidgetSprites SPELLWEAVER_SWITCH_ON = new WidgetSprites(
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_switch1.png"),
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_switch1_hover.png")
    );

    public static final WidgetSprites SPELLWEAVER_WRITE = new WidgetSprites(
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_write.png"),
            ResourceLocation.parse("iter:textures/gui/atlas/spellweaver_table_write_hover.png")
    );
}