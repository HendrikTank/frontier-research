package com.example.frontierresearch.client.screen;

import com.example.frontierresearch.FrontierResearch;
import com.example.frontierresearch.menu.BurnerLabMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Client-side screen for the Burner Lab.
 *
 * <p>Renders the burner lab GUI with fuel slot, 5 science-pack input slots,
 * a fuel progress flame, and the player inventory.  The texture is expected
 * at {@code assets/frontierresearch/textures/gui/burner_lab.png}.</p>
 */
public class BurnerLabScreen extends AbstractContainerScreen<BurnerLabMenu> {

    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(FrontierResearch.MODID, "textures/gui/burner_lab.png");

    /** Position and size of the fuel-flame progress indicator. */
    private static final int FLAME_X = 14;
    private static final int FLAME_Y = 36;
    private static final int FLAME_W = 14;
    private static final int FLAME_H = 14;

    public BurnerLabScreen(BurnerLabMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Draw fuel progress flame (texture UV at the right of the main GUI sheet)
        float fuelProgress = menu.getFuelProgress();
        if (fuelProgress > 0) {
            int flameHeight = (int) (FLAME_H * fuelProgress);
            graphics.blit(TEXTURE, x + FLAME_X, y + FLAME_Y + (FLAME_H - flameHeight),
                    176, FLAME_H - flameHeight,
                    FLAME_W, flameHeight);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
