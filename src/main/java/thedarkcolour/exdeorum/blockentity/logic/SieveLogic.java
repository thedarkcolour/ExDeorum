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

package thedarkcolour.exdeorum.blockentity.logic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.sieve.SieveRecipe;
import thedarkcolour.exdeorum.tag.EItemTags;

public class SieveLogic {
    private final Owner owner;
    private final boolean saveMesh;
    private final boolean mechanical;

    // block currently being sifted
    private ItemStack contents = ItemStack.EMPTY;
    // mesh
    private ItemStack mesh = ItemStack.EMPTY;
    // from 0.0 to 1.0
    private float progress;
    private float efficiency;
    private int fortune;
    private long lastTime = 0;
    private final long minInterval;

    public SieveLogic(Owner owner, boolean saveMesh, boolean mechanical) {
        this.owner = owner;
        this.saveMesh = saveMesh;
        this.mechanical = mechanical;
        this.minInterval = EConfig.SERVER.sieveIntervalTicks.get();
    }

    public ItemStack getMesh() {
        return this.mesh;
    }

    public boolean isValidInput(ItemStack stack) {
        return !RecipeUtil.getSieveRecipes(this.mesh.getItem(), stack).isEmpty();
    }

    public boolean isValidMesh(ItemStack stack) {
        return stack.is(EItemTags.SIEVE_MESHES);
    }

    // not pure, modifies the received stack
    public void startSifting(ItemStack stack) {
        this.contents = stack;
        this.owner.markUpdated();
    }

    // Do not call on the client side
    public void sift(float incrementProgress, long time) {
        if (time < this.lastTime + this.minInterval) {
            return;
        }
        
        this.lastTime = time;
        this.progress += incrementProgress * this.efficiency;

        // Need epsilon because floating point decimals suck
        if (this.progress >= 1.0f - Mth.EPSILON) {
            var level = this.owner.getServerLevel();
            var context = RecipeUtil.emptyLootContext(level);
            var rand = level.random;
            var limitDrops = this.contents.getItem() == Items.MOSS_BLOCK && EConfig.SERVER.limitMossSieveDrops.get();
            var handledAnyDrops = false;
            var hasDrops = false;

            for (SieveRecipe recipe : RecipeUtil.getSieveRecipes(this.mesh.getItem(), this.contents)) {
                var amount = getResultAmount(recipe, context, rand);

                // Split overflowing stacks (64+) into multiple stacks
                while (amount > 0) {
                    hasDrops = true;
                    // make a single item copy of recipe result
                    var result = new ItemStack(recipe.result, 1);
                    // the size of the stack respecting stack limits (ex. ender pearl limits to 16)
                    var stackAmount = Math.min(amount, recipe.result.getMaxStackSize(result));
                    result.setCount(stackAmount);
                    amount -= stackAmount;
                    var handleDrop = this.owner.handleResultItem(result, level, rand);
                    handledAnyDrops = handledAnyDrops || handleDrop;

                    // limit drops to 1 or two items (could be more than 2 but unlikely)
                    if (limitDrops && rand.nextInt(5) != 0) {
                        break;
                    }
                }
            }

            if (handledAnyDrops || !hasDrops) {
                this.contents = ItemStack.EMPTY;
                this.progress = 0.0f;
            } else {
                this.progress = 1.0f;
            }
        }

        this.owner.markUpdated();
    }

    protected int getResultAmount(SieveRecipe recipe, LootContext context, RandomSource rand) {
        if (recipe.byHandOnly && this.mechanical) return 0;

        var amount = recipe.resultAmount.getInt(context);

        // Each level of fortune grants a 30% chance for an extra roll
        for (int i = 0; i < this.fortune; ++i) {
            if (rand.nextFloat() < 0.3f) {
                amount += recipe.resultAmount.getInt(context);
            }
        }

        return amount;
    }

    public void setMesh(ItemStack mesh) {
        this.setMesh(mesh, true);
    }

    public void setMesh(ItemStack mesh, boolean needsUpdate) {
        this.mesh = mesh;
        this.efficiency = 1f + mesh.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * 0.17f;
        this.fortune = mesh.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        if (mesh.isEmpty()) {
            this.progress = 0.0f;
            this.contents = ItemStack.EMPTY;
        }
        if (needsUpdate) {
            this.owner.markUpdated();
        }
    }

    public void saveNbt(CompoundTag nbt) {
        if (!this.contents.isEmpty()) {
            nbt.put("contents", this.contents.serializeNBT());
        }
        if (this.saveMesh && !this.mesh.isEmpty()) {
            nbt.put("mesh", this.mesh.save(new CompoundTag()));
        }
        nbt.putFloat("progress", this.progress);
    }

    public void loadNbt(CompoundTag nbt) {
        if (nbt.contains("contents")) {
            this.contents = ItemStack.of(nbt.getCompound("contents"));
        } else {
            this.contents = ItemStack.EMPTY;
        }
        if (nbt.getTagType("progress") == Tag.TAG_SHORT) {
            this.progress = (float) nbt.getShort("progress") / 100f;
        } else {
            this.progress = nbt.getFloat("progress");
        }
        if (this.saveMesh) {
            if (nbt.contains("mesh")) {
                setMesh(ItemStack.of(nbt.getCompound("mesh")), false);
            } else {
                setMesh(ItemStack.EMPTY, false);
            }
        }
    }

    public ItemStack getContents() {
        return this.contents;
    }

    // client only
    public void setContents(ItemStack contents) {
        this.contents = contents;
    }

    public float getProgress() {
        return this.progress;
    }

    // client only
    public void setProgress(float progress) {
        this.progress = progress;
    }

    // implement on the owner of this sieve logic
    public interface Owner {
        ServerLevel getServerLevel();

        // Return whether the result item was consumed
        boolean handleResultItem(ItemStack result, ServerLevel level, RandomSource rand);

        void markUpdated();

        SieveLogic getLogic();
    }
}
