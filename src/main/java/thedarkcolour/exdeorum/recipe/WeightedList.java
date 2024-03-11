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

package thedarkcolour.exdeorum.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

// much simpler version of SimpleWeightedRandomList that supports .equals
public class WeightedList<T> {
    private final int totalWeight;
    private final Object[] values;
    private final int[] weights;

    private WeightedList(int totalWeight, Object[] values, int[] weights) {
        Preconditions.checkArgument(values.length == weights.length, "Values and weights arrays are different sizes");

        this.totalWeight = totalWeight;
        this.values = values;
        this.weights = weights;
    }

    public boolean isEmpty() {
        return this.values.length == 0;
    }

    // list must not be empty
    @SuppressWarnings("unchecked")
    public T getRandom(RandomSource rand) {
        int chosenIndex = rand.nextInt(this.totalWeight);

        for (int i = 0; i < this.values.length; i++) {
            chosenIndex -= this.weights[i];
            if (chosenIndex < 0) {
                return (T) this.values[i];
            }
        }

        throw new IllegalStateException("Could not get random element");
    }

    @SuppressWarnings("unchecked")
    public void toNetwork(FriendlyByteBuf buffer, BiConsumer<FriendlyByteBuf, T> valueWriter) {
        buffer.writeVarInt(this.values.length);
        for (int i = 0; i < this.values.length; i++) {
            valueWriter.accept(buffer, (T) this.values[i]);
            buffer.writeVarInt(this.weights[i]);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightedList<?> that = (WeightedList<?>) o;
        return Arrays.equals(this.values, that.values) && Arrays.equals(this.weights, that.weights);
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(this.values);
        result = 31 * result + Arrays.hashCode(this.weights);
        return result;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static <T> WeightedList<T> fromNetwork(FriendlyByteBuf buffer, Function<FriendlyByteBuf, T> valueReader) {
        int size = buffer.readVarInt();
        Object[] values = new Object[size];
        int[] weights = new int[size];
        int totalWeight = 0;
        for (int i = 0; i < size; i++) {
            values[i] = Objects.requireNonNull(valueReader.apply(buffer), "Failed to read weighted list value from network");
            totalWeight += weights[i] = buffer.readVarInt();
        }
        return new WeightedList<>(totalWeight, values, weights);
    }

    public JsonArray toJson(Function<T, JsonElement> serializer) {
        JsonArray array = new JsonArray();
        for (int i = 0; i < this.values.length; i++) {
            @SuppressWarnings("unchecked")
            var value = serializer.apply((T) this.values[i]);
            var weight = this.weights[i];
            var json = new JsonObject();
            json.add("value", value);
            json.addProperty("weight", weight);
            array.add(json);
        }
        return array;
    }

    @Nullable
    public static <T> WeightedList<T> fromJson(JsonArray array, Function<JsonElement, @Nullable T> deserializer) {
        WeightedList.Builder<T> list = WeightedList.builder();

        for (var element : array) {
            if (element instanceof JsonObject obj) {
                int weight = GsonHelper.getAsInt(obj, "weight");
                var value = deserializer.apply(obj.get("value"));
                if (value == null) {
                    return null;
                } else {
                    list.add(weight, value);
                }
            } else {
                throw new JsonSyntaxException("Invalid weighted list entry");
            }
        }
        return list.build();
    }

    public static class Builder<T> {
        private final ArrayList<T> values = new ArrayList<>();
        private final IntArrayList weights = new IntArrayList();
        private int totalWeight;

        public Builder<T> add(int weight, T element) {
            this.totalWeight += weight;
            this.values.add(element);
            this.weights.add(weight);
            return this;
        }

        public WeightedList<T> build() {
            return new WeightedList<>(this.totalWeight, this.values.toArray(), this.weights.toArray(new int[0]));
        }
    }
}
