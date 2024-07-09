package thedarkcolour.exdeorum.data;

import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.loot.CompressedHammerLootModifier;
import thedarkcolour.exdeorum.loot.CrookLootModifier;
import thedarkcolour.exdeorum.loot.HammerLootModifier;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

class LootModifiers extends GlobalLootModifierProvider {
    LootModifiers(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, ExDeorum.ID);
    }

    @Override
    protected void start() {
        add("hammer", HammerLootModifier::new, EItemTags.HAMMERS);
        add("compressed_hammer", CompressedHammerLootModifier::new, EItemTags.COMPRESSED_HAMMERS);
        add("crook", CrookLootModifier::new, EItemTags.CROOKS);
    }

    private void add(String name, Function<LootItemCondition[], IGlobalLootModifier> constructor, TagKey<Item> requiredTag) {
        add(name, constructor.apply(new LootItemCondition[]{new MatchTool(Optional.of(ItemPredicate.Builder.item().of(requiredTag).build()))}), List.of());
    }
}
