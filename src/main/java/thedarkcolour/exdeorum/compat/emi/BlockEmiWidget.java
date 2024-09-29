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

package thedarkcolour.exdeorum.compat.emi;

import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.Bounds;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.Widget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.compat.XeiUtil;

import java.util.List;
import java.util.function.Predicate;

public class BlockEmiWidget extends Widget {
    private final List<BlockState> states;
    private final List<Component> extraDetails;
    private final int x;
    private final int y;
    private final float scale;
    private final Bounds bounds;

    public BlockEmiWidget(List<BlockState> states, List<Component> extraDetails, int x, int y, float scale) {
        this.states = states;
        this.extraDetails = extraDetails;
        this.x = x;
        this.y = y;
        this.scale = scale;

        int size = (int) (1.5f * scale);
        this.bounds = new Bounds(x - size / 2, y - size / 4, size, size);
    }

    @Override
    public Bounds getBounds() {
        return this.bounds;
    }

    @Override
    public void render(GuiGraphics draw, int mouseX, int mouseY, float delta) {
        draw.pose().pushPose();

        int index = (int) (System.currentTimeMillis() / 1000 % this.states.size());
        BlockState current = this.states.get(index);

        ClientXeiUtil.renderBlock(draw, current, this.x, this.y, 0, this.scale);

        draw.pose().popPose();
    }

    @Override
    public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
        int index = (int) (System.currentTimeMillis() / 1000 % this.states.size());
        BlockState current = this.states.get(index);
        List<Component> tooltip = XeiUtil.getBlockTooltip(this.extraDetails, current.getBlock());

        return tooltip.stream()
                .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                .toList();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        return slotCall(this.states, widget -> widget.mouseClicked(1, 1, button));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return slotCall(this.states, widget -> widget.keyPressed(keyCode, scanCode, modifiers));
    }

    // Hack to use internal EMI behavior of switching to different recipe view
    private static boolean slotCall(List<BlockState> states, Predicate<SlotWidget> call) {
        int index = (int) (System.currentTimeMillis() / 1000 % states.size());
        BlockState current = states.get(index);
        EmiStack stack = EmiStack.of(current.getBlock());

        if (stack.isEmpty() && !current.getFluidState().isEmpty()) {
            stack = EmiUtil.fluid(current.getFluidState().getType(), 1000);
        }

        if (stack.isEmpty()) {
            return false;
        } else {
            SlotWidget internal = new SlotWidget(stack, 0, 0);
            return call.test(internal);
        }
    }
}
