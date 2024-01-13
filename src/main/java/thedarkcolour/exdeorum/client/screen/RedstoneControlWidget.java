/*
 * Ex Deorum
 * Copyright (c) 2024 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.client.screen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import thedarkcolour.exdeorum.data.TranslationKeys;

import java.util.List;

public class RedstoneControlWidget implements GuiEventListener, NarratableEntry, Renderable {
    public static final int REDSTONE_MODE_IGNORED = 0;
    public static final int REDSTONE_MODE_UNPOWERED = 1;
    public static final int REDSTONE_MODE_POWERED = 2;

    private static final Component[] REDSTONE_MODES = new Component[] {
            Component.translatable(TranslationKeys.REDSTONE_CONTROL_MODES[REDSTONE_MODE_IGNORED]).withStyle(ChatFormatting.YELLOW),
            Component.translatable(TranslationKeys.REDSTONE_CONTROL_MODES[REDSTONE_MODE_UNPOWERED]).withStyle(ChatFormatting.GRAY),
            Component.translatable(TranslationKeys.REDSTONE_CONTROL_MODES[REDSTONE_MODE_POWERED]).withStyle(ChatFormatting.WHITE),
    };
    private static final Component REDSTONE_CONTROL_LABEL = Component.translatable(TranslationKeys.REDSTONE_CONTROL_LABEL);

    private final MechanicalSieveScreen screen;
    private final ResourceLocation texture;
    private final int posX;
    private final int posY;
    private final int tabU;
    private final int tabV;
    private final int tabWidth;
    private final int tabHeight;
    private final int expandedV;
    private final int expandedU;
    private final int expandedWidth;
    private final int expandedHeight;
    private final int buttonsPosX;
    private final int buttonsPosY;

    // Percentage of the widget currently being displayed
    private float percentage = 0.0f;
    // Whether full widget is displayed
    private boolean expanded = false;
    // Last time (from currentTimeMillis) this button was clicked, used in animation lerp
    private long lastClicked = -1L;

    public RedstoneControlWidget(MechanicalSieveScreen screen, ResourceLocation texture, int posX, int posY) {
        this.screen = screen;
        this.texture = texture;
        this.posX = posX;
        this.posY = posY;

        this.expandedU = 0;
        this.expandedV = 189;
        this.expandedWidth = 94;
        this.expandedHeight = 67;
        this.tabU = this.expandedWidth;
        this.tabV = 189;
        this.tabWidth = 26;
        this.tabHeight = 28;
        this.buttonsPosX = this.posX + 18;
        this.buttonsPosY = this.posY + 44;
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float pPartialTick) {
        if (this.lastClicked != -1L) {
            // animation is 200 ms
            this.percentage = (System.currentTimeMillis() - this.lastClicked) / 200.0f;

            if (this.percentage >= 1.0f) {
                this.percentage = 0.0f;
                this.expanded = !this.expanded;
                this.lastClicked = -1L;
            } else {
                drawPartialConfig(graphics);
                return;
            }
        }

        // Otherwise, render the full widget or the tab only
        var font = Minecraft.getInstance().font;

        if (this.expanded) {
            var redstoneMode = this.screen.getMenu().sieve.getRedstoneMode();
            graphics.blit(this.texture, this.posX, this.posY, this.expandedU, this.expandedV, this.expandedWidth, this.expandedHeight);
            for (int i = 0; i < 3; ++i) {
                graphics.blit(this.texture, this.buttonsPosX + (i * 19), this.buttonsPosY, (redstoneMode == i ? this.tabU + 16 : this.tabU), this.tabV + this.tabHeight, 16, 16);
            }
            graphics.blit(this.texture, this.buttonsPosX, this.buttonsPosY, this.tabU, this.tabV + this.tabHeight + 16, 52, 14);

            graphics.drawString(font, Component.translatable(TranslationKeys.REDSTONE_CONTROL_LABEL), this.posX + 16, this.posY + 10, 0xffffff);
            // The label
            graphics.drawString(font, Component.translatable(TranslationKeys.REDSTONE_CONTROL_MODE).append(REDSTONE_MODES[redstoneMode]), this.posX + 4, this.posY + 26, 0xffffff);
        } else {
            graphics.blit(this.texture, this.posX, this.posY, this.tabU, this.tabV, this.tabWidth, this.tabHeight);

            if (this.posX <= mx && mx < this.posX + this.tabWidth && this.posY <= my && my < this.posY + this.tabHeight) {
                graphics.renderTooltip(font, REDSTONE_CONTROL_LABEL, mx, my);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // relative xy
        int mx = (int) mouseX;
        int my = (int) mouseY;
        var rx = mx - this.posX;
        var ry = my - this.posY;

        float percentage = this.expanded ? 1.0f - this.percentage : this.percentage;

        if (0 <= rx && rx < getWidth(percentage) && 0 <= ry && ry < getHeight(percentage)) {
            if (this.expanded) {
                if (this.buttonsPosY <= my && my < this.buttonsPosY + 16) {
                    for (int i = 0; i < 3; ++i) {
                        int buttonStartX = this.buttonsPosX + (i * 19);

                        if (buttonStartX <= mx && mx < buttonStartX + 16) {
                            setRedstoneMode(i);
                            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.05f));
                            return true;
                        }
                    }
                }
            }
            if (this.lastClicked == -1L && rx < this.tabWidth && ry < this.tabHeight) {
                this.lastClicked = System.currentTimeMillis();
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
            }
        }
        return false;
    }

    private void setRedstoneMode(int redstoneMode) {
        this.screen.getMenu().clickMenuButton(Minecraft.getInstance().player, redstoneMode);
        Minecraft.getInstance().gameMode.handleInventoryButtonClick(this.screen.getMenu().containerId, redstoneMode);
    }

    private void drawPartialConfig(GuiGraphics graphics) {
        float percentage = this.expanded ? 1.0f - this.percentage : this.percentage;
        // top left without edge
        int width = getWidth(percentage) - 3;
        int height = getHeight(percentage) - 3;
        // edge
        int edgeU = this.expandedU + this.expandedWidth - 3;
        int edgeV = this.expandedV + this.expandedHeight - 3;

        // top left section (no edges)
        graphics.blit(this.texture, this.posX, this.posY, this.expandedU, this.expandedV, width, height);
        // bottom edge
        graphics.blit(this.texture, this.posX, this.posY + height, this.expandedU, edgeV, width, 3);
        // right edge
        graphics.blit(this.texture, this.posX + width, this.posY, edgeU, this.expandedV, 3, height);
        // bottom right corner
        graphics.blit(this.texture, this.posX + width, this.posY + height, edgeU, edgeV, 3, 3);
    }

    public int getWidth(float percentage) {
        return this.tabWidth + Math.round((this.expandedWidth - this.tabWidth) * percentage);
    }

    public int getHeight(float percentage) {
        return this.tabHeight + Math.round((this.expandedHeight - this.tabHeight) * percentage);
    }

    @Override
    public void setFocused(boolean pFocused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput narration) {
    }

    public List<Rect2i> getJeiBounds() {
        float percentage = this.expanded ? 1.0f - this.percentage : this.percentage;
        return List.of(new Rect2i(this.posX, this.posY, getWidth(percentage), getHeight(percentage)));
    }
}
