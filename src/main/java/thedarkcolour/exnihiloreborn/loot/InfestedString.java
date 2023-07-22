package thedarkcolour.exnihiloreborn.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tileentity.TileEntity;
import thedarkcolour.exnihiloreborn.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exnihiloreborn.registry.ELootFunctions;

import java.util.Random;

// Sets the correct amount based on the progress of the infested leaves
public class InfestedString extends LootFunction {
    protected InfestedString(ILootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        TileEntity te = context.getParamOrNull(LootParameters.BLOCK_ENTITY);

        if (te instanceof InfestedLeavesBlockEntity) {
            float progress = ((InfestedLeavesBlockEntity) te).getProgress();
            Random rand = context.getRandom();
            int count = 0;

            if (rand.nextFloat() < progress * 0.4) {
                if (rand.nextFloat() < progress * 0.1) {
                    ++count;
                }
                ++count;
            }

            if (count > 0) {
                stack.setCount(count);
                return stack;
            }
        }

        return ItemStack.EMPTY;
    }

    @Override
    public LootFunctionType getType() {
        return ELootFunctions.INFESTED_STRING;
    }

    public static LootFunction.Builder<?> infestedString() {
        return simpleBuilder(InfestedString::new);
    }

    public static class Serializer extends LootFunction.Serializer<InfestedString> {
        @Override
        public void serialize(JsonObject json, InfestedString p_230424_2_, JsonSerializationContext p_230424_3_) {
            super.serialize(json, p_230424_2_, p_230424_3_);
        }

        @Override
        public InfestedString deserialize(JsonObject json, JsonDeserializationContext ctx, ILootCondition[] conditions) {
            return new InfestedString(conditions);
        }
    }
}
