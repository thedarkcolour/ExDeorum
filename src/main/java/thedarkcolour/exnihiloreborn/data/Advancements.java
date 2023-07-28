package thedarkcolour.exnihiloreborn.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.ImpossibleTrigger;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.PlayerTrigger;
import net.minecraft.advancements.critereon.RecipeCraftedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.tag.EItemTags;

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
        return new ResourceLocation(ExNihiloReborn.ID, path);
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
                            false
                    )
                    .addCriterion("craft_crook", RecipeCraftedTrigger.TriggerInstance.craftedItem(EItems.CROOK.getId()))
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
                            false
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
