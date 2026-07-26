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

package thedarkcolour.exdeorum.data;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.advancements.Advancement.Builder.advancement;
import static net.minecraft.advancements.triggers.InventoryChangeTrigger.TriggerInstance.hasItems;
import static net.minecraft.advancements.predicates.ItemPredicate.Builder.item;

class Advancements extends AdvancementProvider {
    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new CoreAchievements()));
    }

    private static Identifier modLoc(String path) {
        return Identifier.fromNamespaceAndPath(ExDeorum.ID, path);
    }

    public static class CoreAchievements implements AdvancementSubProvider {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver) {
            var root = advancement()
                    .display(
                            Blocks.OAK_SAPLING,
                            Component.translatable(TranslationKeys.ROOT_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.ROOT_ADVANCEMENT_DESCRIPTION),
                            modLoc("textures/gui/advancements/backgrounds/void.png"),
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    // hardcoded to EventHandler
                    .addCriterion("in_void_world", CriteriaTriggers.IMPOSSIBLE.createCriterion(new ImpossibleTrigger.TriggerInstance()))
                    .save(saver, modLoc("core/root"));
            var crook = advancement()
                    .parent(root)
                    .display(
                            EItems.CROOK.get(),
                            Component.translatable(TranslationKeys.CROOK_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.CROOK_ADVANCEMENT_DESCRIPTION),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            true
                    )
                    .addCriterion("craft_crook", hasItems(item().of(registries.lookupOrThrow(Registries.ITEM), EItemTags.CROOKS).build()))
                    .save(saver, modLoc("core/crook"));
            var barrel = advancement()
                    .parent(root)
                    .display(
                            DefaultMaterials.OAK_BARREL.getItem(),
                            Component.translatable(TranslationKeys.BARREL_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.BARREL_ADVANCEMENT_DESCRIPTION),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            true
                    )
                    .addCriterion("has_barrel", hasItems(item().of(registries.lookupOrThrow(Registries.ITEM), EItemTags.BARRELS).build()))
                    .save(saver, modLoc("core/barrel"));
            var silkWorm = advancement()
                    .parent(crook)
                    .display(
                            EItems.SILKWORM.get(),
                            Component.translatable(TranslationKeys.SILK_WORM_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.SILK_WORM_ADVANCEMENT_DESCRIPTION),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_silk_worm", hasItems(item().of(registries.lookupOrThrow(Registries.ITEM), EItems.SILKWORM.get()).build()))
                    .save(saver, modLoc("core/silk_worm"));
            var stringMesh = advancement()
                    .parent(silkWorm)
                    .display(
                            EItems.STRING_MESH.get(),
                            Component.translatable(TranslationKeys.STRING_MESH_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.STRING_MESH_ADVANCEMENT_DESCRIPTION),
                            null,
                            AdvancementType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_string_mesh", hasItems(item().of(registries.lookupOrThrow(Registries.ITEM), EItems.STRING_MESH.get()).build()))
                    .save(saver, modLoc("core/string_mesh"));
        }
    }
}
