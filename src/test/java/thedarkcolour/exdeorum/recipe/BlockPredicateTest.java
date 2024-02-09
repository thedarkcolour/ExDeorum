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

import io.netty.buffer.Unpooled;
import net.minecraft.SharedConstants;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.Bootstrap;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockPredicateTest {
    @BeforeEach
    void setUp() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
    }

    @Test
    void networkTest() {
        var buffer = new FriendlyByteBuf(Unpooled.buffer());

        var single = BlockPredicate.singleBlock(Blocks.DIAMOND_BLOCK);
        single.toNetwork(buffer);
        assertEquals(single, BlockPredicate.fromNetwork(buffer));

        var states = BlockPredicate.blockState(Blocks.OAK_LOG, StatePropertiesPredicate.Builder.properties().hasProperty(BlockStateProperties.AXIS, Direction.Axis.X).build());
        states.toNetwork(buffer);
        var test = BlockPredicate.fromNetwork(buffer);
        assertEquals(states.toJson(), test.toJson());

        var tag = BlockPredicate.blockTag(BlockTags.OAK_LOGS);
        tag.toNetwork(buffer);
        assertEquals(tag, BlockPredicate.fromNetwork(buffer));
    }
}