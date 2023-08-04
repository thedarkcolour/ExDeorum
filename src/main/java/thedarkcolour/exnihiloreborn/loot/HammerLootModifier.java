package thedarkcolour.exnihiloreborn.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import javax.annotation.Nonnull;

public class HammerLootModifier extends LootModifier {
    public static final Codec<HammerLootModifier> CODEC = RecordCodecBuilder.create(inst -> LootModifier.codecStart(inst).apply(inst, HammerLootModifier::new));

    protected HammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        var level = context.getLevel();
        var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);

        if (state != null && state.getBlock().asItem() != Items.AIR) {
            var temporaryItem = new SimpleContainer(new ItemStack(state.getBlock().asItem()));
            var recipe = level.getRecipeManager().getRecipeFor(ERecipeTypes.HAMMER.get(), temporaryItem, level);

            if (recipe.isPresent()) {
                ObjectArrayList<ItemStack> newLoot = new ObjectArrayList<>();
                var resultAmount = recipe.get().resultAmount.getInt(context);

                if (resultAmount > 0) {
                    newLoot.add(new ItemStack(recipe.get().result, resultAmount));
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

