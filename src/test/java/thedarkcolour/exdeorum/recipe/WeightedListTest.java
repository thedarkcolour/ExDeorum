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

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.XoroshiroRandomSource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeightedListTest {

    @Test
    void isEmpty() {
        WeightedList.Builder<Object> list = WeightedList.builder();
        assertTrue(list.build().isEmpty());
    }

    @Test
    void getRandom() {
        RandomSource rand = new XoroshiroRandomSource(123, 123);
        // An equal list should give all elements a roughly equal chance of appearing randomly
        WeightedList<String> equalList = WeightedList.<String>builder()
                .add(20, "Hello")
                .add(20, "Hi")
                .add(20, "How ya doin")
                .build();

        int hellos = 0;
        int his = 0;
        int howYaDoins = 0;

        for (int i = 0; i < 1_000_000; i++) {
            String choice = equalList.getRandom(rand);
            switch (choice) {
                case "Hello" -> hellos++;
                case "Hi" -> his++;
                case "How ya doin" -> howYaDoins++;
                default -> fail("Bad test");
            }
        }

        System.out.println("Hello: " + hellos + String.format(" (%.2f)", (float) hellos / 1000000));
        System.out.println("Hi: " + his + String.format(" (%.2f)", (float) his / 1000000));
        System.out.println("How ya doin: " + howYaDoins + String.format(" (%.2f)", (float) howYaDoins / 1000000));

        // Ensure that these two elements have roughly the same frequency
        assertTrue(Mth.abs((float) hellos / 1_000_000f - (float) his / 1_000_000f) < 0.025f);

        // Test that the weights specified in the list roughly match the random results
        WeightedList<String> unbalanced = WeightedList.<String>builder()
                .add(60, "Hello")
                .add(20, "Hi")
                .add(20, "How ya doin")
                .build();

        hellos = 0;
        his = 0;
        howYaDoins = 0;

        for (int i = 0; i < 1_000_000; i++) {
            String choice = unbalanced.getRandom(rand);
            switch (choice) {
                case "Hello" -> hellos++;
                case "Hi" -> his++;
                case "How ya doin" -> howYaDoins++;
                default -> fail("Bad test");
            }
        }

        // Ensure the dominant element has the 60% frequency, just as its 60/100 weight specifies
        assertTrue(Mth.abs((float) hellos / 1_000_000f - 0.6f) < 0.025f);
    }

    @Test
    void testEquals() {
        // regular equality
        assertNotEquals(
                WeightedList.builder().add(20, new Object()).add(30, new Object()).build(),
                WeightedList.builder().add(20, new Object()).add(30, new Object()).build()
        );
        // deep equality
        assertEquals(
                WeightedList.<String>builder().add(20, "Hi").add(30, "Hello").build(),
                WeightedList.<String>builder().add(20, "Hi").add(30, "Hello").build()
        );
    }
}