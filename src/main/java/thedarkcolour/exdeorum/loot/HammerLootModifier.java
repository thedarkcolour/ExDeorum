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

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.tag.EItemTags;

public class HammerLootModifier extends LootModifier {
    public static final MapCodec<HammerLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, HammerLootModifier::new));

    private final TagKey<Item> fortuneBlacklistTag;

    public HammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);

        this.fortuneBlacklistTag = EItemTags.HAMMER_FORTUNE_BLACKLIST;
    }

    protected HammerLootModifier(LootItemCondition[] conditionsIn, TagKey<Item> fortuneBlacklistTag) {
        super(conditionsIn);

        this.fortuneBlacklistTag =  fortuneBlacklistTag;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (state == null) {
            return generatedLoot;
        }

        var itemForm = state.getBlock().asItem();
        if (itemForm == Items.AIR) {
            return generatedLoot;
        }

        var holder = getRecipe(itemForm, context);
        if (holder == null) {
            return generatedLoot;
        }
        var recipe = holder.value();

        ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
        var resultAmount = recipe.resultAmount.getInt(context);

        if (!itemForm.builtInRegistryHolder().is(this.fortuneBlacklistTag) && context.hasParam(LootContextParams.TOOL)) {
            var hammer = context.getParam(LootContextParams.TOOL);
            // fortune handling; more likely to boost drops if there are none to begin with
            resultAmount += calculateFortuneBonus(context.getLevel().registryAccess(), hammer, context.getRandom(), resultAmount == 0);
        }

        if (resultAmount > 0) {
            newLoot.add(recipe.result.copyWithCount(resultAmount));
        }

        return newLoot;
    }

    @Nullable
    protected RecipeHolder<? extends HammerRecipe> getRecipe(Item itemForm, LootContext context) {
        return RecipeUtil.getCaches(context.getLevel()).getHammerRecipe(itemForm);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    /**
     * Calculates the bonus number of drops for a hammer enchanted with fortune.
     *
     * @param registryAccess The registry access used for looking up enchantments
     * @param hammer         The hammer in question
     * @param rand           RNG
     * @param zeroBaseDrops  Whether there were no drops to begin with
     * @return The additional number of drops, to be added to the number of base drops
     */
    public static int calculateFortuneBonus(RegistryAccess registryAccess, ItemStack hammer, RandomSource rand, boolean zeroBaseDrops) {
        var fortune = hammer.getEnchantmentLevel(registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE));

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

