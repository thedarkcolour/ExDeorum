package thedarkcolour.exdeorum.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.tag.EItemTags;

public class CompressedHammerLootModifier extends HammerLootModifier {
    public static final MapCodec<CompressedHammerLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, CompressedHammerLootModifier::new));

    public CompressedHammerLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn, EItemTags.COMPRESSED_HAMMER_FORTUNE_BLACKLIST);
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected @Nullable HammerRecipe getRecipe(Item itemForm, LootContext context) {
        return RecipeUtil.getCaches(context.getLevel()).getCompressedHammerRecipe(itemForm);
    }
}
