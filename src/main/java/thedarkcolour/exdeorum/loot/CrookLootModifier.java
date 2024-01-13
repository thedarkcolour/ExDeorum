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
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.exdeorum.registry.EItems;

public class CrookLootModifier extends LootModifier {
    public static final Codec<CrookLootModifier> CODEC = RecordCodecBuilder.create(inst -> {
        return LootModifier.codecStart(inst).apply(inst, CrookLootModifier::new);
    });

    private static final float[] SILK_WORM_FORTUNE_CHANCES = new float[] { 0.01f, 0.0111111114f, 0.0125f, 0.016666668f, 0.05f };

    protected CrookLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        var stack = context.getParamOrNull(LootContextParams.TOOL);

        if (state != null && stack != null && state.is(BlockTags.LEAVES)) {
            var rand = context.getRandom();

            if (stack.getEnchantmentLevel(Enchantments.SILK_TOUCH) == 0) {
                var fortune = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
                var repeats = Math.max(1, Mth.ceil(fortune / 3f));

                if (rand.nextInt(100) == 0) {
                    generatedLoot.add(new ItemStack(EItems.SILK_WORM.get()));
                }

                for (int i = 0; i < repeats; i++) {
                    if (rand.nextFloat() < SILK_WORM_FORTUNE_CHANCES[fortune % 3]) {
                        generatedLoot.add(new ItemStack(EItems.SILK_WORM.get()));
                    }

                    // crook gives an additional roll for drops
                    var builder = new LootParams.Builder(context.getLevel());
                    builder.withParameter(LootContextParams.BLOCK_STATE, context.getParam(LootContextParams.BLOCK_STATE));
                    // avoid recursion
                    var dummy = new ItemStack(Items.DEAD_BUSH);
                    dummy.setTag(stack.getTag());
                    builder.withParameter(LootContextParams.TOOL, dummy);

                    if (context.hasParam(LootContextParams.THIS_ENTITY)) {
                        builder.withParameter(LootContextParams.THIS_ENTITY, context.getParam(LootContextParams.THIS_ENTITY));
                    }
                    if (context.hasParam(LootContextParams.ORIGIN)) {
                        builder.withParameter(LootContextParams.ORIGIN, context.getParam(LootContextParams.ORIGIN));
                    }
                    var reRoll = state.getDrops(builder);
                    generatedLoot.addAll(reRoll);
                }
            }
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
