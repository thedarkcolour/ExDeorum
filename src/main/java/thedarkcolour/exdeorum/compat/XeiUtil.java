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

package thedarkcolour.exdeorum.compat;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.loot.SummationGenerator;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.recipe.CodecUtil;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

// common logic shared between JEI, EMI, and REI (boo REI sucks)
public class XeiUtil {
    // One To One (Hammer, Crucible)
    public static final int ONE_TO_ONE_WIDTH = 72;
    public static final int ONE_TO_ONE_HEIGHT = 18;

    // Barrel mixing (Fluid/Item, Fluid/Fluid)
    public static final int BARREL_MIXING_WIDTH = 120;
    public static final int BARREL_MIXING_HEIGHT = 18;

    // Block predicate (Crucible Heat, Sieve)
    public static final Component REQUIRES_CERTAIN_STATE = Component.translatable(TranslationKeys.CROOK_CATEGORY_REQUIRES_STATE).withStyle(ChatFormatting.GRAY);

    // Sieve
    public static final int SIEVE_WIDTH = 162;
    public static final int SIEVE_ROW_START = 28;
    public static final int SIEVE_ROW_HEIGHT = 18;
    public static final Component BY_HAND_ONLY_LABEL = Component.translatable(TranslationKeys.SIEVE_RECIPE_BY_HAND_ONLY).withStyle(ChatFormatting.RED);

    public static final DecimalFormat FORMATTER = new DecimalFormat();

    static {
        FORMATTER.setMinimumFractionDigits(0);
        FORMATTER.setMaximumFractionDigits(3);
    }

    // Takes a decimal probability and returns a user-friendly percentage value
    public static Component formatChance(double probability) {
        var chance = FORMATTER.format(probability * 100);
        return Component.translatable(TranslationKeys.SIEVE_RECIPE_CHANCE, chance).withStyle(ChatFormatting.GRAY);
    }

    public static List<BlockState> getStates(BlockPredicate predicate) {
        if (predicate instanceof BlockPredicate.BlockStatePredicate state) {
            return state.possibleStates()
                    .filter(blockState -> !blockState.hasProperty(BlockStateProperties.WATERLOGGED) || !blockState.getValue(BlockStateProperties.WATERLOGGED))
                    .toList();
        } else if (predicate instanceof BlockPredicate.SingleBlockPredicate block) {
            return ImmutableList.of(block.block().defaultBlockState());
        } else if (predicate instanceof BlockPredicate.TagPredicate tag) {
            var list = new ArrayList<BlockState>();

            for (var holder : BuiltInRegistries.BLOCK.getTagOrEmpty(tag.tag())) {
                if (holder.isBound()) {
                    list.add(holder.value().defaultBlockState());
                }
            }

            return list;
        }

        throw new IllegalArgumentException("Invalid Block Predicate");
    }

    // Copied from mezz.jei.forge.platform.ModHelper and mezz.jei.library.ModIdHelper
    public static Component getModDisplayName(String modId) {
        String string = ModList.get().getModContainerById(modId)
                .map(ModContainer::getModInfo)
                .map(IModInfo::getDisplayName)
                .orElseGet(() -> StringUtils.capitalize(modId));

        String withoutFormattingCodes = ChatFormatting.stripFormatting(string);
        return Component.literal((withoutFormattingCodes == null) ? "" : withoutFormattingCodes).withStyle(style -> style.withItalic(true).withColor(ChatFormatting.BLUE));
    }

    public static List<Component> getBlockTooltip(List<Component> extraDetails, Block block) {
        var modId = BuiltInRegistries.BLOCK.getKey(block).getNamespace();
        var tooltip = new ArrayList<Component>();

        tooltip.add(Component.translatable(block.getDescriptionId()));
        tooltip.addAll(extraDetails);
        tooltip.add(getModDisplayName(modId));

        return tooltip;
    }

    public static ImmutableList<Component> getStateRequirements(BlockPredicate.@Nullable BlockStatePredicate predicate) {
        ImmutableList.Builder<Component> requirements = ImmutableList.builder();
        if (predicate != null) {
            var json = CodecUtil.encode(StatePropertiesPredicate.CODEC, predicate.properties());
            if (json instanceof JsonObject obj) {
                for (var entry : obj.entrySet()) {
                    requirements.add(Component.literal("  " + entry.getKey() + "=" + entry.getValue().toString()).withStyle(ChatFormatting.GRAY));
                }
            }
        }
        return requirements.build();
    }

    public static List<Component> getExtraDetails(BlockPredicate predicate) {
        List<Component> extraDetails;
        if (predicate instanceof BlockPredicate.TagPredicate tag) {
            extraDetails = ImmutableList.of(Component.literal("#" + tag.tag().location()).withStyle(ChatFormatting.GRAY));
        } else if (predicate instanceof BlockPredicate.BlockStatePredicate state) {
            var requirements = getStateRequirements(state);
            extraDetails = new ArrayList<>(requirements.size() + 1);
            extraDetails.add(REQUIRES_CERTAIN_STATE);
            extraDetails.addAll(requirements);
        } else {
            extraDetails = List.of();
        }
        return extraDetails;
    }

    public static void addSieveDropTooltip(boolean byHandOnly, NumberProvider provider, Consumer<Component> tooltipLines) {
        if (byHandOnly) {
            tooltipLines.accept(XeiUtil.BY_HAND_ONLY_LABEL);
        }

        if (provider instanceof BinomialDistributionGenerator binomial) {
            if (binomial.n() instanceof ConstantValue constant && constant.value() == 1) {
                var chanceLabel = XeiUtil.formatChance(RecipeUtil.getExpectedValue(binomial.p()));
                tooltipLines.accept(chanceLabel);
            } else {
                addAvgOutput(tooltipLines, RecipeUtil.getExpectedValue(provider));
            }

            addMinMaxes(tooltipLines, 0, getMax(binomial.n()));
        } else if (provider.getClass() != ConstantValue.class) {
            var val = RecipeUtil.getExpectedValue(provider);
            if (val != -1.0) {
                addAvgOutput(tooltipLines, val);

                if (provider instanceof UniformGenerator || provider instanceof SummationGenerator) {
                    addMinMaxes(tooltipLines, getMin(provider), getMax(provider));
                }
            }
        }
    }

    private static double getMin(NumberProvider provider) {
        if (provider instanceof ConstantValue value) {
            return value.value();
        } else if (provider instanceof UniformGenerator uniform) {
            return getMin(uniform.min());
        } else if (provider instanceof BinomialDistributionGenerator) {
            return 0;
        } else if (provider instanceof SummationGenerator summation) {
            double sum = 0;

            for (var child : summation.providers()) {
                sum += getMin(child);
            }

            return sum;
        }

        return 0;
    }

    private static double getMax(NumberProvider provider) {
        if (provider instanceof ConstantValue value) {
            return value.value();
        } else if (provider instanceof UniformGenerator uniform) {
            return getMax(uniform.max());
        } else if (provider instanceof BinomialDistributionGenerator binomial) {
            return getMax(binomial.n());
        } else if (provider instanceof SummationGenerator summation) {
            double sum = 0;

            for (var child : summation.providers()) {
                sum += getMax(child);
            }

            return sum;
        }

        return 0;
    }

    private static void addAvgOutput(Consumer<Component> tooltipLines, double avgValue) {
        String avgOutput = XeiUtil.FORMATTER.format(avgValue);
        tooltipLines.accept(Component.translatable(TranslationKeys.SIEVE_RECIPE_AVERAGE_OUTPUT, avgOutput).withStyle(ChatFormatting.GRAY));
    }

    // when the player holds shift, they can see the min/max amounts of a drop
    private static void addMinMaxes(Consumer<Component> tooltipLines, double min, double max) {
        String minFormatted = XeiUtil.FORMATTER.format(min);
        String maxFormatted = XeiUtil.FORMATTER.format(max);

        tooltipLines.accept(Component.translatable(TranslationKeys.SIEVE_RECIPE_MIN_OUTPUT, minFormatted).withStyle(ChatFormatting.GRAY));
        tooltipLines.accept(Component.translatable(TranslationKeys.SIEVE_RECIPE_MAX_OUTPUT, maxFormatted).withStyle(ChatFormatting.GRAY));
    }

    public interface HeatRecipeAcceptor {
        void accept(int heat, BlockState state);
    }

    public static void addCrucibleHeatRecipes(HeatRecipeAcceptor acceptor) {
        var values = new Object2IntOpenHashMap<Block>();
        for (var entry : RecipeUtil.getHeatSources()) {
            var state = entry.getKey();
            var block = state.getBlock();

            if (block instanceof WallTorchBlock) continue;

            if (block != Blocks.AIR) {
                final int newValue = entry.getIntValue();

                values.computeInt(block, (key, value) -> {
                    if (value != null) {
                        return Math.max(value, newValue);
                    } else {
                        return newValue == 0 ? null : newValue;
                    }
                });
            }
        }

        for (var entry : values.object2IntEntrySet()) {
            acceptor.accept(entry.getIntValue(), entry.getKey().defaultBlockState());
        }
    }
}
