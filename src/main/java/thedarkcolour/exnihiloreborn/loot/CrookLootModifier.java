package thedarkcolour.exnihiloreborn.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import thedarkcolour.exnihiloreborn.registry.EItems;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class CrookLootModifier extends LootModifier {
    private static final float[] SILK_WORM_FORTUNE_CHANCES = new float[] { 0.01f, 0.0111111114f, 0.0125f, 0.016666668f, 0.05f };

    protected CrookLootModifier(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        BlockState state = context.getParamOrNull(LootParameters.BLOCK_STATE);

        if (state.is(BlockTags.LEAVES)) {
            Random level = context.getRandom();
            ItemStack stack = context.getParamOrNull(LootParameters.TOOL);

            if (level.nextFloat() < SILK_WORM_FORTUNE_CHANCES[EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, stack)]) {
                generatedLoot.add(new ItemStack(EItems.SILK_WORM.get()));
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<CrookLootModifier> {
        @Override
        public CrookLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new CrookLootModifier(conditions);
        }

        @Override
        public JsonObject write(CrookLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
