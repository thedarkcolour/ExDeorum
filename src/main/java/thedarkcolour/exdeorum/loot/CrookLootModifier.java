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
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.crook.CrookRecipe;

import java.util.List;

public class CrookLootModifier extends LootModifier {
    public static final Codec<CrookLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, CrookLootModifier::new));

    protected CrookLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        var stack = context.getParamOrNull(LootContextParams.TOOL);

        if (state != null && stack != null) {
            var rand = context.getRandom();

            if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) == 0) {
                var fortune = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                var rolls = Math.max(1, Mth.ceil(fortune / 3f));

                for (CrookRecipe recipe : RecipeUtil.getCrookRecipes(state)) {
                    for (int i = 0; i < rolls; i++) {
                        if (rand.nextFloat() < recipe.chance()) {
                            generatedLoot.add(new ItemStack(recipe.result()));
                        }
                    }
                }

                // crook gives an additional roll for leaf drops
                if (state.is(BlockTags.LEAVES)) {
                    // this must not be a crook in order to avoid recursively triggering CrookLootModifier from the re roll method
                    // copying the tag is required so that enchantments like fortune are preserved
                    var nonCrook = new ItemStack(Items.BARRIER);
                    nonCrook.setTag(stack.getTag());

                    for (int i = 0; i < rolls; i++) {
                        generatedLoot.addAll(reRollDrops(context, nonCrook, state));
                    }
                }
            }
        }

        return generatedLoot;
    }

    private static List<ItemStack> reRollDrops(LootContext context, ItemStack nonCrook, BlockState state) {
        var builder = new LootParams.Builder(context.getLevel());
        builder.withParameter(LootContextParams.BLOCK_STATE, context.getParam(LootContextParams.BLOCK_STATE));
        builder.withParameter(LootContextParams.TOOL, nonCrook);

        if (context.hasParam(LootContextParams.THIS_ENTITY)) {
            builder.withParameter(LootContextParams.THIS_ENTITY, context.getParam(LootContextParams.THIS_ENTITY));
        }
        if (context.hasParam(LootContextParams.ORIGIN)) {
            builder.withParameter(LootContextParams.ORIGIN, context.getParam(LootContextParams.ORIGIN));
        }
        return state.getDrops(builder);
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
