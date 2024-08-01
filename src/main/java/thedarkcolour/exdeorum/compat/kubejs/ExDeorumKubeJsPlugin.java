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

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaRegistry;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.neoforged.neoforge.common.NeoForge;
import thedarkcolour.exdeorum.ExDeorum;

public class ExDeorumKubeJsPlugin implements KubeJSPlugin {
    static {
        NeoForge.EVENT_BUS.addListener(ExDeorumKubeJsBindings::onRecipeFilterParse);
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add(ExDeorum.ID, new ExDeorumKubeJsBindings());
    }

    @Override
    public void registerRecipeSchemas(RecipeSchemaRegistry registry) {
        registry.namespace(ExDeorum.ID)
                .register("sieve", SieveRecipeSchema.SCHEMA);
    }
}
