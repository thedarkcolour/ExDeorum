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
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.List;

public class MachineLootFunction extends LootItemConditionalFunction {
    public static final MapCodec<MachineLootFunction> CODEC = RecordCodecBuilder.mapCodec(instance -> commonFields(instance).apply(instance, MachineLootFunction::new));

    protected MachineLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        BlockEntity blockEntity = ctx.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity != null && stack.getItem() instanceof BlockItem) {
            var output = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, ctx.getLevel().registryAccess());
            blockEntity.saveCustomOnly(output);
            BlockItem.setBlockEntityData(stack, blockEntity.getType(), output);
        }

        return stack;
    }

    @Override
    public MapCodec<MachineLootFunction> codec() {
        return CODEC;
    }

    public static LootItemConditionalFunction.Builder<?> machineLoot() {
        return LootItemConditionalFunction.simpleBuilder(MachineLootFunction::new);
    }
}
