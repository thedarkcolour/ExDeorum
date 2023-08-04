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
import thedarkcolour.exdeorum.registry.ELootFunctions;

// Sets the correct amount based on the progress of the infested leaves
public class InfestedStringCount extends LootItemConditionalFunction {
    // todo move to config
    public static final float STRING_CHANCE = 0.4f;

    protected InfestedStringCount(LootItemCondition[] conditions) {
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

                if (rand.nextFloat() < progress * STRING_CHANCE) {
                    if (rand.nextFloat() < progress * STRING_CHANCE / 4f) {
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
        return LootItemConditionalFunction.simpleBuilder(InfestedStringCount::new);
    }

    public static class LootSerializer extends LootItemConditionalFunction.Serializer<InfestedStringCount> {
        @Override
        public InfestedStringCount deserialize(JsonObject json, JsonDeserializationContext ctx, LootItemCondition[] conditions) {
            return new InfestedStringCount(conditions);
        }
    }
}
