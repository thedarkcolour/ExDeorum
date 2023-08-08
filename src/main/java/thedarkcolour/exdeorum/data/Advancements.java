/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static net.minecraft.advancements.Advancement.Builder.advancement;
import static net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems;
import static net.minecraft.advancements.critereon.ItemPredicate.Builder.item;

class Advancements extends ForgeAdvancementProvider {
    public Advancements(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new CoreAchievements()));
    }

    private static ResourceLocation modLoc(String path) {
        return new ResourceLocation(ExDeorum.ID, path);
    }

    public static class CoreAchievements implements AdvancementGenerator {
        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper helper) {
            var root = advancement()
                    .display(
                            Blocks.OAK_SAPLING,
                            Component.translatable(TranslationKeys.ROOT_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.ROOT_ADVANCEMENT_DESCRIPTION),
                            modLoc("textures/gui/advancements/backgrounds/void.png"),
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    // hardcoded to EventHandler
                    .addCriterion("in_void_world", new ImpossibleTrigger.TriggerInstance())
                    .save(saver, modLoc("core/root"), helper);
            var crook = advancement()
                    .parent(root)
                    .display(
                            EItems.CROOK.get(),
                            Component.translatable(TranslationKeys.CROOK_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.CROOK_ADVANCEMENT_DESCRIPTION),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            true
                    )
                    .addCriterion("craft_crook", hasItems(item().of(EItemTags.CROOKS).build()))
                    .save(saver, modLoc("core/crook"), helper);
            var barrel = advancement()
                    .parent(root)
                    .display(
                            EItems.OAK_BARREL.get(),
                            Component.translatable(TranslationKeys.BARREL_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.BARREL_ADVANCEMENT_DESCRIPTION),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            true
                    )
                    .addCriterion("has_barrel", hasItems(item().of(EItemTags.BARRELS).build()))
                    .save(saver, modLoc("core/barrel"), helper);
            var silkWorm = advancement()
                    .parent(crook)
                    .display(
                            EItems.SILK_WORM.get(),
                            Component.translatable(TranslationKeys.SILK_WORM_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.SILK_WORM_ADVANCEMENT_DESCRIPTION),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_silk_worm", hasItems(item().of(EItems.SILK_WORM.get()).build()))
                    .save(saver, modLoc("core/silk_worm"), helper);
            var stringMesh = advancement()
                    .parent(silkWorm)
                    .display(
                            EItems.STRING_MESH.get(),
                            Component.translatable(TranslationKeys.STRING_MESH_ADVANCEMENT_TITLE),
                            Component.translatable(TranslationKeys.STRING_MESH_ADVANCEMENT_DESCRIPTION),
                            null,
                            FrameType.TASK,
                            true,
                            true,
                            false
                    )
                    .addCriterion("has_string_mesh", hasItems(item().of(EItems.STRING_MESH.get()).build()))
                    .save(saver, modLoc("core/string_mesh"), helper);
        }
    }
}
