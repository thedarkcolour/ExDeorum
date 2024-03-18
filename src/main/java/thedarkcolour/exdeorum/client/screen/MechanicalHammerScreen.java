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

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.menu.MechanicalHammerMenu;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;

public class MechanicalHammerScreen extends AbstractContainerScreen<MechanicalHammerMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ExDeorum.ID, "textures/gui/container/mechanical_hammer.png");

    public static final int RECIPE_CLICK_AREA_POS_X = 80;
    public static final int RECIPE_CLICK_AREA_POS_Y = 34;
    public static final int RECIPE_CLICK_AREA_WIDTH = 23;
    public static final int RECIPE_CLICK_AREA_HEIGHT = 16;

    @Nullable
    private RedstoneControlWidget redstoneControlWidget;

    public MechanicalHammerScreen(MechanicalHammerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();

        this.redstoneControlWidget = new RedstoneControlWidget(this.menu, BACKGROUND_TEXTURE, this.leftPos + this.imageWidth, this.topPos + 3);
        addRenderableWidget(this.redstoneControlWidget);
    }

    @Nullable
    public RedstoneControlWidget getRedstoneControlWidget() {
        return this.redstoneControlWidget;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        int left = this.leftPos;
        int top = this.topPos;
        graphics.blit(BACKGROUND_TEXTURE, left, top, 0, 0, this.imageWidth, this.imageHeight);

        // energy bar
        int energy = Mth.floor(54 * ((float) this.menu.prevEnergy / EConfig.SERVER.mechanicalSieveEnergyStorage.get()));
        graphics.blit(BACKGROUND_TEXTURE, left + 10, top + 15 + 54 - energy, this.imageWidth, 16 + 54 - energy, 12, energy);

        // progress arrow
        int progress = Math.min(23, this.menu.machine.getGuiProgress());
        graphics.blit(BACKGROUND_TEXTURE, left + RECIPE_CLICK_AREA_POS_X, top + RECIPE_CLICK_AREA_POS_Y, this.imageWidth, 0, progress, 16);
    }

    @Override
    public void render(GuiGraphics graphics, int mx, int my, float pPartialTick) {
        renderBackground(graphics, mx, my, pPartialTick);
        super.render(graphics, mx, my, pPartialTick);
        renderTooltip(graphics, mx, my);

        int rx = mx - this.leftPos;
        int ry = my - this.topPos;

        if (9 <= rx && rx < 23 && 14 <= ry && ry < 70) {
            var energyTooltip = Component.translatable(TranslationKeys.ENERGY).append(Component.translatable(TranslationKeys.FRACTION_DISPLAY, this.menu.prevEnergy, EConfig.SERVER.mechanicalSieveEnergyStorage.get())).append(" FE");
            graphics.renderTooltip(Minecraft.getInstance().font, energyTooltip, mx, my);
        }
    }
}
