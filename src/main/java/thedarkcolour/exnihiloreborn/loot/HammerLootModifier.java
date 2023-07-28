package thedarkcolour.exnihiloreborn.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import thedarkcolour.exnihiloreborn.recipe.RewardRecipe;

import javax.annotation.Nonnull;

public class HammerLootModifier extends LootModifier {
    public static final Codec<HammerLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).and(ForgeRegistries.RECIPE_TYPES.getCodec().fieldOf("reward_recipe_type").forGetter(modifier -> modifier.type)).apply(inst, HammerLootModifier::new));
    private final RecipeType<? extends RewardRecipe> type;

    @SuppressWarnings("unchecked")
    protected HammerLootModifier(LootItemCondition[] conditionsIn, RecipeType<?> type) {
        super(conditionsIn);
        this.type = (RecipeType<? extends RewardRecipe>) type;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var level = context.getLevel();
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (state != null && state.getBlock().asItem() != Items.AIR) {
            var temporaryItem = new SimpleContainer(new ItemStack(state.getBlock().asItem()));
            var recipe = level.getRecipeManager().getRecipeFor(type, temporaryItem, level);

            if (recipe.isPresent()) {
                var rand = level.random;
                ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();

                for (var reward : recipe.get().getRewards()) {
                    if (rand.nextFloat() < reward.getChance()) {
                        newLoot.add(reward.getItem().copy());
                    }
                }

                return newLoot;
            }
        }

        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}

