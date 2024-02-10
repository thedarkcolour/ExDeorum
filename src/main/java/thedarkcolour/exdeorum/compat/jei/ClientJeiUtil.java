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

package thedarkcolour.exdeorum.compat.jei;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import me.shedaniel.rei.api.client.view.ViewSearchBuilder;
import me.shedaniel.rei.jeicompat.JEIPluginDetector;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import thedarkcolour.exdeorum.compat.ModIds;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class ClientJeiUtil {
    private static final FluidState EMPTY = Fluids.EMPTY.defaultFluidState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    // From https://github.com/The-Aether-Team/Nitrogen/blob/1.20.1-develop/src/main/java/com/aetherteam/nitrogen/integration/jei/BlockStateRenderer.java
    private static final Vector3f L1 = new Vector3f(0.4F, 0.0F, 1.0F).normalize();
    private static final Vector3f L2 = new Vector3f(-0.4F, 1.0F, -0.2F).normalize();

    // https://github.com/way2muchnoise/JustEnoughResources/blob/89ee40ff068c8d6eb6ab103f76381445691cffc9/Common/src/main/java/jeresources/util/RenderHelper.java#L100
    static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float scale, RenderBlockFn renderFunction) {
        PoseStack poseStack = guiGraphics.pose();

        poseStack.translate(x, y, z);
        poseStack.scale(-scale, -scale, -scale);
        poseStack.translate(-0.5F, -0.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30F));
        poseStack.translate(0.5F, 0, -0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(45f));
        poseStack.translate(-0.5F, 0, 0.5F);

        poseStack.pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        poseStack.translate(0, 0, -1);

        FluidState fluidState = block.getFluidState();

        if (fluidState.isEmpty()) {
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

            RenderSystem.setupGui3DDiffuseLighting(L1, L2);
            renderFunction.renderBlock(block, poseStack, buffers);

            buffers.endBatch();
        } else {
            RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluidState);
            PoseStack modelView = RenderSystem.getModelViewStack();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            renderType.setupRenderState();
            modelView.pushPose();
            modelView.mulPoseMatrix(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();

            builder.begin(renderType.mode(), renderType.format());

            Dummy.tempState = block;
            Dummy.tempFluid = fluidState;
            Minecraft.getInstance().getBlockRenderer().renderLiquid(BlockPos.ZERO, Dummy.INSTANCE, builder, block, block.getFluidState());
            Dummy.tempFluid = EMPTY;
            Dummy.tempState = AIR;

            if (builder.building()) {
                tesselator.end();
            }

            renderType.clearRenderState();
            modelView.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        poseStack.popPose();
    }

    static void renderItemWithAsterisk(GuiGraphics graphics, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getItemRenderer().getModel(stack, mc.level, null, 0);
        renderItemAlternativeModel(graphics, model, stack, 0, 0);
        renderAsterisk(graphics, 0, 0);
    }

    static void renderAsterisk(GuiGraphics graphics, int xOffset, int yOffset) {
        graphics.pose().pushPose();
        graphics.pose().translate(0f, 0f, 200f);

        var font = Minecraft.getInstance().font;
        // 0xff5555 is Minecraft's red text color.
        graphics.drawString(font, "*", xOffset + 19 - 2 - font.width("*"), yOffset + 12, 0xff5555, true);

        graphics.pose().popPose();
    }

    static void renderItemAlternativeModel(GuiGraphics graphics, BakedModel model, ItemStack stack, int xOffset, int yOffset) {
        Minecraft mc = Minecraft.getInstance();

        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(8 + xOffset, 8 + yOffset, 150);

        try {
            pose.mulPoseMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
            pose.scale(16f, 16f, 16f);
            boolean flag = !model.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }

            mc.getItemRenderer().render(stack, ItemDisplayContext.GUI, false, pose, graphics.bufferSource(), 0xf000f0, OverlayTexture.NO_OVERLAY, model);
            graphics.flush();
            if (flag) {
                Lighting.setupFor3DItems();
            }
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering item");
            CrashReportCategory crashreportcategory = crashreport.addCategory("Item being rendered");
            crashreportcategory.setDetail("Item Type", () -> {
                return String.valueOf(stack.getItem());
            });
            crashreportcategory.setDetail("Registry Name", () -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            throw new ReportedException(crashreport);
        }

        pose.popPose();

        // From end of ItemStackRenderer
        RenderSystem.disableBlend();
    }

    // Required due to broken JEI implementation in REI plugin compatibility
    public static <T> void checkTypedIngredient(IIngredientManager manager, IIngredientType<T> ingredientType, T uncheckedIngredient, Consumer<ITypedIngredient<T>> action) {
        if ((uncheckedIngredient instanceof ItemStack stack && !stack.isEmpty()) || (uncheckedIngredient instanceof FluidStack fluidStack && !fluidStack.isEmpty())) {
            manager.createTypedIngredient(ingredientType, uncheckedIngredient).ifPresent(action);
        }
    }

    public static <T> void showRecipes(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().screen instanceof IRecipesGui recipesGui) {
            recipesGui.show(focusFactory.createFocus(RecipeIngredientRole.OUTPUT, ingredient));
        } else if (ModList.get().isLoaded(ModIds.REI_PC)) {
            ViewSearchBuilder.builder().addRecipesFor(JEIPluginDetector.unwrapStack(ingredient)).open();
        }
    }

    public static <T> void showUsages(IFocusFactory focusFactory, ITypedIngredient<T> ingredient) {
        if (Minecraft.getInstance().screen instanceof IRecipesGui recipesGui) {
            // input + catalyst
            recipesGui.show(List.of(focusFactory.createFocus(RecipeIngredientRole.INPUT, ingredient), focusFactory.createFocus(RecipeIngredientRole.CATALYST, ingredient)));
        } else if (ModList.get().isLoaded(ModIds.REI_PC)) {
            ViewSearchBuilder.builder().addUsagesFor(JEIPluginDetector.unwrapStack(ingredient)).open();
        }
    }

    @FunctionalInterface
    interface RenderBlockFn {
        void renderBlock(BlockState block, PoseStack poseStack, MultiBufferSource.BufferSource buffers);
    }

    private enum Dummy implements BlockAndTintGetter {
        INSTANCE;

        private static BlockState tempState = AIR;
        private static FluidState tempFluid = EMPTY;

        @Override
        public float getShade(Direction pDirection, boolean pShade) {
            return 1;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return Minecraft.getInstance().level.getLightEngine();
        }

        @Override
        public int getBlockTint(BlockPos pBlockPos, ColorResolver pColorResolver) {
            return 0;
        }

        @Override
        public int getBrightness(LightLayer pLightType, BlockPos pBlockPos) {
            return 15;
        }

        @Override
        public int getRawBrightness(BlockPos pBlockPos, int pAmount) {
            return 15;
        }

        @Nullable
        @Override
        public BlockEntity getBlockEntity(BlockPos pPos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return pos.equals(BlockPos.ZERO) ? tempState : AIR;
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return pos.equals(BlockPos.ZERO) ? tempFluid : EMPTY;
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getMinBuildHeight() {
            return 0;
        }
    }

    enum AsteriskItemRenderer implements IIngredientRenderer<ItemStack> {
        INSTANCE;

        @Override
        public void render(GuiGraphics graphics, ItemStack ingredient) {
            // From mezz.jei.library.render.ItemStackRenderer
            RenderSystem.enableDepthTest();
            ClientJeiUtil.renderItemWithAsterisk(graphics, ingredient);
            // From end of DrawableIngredient
            RenderSystem.disableDepthTest();
        }

        @Override
        public List<Component> getTooltip(ItemStack ingredient, TooltipFlag tooltipFlag) {
            // Copied from ItemStackRenderer
            Minecraft minecraft = Minecraft.getInstance();
            Player player = minecraft.player;
            try {
                return ingredient.getTooltipLines(player, tooltipFlag);
            } catch (RuntimeException | LinkageError e) {
                List<Component> list = new ArrayList<>();
                MutableComponent crash = Component.translatable("jei.tooltip.error.crash");
                list.add(crash.withStyle(ChatFormatting.RED));
                return list;
            }
        }
    }
}
