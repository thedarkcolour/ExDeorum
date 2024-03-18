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

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
import thedarkcolour.exdeorum.registry.ENumberProviders;

import java.util.List;

public record SummationGenerator(List<NumberProvider> providers) implements NumberProvider {
    public static final Codec<SummationGenerator> CODEC = RecordCodecBuilder.create(instance -> instance.group(NumberProviders.CODEC.listOf().fieldOf("values").forGetter(SummationGenerator::providers)).apply(instance, SummationGenerator::new));

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
}
