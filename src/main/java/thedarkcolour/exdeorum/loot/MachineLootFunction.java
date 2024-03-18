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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import thedarkcolour.exdeorum.registry.ELootFunctions;

import java.util.List;

public class MachineLootFunction extends LootItemConditionalFunction {
    public static final Codec<MachineLootFunction> CODEC = RecordCodecBuilder.create(instance -> commonFields(instance).apply(instance, MachineLootFunction::new));

    protected MachineLootFunction(List<LootItemCondition> conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext ctx) {
        BlockEntity blockEntity = ctx.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        if (blockEntity != null) {
            blockEntity.saveToItem(stack);
        }

        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return ELootFunctions.MACHINE.get();
    }

    public static LootItemConditionalFunction.Builder<?> machineLoot() {
        return LootItemConditionalFunction.simpleBuilder(MachineLootFunction::new);
    }
}
