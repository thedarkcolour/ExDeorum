package thedarkcolour.exnihiloreborn.loot;

import com.google.gson.JsonObject;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;
import thedarkcolour.exnihiloreborn.recipe.Reward;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class HammerLootModifier extends LootModifier {
    private final IRecipeType<? extends RewardRecipe> type;

    protected HammerLootModifier(ILootCondition[] conditionsIn, IRecipeType<? extends RewardRecipe> type) {
        super(conditionsIn);
        this.type = type;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        ServerWorld level = context.getLevel();
        Inventory temporaryItem = new Inventory(1); // wrap IInventory
        BlockState state = context.getParamOrNull(LootParameters.BLOCK_STATE);

        if (state.getBlock().asItem() != Items.AIR) {
            temporaryItem.setItem(0, new ItemStack(state.getBlock().asItem()));
            Optional<? extends RewardRecipe> recipe = level.getRecipeManager().getRecipeFor(type, temporaryItem, level);

            if (recipe.isPresent()) {
                Random rand = level.random;
                ArrayList<ItemStack> newLoot = new ArrayList<>();

                for (Reward reward : recipe.get().getRewards()) {
                    if (rand.nextFloat() < reward.getChance()) {
                        newLoot.add(reward.getItem().copy());
                    }
                }

                return newLoot;
            }
        }

        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<HammerLootModifier> {
        private final IRecipeType<? extends RewardRecipe> type;

        public Serializer(IRecipeType<? extends RewardRecipe> type) {
            this.type = type;
        }

        @Override
        public HammerLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            return new HammerLootModifier(conditions, type);
        }

        @Override
        public JsonObject write(HammerLootModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
