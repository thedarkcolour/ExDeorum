package thedarkcolour.exdeorum.compat;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;
import org.joml.Vector3f;
import thedarkcolour.exdeorum.client.ClientHandler;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.registry.EBlocks;

import java.text.DecimalFormat;

// client-only logic shared between JEI and EMI
public class ClientXeiUtil {
    public static final DecimalFormat FORMATTER = new DecimalFormat();
    private static final ItemStack OAK_BARREL = new ItemStack(DefaultMaterials.OAK_BARREL.getItem());

    private static final FluidState EMPTY = Fluids.EMPTY.defaultFluidState();
    private static final BlockState AIR = Blocks.AIR.defaultBlockState();

    // From https://github.com/The-Aether-Team/Nitrogen/blob/1.20.1-develop/src/main/java/com/aetherteam/nitrogen/integration/jei/BlockStateRenderer.java
    private static final Vector3f L1 = new Vector3f(0.4F, 0.0F, 1.0F).normalize();
    private static final Vector3f L2 = new Vector3f(-0.4F, 1.0F, -0.2F).normalize();

    public static void renderItemAlternativeModel(GuiGraphics graphics, BakedModel model, ItemStack stack, int xOffset, int yOffset) {
        Minecraft mc = Minecraft.getInstance();

        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(8 + xOffset, 8 + yOffset, 150);

        try {
            pose.mulPose(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
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
            crashreportcategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
            crashreportcategory.setDetail("Registry Name", () -> BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            throw new ReportedException(crashreport);
        }

        pose.popPose();

        // From end of ItemStackRenderer
        RenderSystem.disableBlend();
    }

    // https://github.com/way2muchnoise/JustEnoughResources/blob/89ee40ff068c8d6eb6ab103f76381445691cffc9/Common/src/main/java/jeresources/util/RenderHelper.java#L100
    public static void renderBlock(GuiGraphics guiGraphics, BlockState state, float x, float y, float z, float scale) {
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

        FluidState fluidState = state.getFluidState();

        if (fluidState.isEmpty()) {
            MultiBufferSource.BufferSource buffers = Minecraft.getInstance().renderBuffers().bufferSource();

            RenderSystem.setupGui3DDiffuseLighting(L1, L2);
            if (state.is(EBlocks.INFESTED_LEAVES.get())) {
                var blockRenderer = Minecraft.getInstance().getBlockRenderer();
                var bakedmodel = blockRenderer.getBlockModel(state);

                for (var renderType : bakedmodel.getRenderTypes(state, RandomSource.create(42), ModelData.EMPTY)) {
                    blockRenderer.getModelRenderer().renderModel(poseStack.last(), buffers.getBuffer(RenderTypeHelper.getEntityRenderType(renderType, false)), state, bakedmodel, 1f, 1f, 1f, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, renderType);
                }
            } else {
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, poseStack, buffers, 15728880, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
            }

            buffers.endBatch();
        } else {
            RenderType renderType = ItemBlockRenderTypes.getRenderLayer(fluidState);
            Matrix4fStack modelView = RenderSystem.getModelViewStack();
            Tesselator tesselator = Tesselator.getInstance();
            renderType.setupRenderState();
            modelView.pushMatrix();
            modelView.mul(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();

            BufferBuilder builder = tesselator.begin(renderType.mode(), renderType.format());

            Dummy.tempState = state;
            Dummy.tempFluid = fluidState;
            Minecraft.getInstance().getBlockRenderer().renderLiquid(BlockPos.ZERO, Dummy.INSTANCE, builder, state, state.getFluidState());
            Dummy.tempFluid = EMPTY;
            Dummy.tempState = AIR;

            MeshData build = builder.build();
            if (build != null) {
                BufferUploader.drawWithShader(build);
            }

            renderType.clearRenderState();
            modelView.popMatrix();
            RenderSystem.applyModelViewMatrix();
        }

        poseStack.popPose();
    }

    public static void renderItemWithAsterisk(GuiGraphics graphics, ItemStack stack) {
        Minecraft mc = Minecraft.getInstance();
        BakedModel model = mc.getItemRenderer().getModel(stack, mc.level, null, 0);
        renderItemAlternativeModel(graphics, model, stack, 0, 0);
        renderAsterisk(graphics, 0, 0);
    }

    public static void renderAsterisk(GuiGraphics graphics, int xOffset, int yOffset) {
        graphics.pose().pushPose();
        graphics.pose().translate(0f, 0f, 200f);

        var font = Minecraft.getInstance().font;
        // 0xff5555 is Minecraft's red text color.
        graphics.drawString(font, "*", xOffset + 19 - 2 - font.width("*"), yOffset + 12, 0xff5555, true);

        graphics.pose().popPose();
    }

    // Takes a decimal probability and returns a user-friendly percentage value
    public static Component formatChance(double probability) {
        var chance = FORMATTER.format(probability * 100);
        return Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY);
    }

    public static void renderFilledCompostBarrel(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        // From mezz.jei.library.render.ItemStackRenderer
        RenderSystem.enableDepthTest();

        Minecraft mc = Minecraft.getInstance();
        var model = mc.getModelManager().getModel(ClientHandler.OAK_BARREL_COMPOSTING);
        // From GuiGraphics.renderFakeItem
        ClientXeiUtil.renderItemAlternativeModel(guiGraphics, model, OAK_BARREL, xOffset, yOffset);
        // From end of DrawableIngredient
        RenderSystem.disableDepthTest();
    }

    public enum Dummy implements BlockAndTintGetter {
        INSTANCE;

        private static BlockState tempState = AIR;
        private static FluidState tempFluid = EMPTY;

        @Override
        public float getShade(Direction pDirection, boolean pShade) {
            return 1;
        }

        @SuppressWarnings("DataFlowIssue")
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
}
