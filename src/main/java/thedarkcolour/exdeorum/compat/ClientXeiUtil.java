package thedarkcolour.exdeorum.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.client.RenderUtil;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;

// client-only logic shared between JEI and EMI
public class ClientXeiUtil {
    private static final ItemStack OAK_BARREL = new ItemStack(DefaultMaterials.OAK_BARREL.getItem());

    public static void renderBlock(GuiGraphicsExtractor guiGraphics, BlockState state, float x, float y, float z, float scale) {
        var fluidState = state.getFluidState();
        if (!fluidState.isEmpty()) {
            var sprite = RenderUtil.getFluidSprite(fluidState.getType());
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, Math.round(x), Math.round(y), Math.round(scale), Math.round(scale));
            return;
        }

        var stack = new ItemStack(state.getBlock());
        if (!stack.isEmpty()) {
            renderScaledItem(guiGraphics, stack, x, y, scale);
        }
    }

    public static void renderItemWithAsterisk(GuiGraphicsExtractor graphics, ItemStack stack) {
        graphics.fakeItem(stack, 0, 0);
        renderAsterisk(graphics, 0, 0);
    }

    public static void renderAsterisk(GuiGraphicsExtractor graphics, int xOffset, int yOffset) {
        var font = net.minecraft.client.Minecraft.getInstance().font;
        // 0xff5555 is Minecraft's red text color.
        graphics.text(font, "*", xOffset + 19 - 2 - font.width("*"), yOffset + 12, 0xff5555);
    }

    public static void renderFilledCompostBarrel(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        guiGraphics.fakeItem(OAK_BARREL, xOffset, yOffset);
        var sprite = RenderUtil.getBlockSprite(ExDeorum.loc("block/compost_dirt"));
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xOffset + 4, yOffset + 5, 8, 8);
    }

    // Takes a decimal probability and returns a user-friendly percentage value
    public static Component formatChance(double probability) {
        var chance = XeiUtil.FORMATTER.format(probability * 100);
        return Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY);
    }

    private static void renderScaledItem(GuiGraphicsExtractor graphics, ItemStack stack, float x, float y, float scale) {
        var pose = graphics.pose();
        pose.pushMatrix();
        pose.translate(x, y);
        pose.scale(scale / 16f);
        graphics.fakeItem(stack, 0, 0);
        pose.popMatrix();
    }
}
