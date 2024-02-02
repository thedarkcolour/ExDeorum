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
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.block.MechanicalHammerBlock;
import thedarkcolour.exdeorum.blockentity.helper.ItemHelper;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.loot.HammerLootModifier;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.hammer.HammerRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.tag.EItemTags;

public class MechanicalHammerBlockEntity extends AbstractMachineBlockEntity<MechanicalHammerBlockEntity> {
    private static final Component TITLE = Component.translatable(TranslationKeys.MECHANICAL_HAMMER_SCREEN_TITLE);
    private static final int INPUT_SLOT = 0;
    public static final int HAMMER_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    public static final int TOTAL_PROGRESS = 10_000_000;
    // process should take 200 ticks or 10 seconds with no efficiency
    private static final int PROGRESS_INTERVAL = TOTAL_PROGRESS / 200;
    public static final int NOT_RUNNING = -1;

    // an integer from 0 to 10,000,000 instead of a decimal number which is inaccurate and buggy
    private int progress = NOT_RUNNING;
    private float efficiency;

    public MechanicalHammerBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.MECHANICAL_HAMMER.get(), pos, state, ItemHandler::new, EConfig.SERVER.mechanicalHammerEnergyStorage.get());
    }

    public static boolean isValidInput(ItemStack stack) {
        return RecipeUtil.getHammerRecipe(stack.getItem()) != null;
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.putInt("progress", this.progress);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.progress = nbt.getInt("progress");
        onHammerChanged();
    }

    @Override
    public Component getDisplayName() {
        return TITLE;
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player pPlayer) {
        return new MechanicalHammerMenu(containerId, playerInventory, this);
    }

    @Override
    protected boolean isRunning() {
        return this.progress != NOT_RUNNING;
    }

    @Override
    protected void tryStartRunning() {
        var input = this.inventory.getStackInSlot(INPUT_SLOT);

        if (!input.isEmpty()) {
            if (canFitResultIntoOutput(input) != null) {
                this.progress = 0;
                this.level.setBlock(this.worldPosition, getBlockState().setValue(MechanicalHammerBlock.RUNNING, true), 3);

                return;
            }
        }

        if (getBlockState().getValue(MechanicalHammerBlock.RUNNING)) {
            this.level.setBlock(this.worldPosition, getBlockState().setValue(MechanicalHammerBlock.RUNNING, false), 3);
        }
    }

    @Override
    protected void noEnergyTick() {
        if (getBlockState().getValue(MechanicalHammerBlock.RUNNING)) {
            this.level.setBlock(this.worldPosition, getBlockState().setValue(MechanicalHammerBlock.RUNNING, false), 3);
        }
    }

    @Nullable
    private HammerRecipe canFitResultIntoOutput(ItemStack input) {
        var output = this.inventory.getStackInSlot(OUTPUT_SLOT);

        if (output.isEmpty() || output.getCount() < output.getMaxStackSize()) {
            var recipe = RecipeUtil.getHammerRecipe(input.getItem());

            if (recipe != null && (output.isEmpty() || matchesStack(recipe.result, output))) {
                return recipe;
            }
        }

        return null;
    }

    private static boolean matchesStack(Item item, ItemStack stack) {
        return !stack.hasTag() && item == stack.getItem();
    }

    @Override
    protected void runMachineTick() {
        var input = this.inventory.getStackInSlot(INPUT_SLOT);

        if (!input.isEmpty()) {
            this.progress += (int) (PROGRESS_INTERVAL * this.efficiency);

            if (this.progress >= TOTAL_PROGRESS) {
                var recipe = canFitResultIntoOutput(input);

                if (recipe != null) {
                    @SuppressWarnings("DataFlowIssue")
                    LootContext ctx = RecipeUtil.emptyLootContext((ServerLevel) this.level);
                    var resultCount = recipe.resultAmount.getInt(ctx);
                    resultCount += HammerLootModifier.calculateFortuneBonus(this.inventory.getStackInSlot(HAMMER_SLOT), ctx.getRandom(), resultCount == 0);
                    var output = this.inventory.getStackInSlot(OUTPUT_SLOT);
                    if (output.isEmpty()) {
                        this.inventory.setStackInSlot(OUTPUT_SLOT, new ItemStack(recipe.result, resultCount));
                    } else {
                        output.setCount(Math.min(output.getMaxStackSize(), resultCount + output.getCount()));
                    }
                    input.shrink(1);
                    damageHammer(ctx.getRandom());

                    setChanged();
                }

                this.progress = NOT_RUNNING;
            }
        } else {
            this.level.setBlock(this.worldPosition, this.getBlockState().setValue(MechanicalHammerBlock.RUNNING, false), 3);
        }
    }

    private void damageHammer(RandomSource rand) {
        var hammer = this.inventory.getStackInSlot(HAMMER_SLOT);

        if (hammer.isDamageableItem()) {

            if (hammer.hurt(1, rand, null)) {
                hammer.shrink(1);

                if (hammer.isEmpty()) {
                    this.inventory.setStackInSlot(HAMMER_SLOT, ItemStack.EMPTY);
                }
            }
        }
    }

    private void onHammerChanged() {
        var hammer = this.inventory.getStackInSlot(HAMMER_SLOT);
        if (hammer.isEmpty()) {
            this.efficiency = 1f;
        } else {
            // This timing allows full efficiency hammer to match full efficiency sieve (55 ticks/craft
            // Rewards player for using hammer by doubling speed right off the bat, before efficiency
            // although not as fast as Mekanism's crusher, still pretty fast and much cheaper
            this.efficiency = 2f + hammer.getEnchantmentLevel(Enchantments.BLOCK_EFFICIENCY) * 0.33f;
        }
    }

    @Override
    protected int getEnergyConsumption() {
        return EConfig.SERVER.mechanicalHammerEnergyConsumption.get();
    }

    // The value synced to the client for rendering the arrow in GUI
    public int getGuiProgress() {
        return Math.round((float)(24 * this.progress) / TOTAL_PROGRESS);
    }

    public void setGuiProgress(int guiProgress) {
        this.progress = (guiProgress * TOTAL_PROGRESS) / 24;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    private static class ItemHandler extends ItemHelper {
        private final MechanicalHammerBlockEntity hammer;

        public ItemHandler(MechanicalHammerBlockEntity hammer) {
            super(3);
            this.hammer = hammer;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == INPUT_SLOT) {
                return RecipeUtil.getHammerRecipe(stack.getItem()) != null;
            } else if (slot == HAMMER_SLOT) {
                return stack.is(EItemTags.HAMMERS);
            } else {
                return false;
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return slot == HAMMER_SLOT ? 1 : super.getSlotLimit(slot);
        }

        @Override
        public boolean canMachineExtract(int slot) {
            return slot == OUTPUT_SLOT;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (slot == HAMMER_SLOT) {
                this.hammer.onHammerChanged();
            } else if (slot == INPUT_SLOT) {
                if (getStackInSlot(INPUT_SLOT).isEmpty()) {
                    this.hammer.progress = NOT_RUNNING;
                }
            }
        }
    }
}
