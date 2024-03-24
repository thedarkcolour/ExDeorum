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

package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.FakePlayer;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;
import thedarkcolour.exdeorum.config.EConfig;

import java.util.function.Function;

public abstract class AbstractSieveBlockEntity extends EBlockEntity implements SieveLogic.Owner {
    protected final SieveLogic logic;
    private final float sieveInterval;

    public AbstractSieveBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, float sieveInterval, Function<SieveLogic.Owner, SieveLogic> logic) {
        super(type, pos, state);

        this.sieveInterval = sieveInterval;
        this.logic = logic.apply(this);
    }

    public static ItemStack singleCopy(ItemStack stack) {
        var copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    @Override
    public boolean handleResultItem(ItemStack result, ServerLevel level, RandomSource rand) {
        var pos = this.worldPosition;
        var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, result);
        itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
        level.addFreshEntity(itemEntity);
        return true;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public ServerLevel getServerLevel() {
        return (ServerLevel) this.level;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        this.logic.saveNbt(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.logic.loadNbt(nbt);
    }

    @Override
    public SieveLogic getLogic() {
        return this.logic;
    }

    @Override
    public void writeVisualData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.logic.getMesh());
        buffer.writeFloat(this.logic.getProgress());
        buffer.writeItem(this.logic.getContents());
    }

    @Override
    public void readVisualData(FriendlyByteBuf buffer) {
        this.logic.setMesh(buffer.readItem(), false);
        this.logic.setProgress(buffer.readFloat());
        this.logic.setContents(buffer.readItem());
    }



    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack playerItem = player.getItemInHand(hand);
        boolean isClientSide = level.isClientSide;

        // Try insert mesh
        if (this.logic.getMesh().isEmpty()) {
            if (this.logic.isValidMesh(playerItem)) {
                if (!isClientSide) {
                    this.logic.setMesh(singleCopy(playerItem));

                    if (!player.getAbilities().instabuild) {
                        playerItem.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.SUCCESS;
                }
            }
        } else if (this.logic.getContents().isEmpty()) {
            // remove mesh with sneak right click
            if (player.isShiftKeyDown() && player.getMainHandItem().isEmpty()) {
                popOutMesh(level, this.worldPosition, this.logic);
            }
        }

        if (!isClientSide) {
            // Insert an item
            if (this.logic.getContents().isEmpty()) {
                // If the input has any sieve drops, insert it into the mesh
                if (this.logic.isValidInput(playerItem)) {
                    playerItem = insertContents(player, hand, this.logic);
                    var realPlayer = !(player instanceof FakePlayer);

                    // prevent machines placing in multiple sieves if nerf is off to avoid confusion
                    if ((realPlayer || !EConfig.SERVER.nerfAutomatedSieves.get()) && canUseSimultaneously()) {
                        int range = EConfig.SERVER.simultaneousSieveUsageRange.get();
                        var cursor = this.worldPosition.mutable().move(-range, 0, -range);
                        var selfType = getType();

                        // Fill adjacent sieves
                        otherSieves:
                        for (int x = -range; x <= range; x++) {
                            for (int z = -range; z <= range; z++) {
                                if (playerItem.isEmpty()) {
                                    break otherSieves;
                                }

                                if ((x | z) != 0) {
                                    if (level.getBlockEntity(cursor) instanceof AbstractSieveBlockEntity other && other.getType() == selfType) {
                                        var otherLogic = other.logic;

                                        if (otherLogic.getContents().isEmpty()) {
                                            if (this.logic.getMesh().getItem() == otherLogic.getMesh().getItem()) {
                                                playerItem = insertContents(player, hand, otherLogic);
                                            }
                                        }
                                    }
                                }
                                cursor.move(0, 0, 1);
                            }
                            cursor.move(1, 0, (-2 * range) - 1);
                        }
                    }
                }
            } else {
                var time = level.getGameTime();
                var realPlayer = !(player instanceof FakePlayer);

                if ((realPlayer || !EConfig.SERVER.nerfAutomatedSieves.get()) && canUseSimultaneously()) {
                    int range = EConfig.SERVER.simultaneousSieveUsageRange.get();
                    var cursor = this.worldPosition.mutable().move(-range, 0, -range);
                    var selfType = getType();

                    // Sieve with adjacent sieves
                    for (int x = -range; x <= range; x++) {
                        for (int z = -range; z <= range; z++) {
                            if (level.getBlockEntity(cursor) instanceof AbstractSieveBlockEntity other && other.getType() == selfType) {
                                var otherLogic = other.logic;

                                if (!otherLogic.getContents().isEmpty()) {
                                    if (this.logic.getMesh().getItem() == otherLogic.getMesh().getItem()) {
                                        otherLogic.sift(this.sieveInterval, time);
                                    }
                                }
                            }
                            cursor.move(0, 0, 1);
                        }
                        cursor.move(1, 0, (-2 * range) - 1);
                    }
                } else if (realPlayer || EConfig.SERVER.automatedSieves.get()) {
                    this.logic.sift(this.sieveInterval, time);
                }
            }
        }

        return InteractionResult.sidedSuccess(isClientSide);
    }

    // Fills the sieve (assumes contents is EMPTY) and returns the remaining item, putting it in the player's hand
    public static ItemStack insertContents(Player player, InteractionHand hand, SieveLogic logic) {
        var consume = !player.getAbilities().instabuild;
        var playerItem = player.getItemInHand(hand);

        if (consume) {
            if (playerItem.getCount() == 1) {
                logic.startSifting(playerItem);
                player.setItemInHand(hand, ItemStack.EMPTY);
                playerItem = ItemStack.EMPTY;
            } else {
                logic.startSifting(singleCopy(playerItem));
                playerItem.shrink(1);
            }
        } else {
            logic.startSifting(singleCopy(playerItem));
        }

        return playerItem;
    }

    // Do not call on client side
    public static void popOutMesh(Level level, BlockPos sievePos, SieveLogic logic) {
        if (!level.isClientSide) {
            // Pop out item
            var itemEntity = new ItemEntity(level, sievePos.getX() + 0.5, sievePos.getY() + 1.5, sievePos.getZ() + 0.5, logic.getMesh());
            var rand = level.random;
            itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
            level.addFreshEntity(itemEntity);

            // Empty contents
            logic.setMesh(ItemStack.EMPTY);
        }
    }

    protected boolean canUseSimultaneously() {
        return false;
    }
}
