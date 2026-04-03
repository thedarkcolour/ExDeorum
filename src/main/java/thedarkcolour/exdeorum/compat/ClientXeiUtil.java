package thedarkcolour.exdeorum.compat;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.data.TranslationKeys;

// TODO: port ClientXeiUtil to MC 26.x:
//   - GuiGraphics → GuiGraphicsExtractor
//   - BakedModel/ItemRenderer removed; use new item rendering API
//   - BlockRenderDispatcher/renderSingleBlock removed
//   - ItemBlockRenderTypes.getRenderLayer removed; use FluidStateModelSet/FluidModel
//   - RenderTypeHelper removed
//   - ModelData moved package
//   - OAK_BARREL_COMPOSTING removed from ClientHandler (ModelResourceLocation removed)
// client-only logic shared between JEI and EMI
public class ClientXeiUtil {
    public static void renderBlock(GuiGraphicsExtractor guiGraphics, BlockState state, float x, float y, float z, float scale) {
        // TODO: port block/fluid rendering using 26.x FluidRenderer and block model APIs
    }

    public static void renderItemWithAsterisk(GuiGraphicsExtractor graphics, net.minecraft.world.item.ItemStack stack) {
        // TODO: port item rendering using 26.x item model API
    }

    public static void renderAsterisk(GuiGraphicsExtractor graphics, int xOffset, int yOffset) {
        var font = net.minecraft.client.Minecraft.getInstance().font;
        // 0xff5555 is Minecraft's red text color.
        graphics.text(font, "*", xOffset + 19 - 2 - font.width("*"), yOffset + 12, 0xff5555);
    }

    public static void renderFilledCompostBarrel(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        // TODO: port using 26.x item model API (ModelResourceLocation/OAK_BARREL_COMPOSTING removed)
    }

    // Takes a decimal probability and returns a user-friendly percentage value
    public static Component formatChance(double probability) {
        var chance = XeiUtil.FORMATTER.format(probability * 100);
        return Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY);
    }
}
