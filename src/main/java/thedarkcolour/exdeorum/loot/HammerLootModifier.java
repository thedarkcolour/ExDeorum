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

package thedarkcolour.exdeorum.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import thedarkcolour.exdeorum.recipe.RecipeUtil;

import javax.annotation.Nonnull;

public class HammerLootModifier extends LootModifier {
    public static final Codec<HammerLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, HammerLootModifier::new));

    protected HammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (state != null) {
            var itemForm = state.getBlock().asItem();
            if (itemForm != Items.AIR) {
                var recipe = RecipeUtil.getHammerRecipe(itemForm);

                if (recipe != null) {
                    ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
                    var resultAmount = recipe.resultAmount.getInt(context);

                    // fortune handling; more likely to boost drops if there are none to begin with
                    if (context.hasParam(LootContextParams.TOOL)) {
                        var hammer = context.getParam(LootContextParams.TOOL);
                        resultAmount += calculateFortuneBonus(hammer, context.getRandom(), resultAmount == 0);
                    }

                    if (resultAmount > 0) {
                        newLoot.add(new ItemStack(recipe.result, resultAmount));
                    }
                    return newLoot;
                }
            }
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    /**
     * Calculates the bonus number of drops for a hammer enchanted with fortune.
     * @param hammer The hammer in question
     * @param rand RNG
     * @param zeroBaseDrops Whether there were no drops to begin with
     * @return The additional number of drops, to be added to the number of base drops
     */
    public static int calculateFortuneBonus(ItemStack hammer, RandomSource rand, boolean zeroBaseDrops) {
        var fortune = hammer.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

        if (fortune != 0) {
            var chance = rand.nextFloat();

            if (zeroBaseDrops) {
                if (chance < 0.06f * fortune) {
                    return 1;
                }
            } else {
                if (chance < 0.03f * fortune) {
                    return 1;
                }
            }
        }

        return 0;
    }
}

