/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.common.util.FakePlayer;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.tag.EItemTags;

import java.util.Map;

public class SieveBlockEntity extends EBlockEntity {
    public static final short MAX_SIEVE_CAPACITY = 100;
    public static final short SIEVE_INTERVAL = 10;

    private ItemStack contents = ItemStack.EMPTY;
    private ItemStack mesh = ItemStack.EMPTY;
    private short progress = 0; // Max is 100
    private float efficiency = 1f;
    private int fortune = 0;

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.SIEVE.get(), pos, state);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("contents", contents.serializeNBT());
        nbt.putShort("progress", progress);
        if (!mesh.isEmpty()) {
            nbt.put("mesh", mesh.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        if (nbt.contains("contents")) {
            this.contents = ItemStack.of(nbt.getCompound("contents"));
        } else {
            this.contents = ItemStack.EMPTY;
        }
        this.progress = nbt.getShort("progress");
        if (nbt.contains("mesh")) {
            setMesh(ItemStack.of(nbt.getCompound("mesh")));
        } else {
            setMesh(ItemStack.EMPTY);
        }

        super.load(nbt);
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack playerItem = player.getItemInHand(hand);
        boolean isClientSide = level.isClientSide;

        // Try insert mesh
        if (mesh.isEmpty()) {
            if (isMesh(playerItem)) {
                if (!level.isClientSide) {
                    var meshCopy = playerItem.copy();
                    meshCopy.setCount(1);
                    setMesh(meshCopy);

                    markUpdated();

                    if (!player.getAbilities().instabuild) {
                        playerItem.shrink(1);
                    }
                    return InteractionResult.CONSUME;
                } else {
                    return InteractionResult.SUCCESS;
                }
            }
        } else if (contents.isEmpty()) {
            // remove mesh with sneak right click
            if (player.isShiftKeyDown() && player.getMainHandItem().isEmpty()) {
                removeMesh();
            }
        }

        if (contents.isEmpty()) {
            // Insert an item
            if (!isClientSide) {
                // If the input has any sieve drops, insert it into the mesh
                if (!RecipeUtil.getSieveRecipes(mesh.getItem(), playerItem).isEmpty()) {
                    playerItem = this.insertContents(player, hand);
                    markUpdated();

                    if (EConfig.SERVER.simultaneousSieveUsage.get()) {
                        var cursor = worldPosition.mutable().move(-1, 0, -1);

                        // Fill adjacent sieves
                        otherSieves:
                        for (int x = -1; x <= 1; x++) {
                            for (int z = -1; z <= 1; z++) {
                                if (playerItem.isEmpty()) {
                                    break otherSieves;
                                }

                                if ((x | z) != 0) {
                                    if (level.getBlockEntity(cursor) instanceof SieveBlockEntity other) {
                                        if (other.contents.isEmpty()) {
                                            if (this.mesh.getItem() == other.mesh.getItem()) {
                                                playerItem = other.insertContents(player, hand);
                                                other.markUpdated();
                                            }
                                        }
                                    }
                                }

                                cursor.move(0, 0, 1);
                            }
                            cursor.move(1, 0, -3);
                        }
                    }
                }
            }
        } else {
            var realPlayer = !(player instanceof FakePlayer);

            if (realPlayer && EConfig.SERVER.simultaneousSieveUsage.get()) {
                var cursor = worldPosition.mutable().move(-1, 0, -1);

                // Sieve with adjacent sieves
                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {
                        if (level.getBlockEntity(cursor) instanceof SieveBlockEntity other) {
                            if (!other.contents.isEmpty()) {
                                if (this.mesh.getItem() == other.mesh.getItem()) {
                                    other.performSift(player);
                                }
                            }
                        }

                        cursor.move(0, 0, 1);
                    }
                    cursor.move(1, 0, -3);
                }
            } else if (realPlayer || EConfig.SERVER.automatedSieves.get()) {
                performSift(player);
            }
        }

        return InteractionResult.sidedSuccess(isClientSide);
    }

    // Fills the sieve (assumes contents is EMPTY) and returns the remaining item, putting it in the player's hand
    private ItemStack insertContents(Player player, InteractionHand hand) {
        var consume = !player.getAbilities().instabuild;
        var playerItem = player.getItemInHand(hand);

        if (consume) {
            if (playerItem.getCount() == 1) {
                this.contents = playerItem;
                player.setItemInHand(hand, ItemStack.EMPTY);
                playerItem = ItemStack.EMPTY;
            } else {
                this.contents = singleCopy(playerItem);
                playerItem.shrink(1);
            }
        } else {
            this.contents = singleCopy(playerItem);
        }

        this.progress = MAX_SIEVE_CAPACITY;

        return playerItem;
    }

    private static ItemStack singleCopy(ItemStack stack) {
        var copy = stack.copy();
        copy.setCount(1);
        return copy;
    }

    private void performSift(Player player) {
        progress -= efficiency * SIEVE_INTERVAL;

        if (progress <= 0) {
            progress = 0;

            if (!level.isClientSide) {
                giveItems(player);
            }
        } else {
            markUpdated();
        }
    }

    private void removeMesh() {
        if (!level.isClientSide) {
            // Pop out item
            var itemEntity = new ItemEntity(level, worldPosition.getX() + 0.5, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5, mesh);
            var rand = level.random;
            itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
            level.addFreshEntity(itemEntity);

            // Empty contents
            setMesh(ItemStack.EMPTY);
            markUpdated();
        }
    }

    private void setMesh(ItemStack mesh) {
        this.mesh = mesh;
        this.efficiency = 1f + mesh.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * 0.17f;
        this.fortune = mesh.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
    }

    private void giveItems(Player player) {
        var pos = this.worldPosition;
        var level = this.level;
        var context = new LootContext.Builder(new LootParams((ServerLevel) level, Map.of(), Map.of(), player.getLuck())).create(null);
        var rand = this.level.random;
        var limitDrops = this.contents.getItem() == Items.MOSS_BLOCK && EConfig.SERVER.limitMossSieveDrops.get();

        for (SieveRecipe recipe : RecipeUtil.getSieveRecipes(this.mesh.getItem(), this.contents)) {
            var amount = recipe.resultAmount.getInt(context);

            for (int i = 0; i < this.fortune; i++) {
                if (rand.nextFloat() < 0.3f) {
                    amount += recipe.resultAmount.getInt(context);
                }
            }

            if (amount >= 1) {
                var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, new ItemStack(recipe.result, amount));
                itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
                level.addFreshEntity(itemEntity);

                // limit drops to 1 or two items (could be more than 2 but unlikely)
                if (limitDrops && rand.nextInt(5) != 0) {
                    break;
                }
            }
        }

        this.contents = ItemStack.EMPTY;
        markUpdated();
    }

    private boolean isMesh(ItemStack stack) {
        return stack.is(EItemTags.SIEVE_MESHES);
    }

    public ItemStack getMesh() {
        return this.mesh;
    }

    // Used for rendering
    public short getProgress() {
        return this.progress;
    }

    // Used for rendering
    public ItemStack getContents() {
        return this.contents;
    }
}
