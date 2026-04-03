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

package thedarkcolour.exdeorum.block;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import thedarkcolour.exdeorum.blockentity.MechanicalHammerBlockEntity;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.registry.EBlockEntities;

public class MechanicalHammerBlock extends MachineBlock {
    public static final BooleanProperty RUNNING = BooleanProperty.create("running");
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;

    public MechanicalHammerBlock(Properties properties) {
        super(properties, EBlockEntities.MECHANICAL_HAMMER);

        registerDefaultState(defaultBlockState().setValue(RUNNING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(RUNNING, FACING);
    }

    @Override
    protected int getHighlightItemSlot() {
        return MechanicalHammerBlockEntity.HAMMER_SLOT;
    }

    @Override
    protected MutableComponent getHighlightItemLabel() {
        return Component.translatable(TranslationKeys.MECHANICAL_HAMMER_HAMMER_LABEL);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var state = defaultBlockState();
        var customData = context.getItemInHand().get(DataComponents.BLOCK_ENTITY_DATA);

        if (customData != null) {
            @SuppressWarnings("deprecation")
            var nbt = customData.getUnsafe();

            if (nbt.contains("progress") && nbt.getInt("progress") != MechanicalHammerBlockEntity.NOT_RUNNING) {
                state = state.setValue(RUNNING, true);
            }
        }

        return state.setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}
