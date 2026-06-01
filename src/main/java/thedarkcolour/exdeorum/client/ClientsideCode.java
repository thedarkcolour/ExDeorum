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

package thedarkcolour.exdeorum.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.fml.util.thread.EffectiveSide;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.recipe.RecipeCaches;

public class ClientsideCode {
    private static final RecipeCaches RECIPE_CACHES = new RecipeCaches();

    public static RecipeCaches getRecipeCaches() {
        assert EffectiveSide.get().isClient() : Thread.currentThread().getName();
        return RECIPE_CACHES;
    }

    @Nullable
    public static RecipeManager getRecipeManager() {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) {
            return connection.getRecipeManager();
        }
        return null;
    }
}
