package com.malignant.iter.common.world.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TextureButton extends Button {
    private final ResourceLocation defaultTexture;
    private final ResourceLocation hoverTexture;
    private final int texWidth;
    private final int texHeight;

    public TextureButton(int x, int y, int width, int height,
                         ResourceLocation normalTexture,
                         ResourceLocation hoverTexture,
                         OnPress onPress) {
        this(x, y, width, height, normalTexture, hoverTexture, onPress,
                Component.empty(), width, height);
    }

    public TextureButton(int x, int y, int width, int height,
                         ResourceLocation normalTexture,
                         ResourceLocation hoverTexture,
                         OnPress onPress,
                         Component message,
                         int texWidth, int texHeight) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.defaultTexture = normalTexture;
        this.hoverTexture = hoverTexture;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ResourceLocation texture = this.isHoveredOrFocused() ? hoverTexture : defaultTexture;
        if (hoverTexture == null) texture = defaultTexture;

        guiGraphics.blit(texture, this.getX(), this.getY(), 0, 0,
                this.width, this.height, texWidth, texHeight);
    }
}