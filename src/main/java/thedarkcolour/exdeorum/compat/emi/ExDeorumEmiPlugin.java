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

package thedarkcolour.exdeorum.compat.emi;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiRenderable;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.WallTorchBlock;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.compat.ClientXeiUtil;
import thedarkcolour.exdeorum.compat.CompatUtil;
import thedarkcolour.exdeorum.compat.XeiSieveRecipe;
import thedarkcolour.exdeorum.material.DefaultMaterials;
import thedarkcolour.exdeorum.recipe.BlockPredicate;
import thedarkcolour.exdeorum.registry.EItems;
import thedarkcolour.exdeorum.registry.ERecipeTypes;

import java.util.HashSet;
import java.util.Set;

@EmiEntrypoint
public class ExDeorumEmiPlugin implements EmiPlugin {
    static final EmiRecipeCategory BARREL_COMPOST = emiCategory("barrel_compost", (graphics, x, y, partialTick) -> ClientXeiUtil.renderFilledCompostBarrel(graphics, x, y));
    static final EmiRecipeCategory BARREL_MIXING = emiCategory("barrel_mixing", EmiStack.of(DefaultMaterials.OAK_BARREL));
    static final EmiRecipeCategory BARREL_FLUID_MIXING = emiCategory("barrel_fluid_mixing", EmiStack.of(DefaultMaterials.STONE_BARREL));
    static final EmiRecipeCategory LAVA_CRUCIBLE = emiCategory("lava_crucible", EmiStack.of(DefaultMaterials.PORCELAIN_CRUCIBLE));
    static final EmiRecipeCategory WATER_CRUCIBLE = emiCategory("water_crucible", EmiStack.of(DefaultMaterials.OAK_CRUCIBLE));
    static final EmiRecipeCategory CRUCIBLE_HEAT_SOURCES = emiCategory("crucible_heat_sources", EmiStack.of(DefaultMaterials.PORCELAIN_CRUCIBLE));
    static final EmiRecipeCategory SIEVE = emiCategory("sieve", EmiStack.of(DefaultMaterials.OAK_SIEVE));
    static final EmiRecipeCategory COMPRESSED_SIEVE = emiCategory("compressed_sieve", EmiStack.of(DefaultMaterials.OAK_COMPRESSED_SIEVE));
    static final EmiRecipeCategory HAMMER = emiCategory("hammer", EmiStack.of(EItems.DIAMOND_HAMMER.get()));
    static final EmiRecipeCategory COMPRESSED_HAMMER = emiCategory("compressed_hammer", EmiStack.of(EItems.COMPRESSED_DIAMOND_HAMMER.get()));
    static final EmiRecipeCategory CROOK = emiCategory("crook", EmiStack.of(EItems.CROOK.get()));

    private static EmiRecipeCategory emiCategory(String name, EmiRenderable icon) {
        return new EmiRecipeCategory(ExDeorum.loc(name), icon);
    }

    @Override
    public void register(EmiRegistry registry) {
        addCategories(registry);
        addWorkstations(registry);
        addRecipes(registry);
    }

    private static void addCategories(EmiRegistry registry) {
        registry.addCategory(BARREL_COMPOST);
        registry.addCategory(BARREL_MIXING);
        registry.addCategory(BARREL_FLUID_MIXING);
        registry.addCategory(LAVA_CRUCIBLE);
        registry.addCategory(WATER_CRUCIBLE);
        registry.addCategory(CRUCIBLE_HEAT_SOURCES);
        registry.addCategory(SIEVE);
        registry.addCategory(COMPRESSED_SIEVE);
        registry.addCategory(HAMMER);
        registry.addCategory(COMPRESSED_HAMMER);
        registry.addCategory(CROOK);
    }

    private static void addWorkstations(EmiRegistry registry) {
        for (var barrel : CompatUtil.getAvailableBarrels(true)) {
            var stack = EmiStack.of(barrel);
            registry.addWorkstation(BARREL_COMPOST, stack);
            registry.addWorkstation(BARREL_MIXING, stack);
            registry.addWorkstation(BARREL_FLUID_MIXING, stack);
        }
        for (var lavaCrucible : CompatUtil.getAvailableLavaCrucibles(true)) {
            var stack = EmiStack.of(lavaCrucible);
            registry.addWorkstation(LAVA_CRUCIBLE, stack);
            registry.addWorkstation(CRUCIBLE_HEAT_SOURCES, stack);
        }
        for (var waterCrucible : CompatUtil.getAvailableWaterCrucibles(true)) {
            registry.addWorkstation(WATER_CRUCIBLE, EmiStack.of(waterCrucible));
        }
        for (var sieve : CompatUtil.getAvailableSieves(true, true)) {
            registry.addWorkstation(SIEVE, EmiStack.of(sieve));
        }
        for (var compressedSieve : CompatUtil.getAvailableCompressedSieves(true)) {
            registry.addWorkstation(COMPRESSED_SIEVE, EmiStack.of(compressedSieve));
        }

        registry.addWorkstation(HAMMER, EmiStack.of(EItems.WOODEN_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.STONE_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.GOLDEN_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.IRON_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.DIAMOND_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.NETHERITE_HAMMER.get()));
        registry.addWorkstation(HAMMER, EmiStack.of(EItems.MECHANICAL_HAMMER.get()));

        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_WOODEN_HAMMER.get()));
        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_STONE_HAMMER.get()));
        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_GOLDEN_HAMMER.get()));
        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_IRON_HAMMER.get()));
        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_DIAMOND_HAMMER.get()));
        registry.addWorkstation(COMPRESSED_HAMMER, EmiStack.of(EItems.COMPRESSED_NETHERITE_HAMMER.get()));

        registry.addWorkstation(CROOK, EmiStack.of(EItems.CROOK.get()));
        registry.addWorkstation(CROOK, EmiStack.of(EItems.BONE_CROOK.get()));
    }

    private static void addRecipes(EmiRegistry registry) {
        EmiUtil.addAll(registry, ERecipeTypes.BARREL_COMPOST, BarrelCompostEmiRecipe::new);
        EmiUtil.addAll(registry, ERecipeTypes.BARREL_MIXING, BarrelMixingEmiRecipe.Items::new);
        EmiUtil.addAll(registry, ERecipeTypes.BARREL_FLUID_MIXING, BarrelMixingEmiRecipe.Fluids::new);
        EmiUtil.addAll(registry, ERecipeTypes.LAVA_CRUCIBLE, CrucibleEmiRecipe.Lava::new);
        EmiUtil.addAll(registry, ERecipeTypes.WATER_CRUCIBLE, CrucibleEmiRecipe.Water::new);

        for (var holder : registry.getRecipeManager().byType(ERecipeTypes.CRUCIBLE_HEAT_SOURCE.get())) {
            var value = holder.value();
            if (value.blockPredicate() instanceof BlockPredicate.SingleBlockPredicate block && block.block() instanceof WallTorchBlock) {
                continue;
            }
            registry.addRecipe(new CrucibleHeatEmiRecipe(value, holder.id()));
        }

        for (XeiSieveRecipe recipe : XeiSieveRecipe.getAllRecipesGrouped(ERecipeTypes.SIEVE.get(), XeiSieveRecipe.SIEVE_ROWS)) {
            registry.addRecipe(new SieveEmiRecipe.Sieve(recipe));
        }
        for (XeiSieveRecipe recipe : XeiSieveRecipe.getAllRecipesGrouped(ERecipeTypes.COMPRESSED_SIEVE.get(), XeiSieveRecipe.COMPRESSED_SIEVE_ROWS)) {
            registry.addRecipe(new SieveEmiRecipe.CompressedSieve(recipe));
        }

        EmiUtil.addAll(registry, ERecipeTypes.HAMMER, HammerEmiRecipe.Hammer::new);
        EmiUtil.addAll(registry, ERecipeTypes.COMPRESSED_HAMMER, HammerEmiRecipe.CompressedHammer::new);
        EmiUtil.addAll(registry, ERecipeTypes.CROOK, CrookEmiRecipe::new);
    }

    @Override
    public void initialize(EmiInitRegistry registry) {
        Set<ItemLike> toRemove = new HashSet<>();

        toRemove.addAll(CompatUtil.getAvailableBarrels(false));
        toRemove.addAll(CompatUtil.getAvailableSieves(false, false));
        toRemove.addAll(CompatUtil.getAvailableLavaCrucibles(false));
        toRemove.addAll(CompatUtil.getAvailableWaterCrucibles(false));

        Set<Item> toRemoveItems = new HashSet<>();

        for (var itemLike : toRemove) {
            toRemoveItems.add(itemLike.asItem());
        }

        registry.disableStacks(stack -> toRemoveItems.contains(stack.getItemStack().getItem()));
    }
}
