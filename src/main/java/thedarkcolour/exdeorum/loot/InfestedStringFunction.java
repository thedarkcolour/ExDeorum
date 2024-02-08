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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import thedarkcolour.exdeorum.block.InfestedLeavesBlock;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.registry.ELootFunctions;

// Sets the correct amount based on the progress of the infested leaves
public class InfestedStringFunction extends LootItemConditionalFunction {
    protected InfestedStringFunction(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        var te = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (state != null && state.getValue(InfestedLeavesBlock.FULLY_INFESTED)) {
            if (te instanceof InfestedLeavesBlockEntity leaves) {
                var progress = leaves.getProgress();
                var rand = context.getRandom();
                var count = 0;
                var chance = EConfig.SERVER.infestedLeavesStringChance.get();

                if (rand.nextFloat() < progress * chance) {
                    if (rand.nextFloat() < progress * chance / 4f) {
                        ++count;
                    }
                    ++count;
                }

                if (count > 0) {
                    stack.setCount(count);
                    return stack;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public LootItemFunctionType getType() {
        return ELootFunctions.INFESTED_STRING.get();
    }

    public static LootItemConditionalFunction.Builder<?> infestedString() {
        return LootItemConditionalFunction.simpleBuilder(InfestedStringFunction::new);
    }

    public static class LootSerializer extends LootItemConditionalFunction.Serializer<InfestedStringFunction> {
        @Override
        public InfestedStringFunction deserialize(JsonObject json, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
            return new InfestedStringFunction(conditions);
        }
    }
}
