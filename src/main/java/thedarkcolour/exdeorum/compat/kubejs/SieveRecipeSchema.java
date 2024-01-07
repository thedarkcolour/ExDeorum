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

package thedarkcolour.exdeorum.compat.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public interface SieveRecipeSchema {
    RecipeComponent<OutputItem> OUTPUT_ITEM_ONLY = new RecipeComponent<>() {
        @Override
        public String componentType() {
            return "output_item_only";
        }

        @Override
        public ComponentRole role() {
            return ComponentRole.OUTPUT;
        }

        @Override
        public Class<?> componentClass() {
            return OutputItem.class;
        }

        @Override
        public boolean hasPriority(RecipeJS recipe, Object from) {
            return recipe.outputItemHasPriority(from);
        }

        @Override
        public JsonElement write(RecipeJS recipe, OutputItem value) {
            return new JsonPrimitive(ForgeRegistries.ITEMS.getKey(value.item.getItem()).toString());
        }

        @Override
        public OutputItem read(RecipeJS recipe, Object from) {
            if (from instanceof JsonPrimitive primitive && primitive.isString()) {
                return OutputItem.of(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(primitive.getAsString()))));
            } else {
                return OutputItem.of(from);
            }
        }

        @Override
        public boolean isOutput(RecipeJS recipe, OutputItem value, ReplacementMatch match) {
            return match instanceof ItemMatch m && !value.isEmpty() && m.contains(value.item);
        }

        @Override
        public String checkEmpty(RecipeKey<OutputItem> key, OutputItem value) {
            if (value.isEmpty()) {
                return "ItemStack '" + key.name + "' can't be empty";
            }

            return "";
        }
    };
    
    RecipeKey<OutputItem> RESULT = OUTPUT_ITEM_ONLY.key("result");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("ingredient");
    
    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENT).uniqueOutputId(RESULT);
}
