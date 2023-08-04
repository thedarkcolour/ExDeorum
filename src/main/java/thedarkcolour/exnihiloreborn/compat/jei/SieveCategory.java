package thedarkcolour.exnihiloreborn.compat.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.util.Lazy;
import thedarkcolour.exnihiloreborn.data.TranslationKeys;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.registry.EBlocks;

import java.text.DecimalFormat;

class SieveCategory implements IRecipeCategory<JeiSieveRecipeGroup> {
    private static final DecimalFormat FORMATTER = new DecimalFormat();
    public static final int WIDTH = 162;
    public static final int ROW_START = 28;

    static {
        FORMATTER.setMinimumFractionDigits(0);
        FORMATTER.setMaximumFractionDigits(3);
    }

    private final Lazy<IDrawable> background;
    private final IDrawable slot;
    private final IDrawable row;
    private final IDrawable icon;
    private final Component title;

    public SieveCategory(IGuiHelper helper) {
        this.background = Lazy.of(() -> helper.createBlankDrawable(WIDTH, ROW_START + 18 * JeiSieveRecipeGroup.maxSieveRows));
        this.slot = helper.getSlotDrawable();
        this.row = helper.createDrawable(ExNihiloRebornJeiPlugin.ENH_JEI_TEXTURE, 0, 0, 162, 18);
        this.icon = helper.createDrawableItemStack(new ItemStack(EBlocks.OAK_SIEVE.get()));
        this.title = Component.translatable(TranslationKeys.SIEVE_CATEGORY_TITLE);
    }

    @Override
    public RecipeType<JeiSieveRecipeGroup> getRecipeType() {
        return ExNihiloRebornJeiPlugin.SIEVE;
    }

    @Override
    public Component getTitle() {
        return this.title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background.get();
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, JeiSieveRecipeGroup recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 59, 1).addIngredients(recipe.ingredient());
        builder.addSlot(RecipeIngredientRole.CATALYST, 87, 1).addItemStack(recipe.mesh());

        for (int i = 0; i < recipe.results().size(); i++) {
            var result = recipe.results().get(i);
            var slot = builder.addSlot(RecipeIngredientRole.OUTPUT, 1 + (i % 9) * 18, 1 + ROW_START + 18 * (i / 9)).addItemStack(result.item);

            addTooltips(slot, result.provider);
        }
    }

    public static void addTooltips(IRecipeSlotBuilder slot, NumberProvider provider) {
        if (provider instanceof BinomialDistributionGenerator binomial) {
            if (binomial.n instanceof ConstantValue constant && constant.value == 1) {
                var chance = FORMATTER.format(RecipeUtil.getExpectedValue(binomial.p) * 100);
                slot.addTooltipCallback((slotView, tooltip) -> tooltip.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY)));
            } else {
                addAvgOutput(slot, RecipeUtil.getExpectedValue(provider));
            }

            addMinMaxes(slot, ConstantValue.exactly(0), binomial.n);
        } else if (provider.getClass() != ConstantValue.class) {
            var val = RecipeUtil.getExpectedValue(provider);
            if (val != -1.0) {
                addAvgOutput(slot, val);

                if (provider instanceof UniformGenerator uniform) {
                    addMinMaxes(slot, uniform.min, uniform.max);
                }
            }
        }
    }

    private static void addAvgOutput(IRecipeSlotBuilder slot, double avgValue) {
        String avgOutput = FORMATTER.format(avgValue);
        slot.addTooltipCallback((slotView, tooltip) -> tooltip.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_AVERAGE_OUTPUT, avgOutput).withStyle(ChatFormatting.GRAY)));
    }

    // when the player holds shift, they can see the min/max amounts of a drop
    private static void addMinMaxes(IRecipeSlotBuilder slot, NumberProvider min, NumberProvider max) {
        var minFormatted = FORMATTER.format(RecipeUtil.getExpectedValue(min));
        var maxFormatted = FORMATTER.format(RecipeUtil.getExpectedValue(max));

        slot.addTooltipCallback((slotView, tooltip) -> {
            if (Screen.hasShiftDown()) {
                tooltip.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_MIN_OUTPUT, minFormatted).withStyle(ChatFormatting.GRAY));
                tooltip.add(Component.translatable(TranslationKeys.SIEVE_RECIPE_MAX_OUTPUT, maxFormatted).withStyle(ChatFormatting.GRAY));
            }
        });
    }

    @Override
    public void draw(JeiSieveRecipeGroup recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        slot.draw(graphics, 58, 0);
        slot.draw(graphics, 86, 0);

        for (int i = 0; i < JeiSieveRecipeGroup.maxSieveRows; i++) {
            row.draw(graphics, 0, 28 + i * 18);
        }
    }
}
