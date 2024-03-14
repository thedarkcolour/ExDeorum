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

package thedarkcolour.exdeorum.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import thedarkcolour.exdeorum.registry.ENumberProviders;

public record SummationGenerator(NumberProvider[] providers) implements NumberProvider {
    @Override
    public float getFloat(LootContext context) {
        float sum = 0f;
        for (NumberProvider provider : this.providers) {
            sum += provider.getFloat(context);
        }
        return sum;
    }

    @Override
    public LootNumberProviderType getType() {
        return ENumberProviders.SUMMATION.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<SummationGenerator> {
        @Override
        public void serialize(JsonObject json, SummationGenerator value, JsonSerializationContext ctx) {
            JsonArray array = new JsonArray();
            for (var provider : value.providers) {
                array.add(ctx.serialize(provider, NumberProvider.class));
            }
            json.add("values", array);
        }

        @Override
        public SummationGenerator deserialize(JsonObject json, JsonDeserializationContext ctx) {
            var valuesJson = GsonHelper.getAsJsonArray(json, "values");
            NumberProvider[] providers = new NumberProvider[valuesJson.size()];
            int i = 0;
            for (var valueJson : valuesJson) {
                providers[i++] = ctx.deserialize(valueJson, NumberProvider.class);
            }

            return new SummationGenerator(providers);
        }
    }
}
