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
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import thedarkcolour.exdeorum.block.BarrelBlock;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.recipe.barrel.BarrelFluidMixingRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrelBlockEntity extends EBlockEntity {
    private static final int MOSS_SPREAD_RANGE = 2;

    private final BarrelBlockEntity.ItemHandler item = new BarrelBlockEntity.ItemHandler();
    private final BarrelBlockEntity.FluidHandler tank = new BarrelBlockEntity.FluidHandler();
    public float progress;
    public short compost;
    // compost colors
    public short r, g, b;
    // used to avoid obsidian dupe
    private boolean isBeingFilledByPlayer;

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.BARREL.get(), pos, state);
    }

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> this.item);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> this.tank);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return this.fluidHandler.cast();
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return this.itemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.fluidHandler.invalidate();
        this.itemHandler.invalidate();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("item", this.item.serializeNBT());
        nbt.put("tank", this.tank.writeToNBT(new CompoundTag()));
        nbt.putShort("compost", this.compost);
        nbt.putFloat("progress", this.progress);
        nbt.putShort("r", this.r);
        nbt.putShort("g", this.g);
        nbt.putShort("b", this.b);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.item.deserializeNBT(nbt.getCompound("item"));
        this.tank.readFromNBT(nbt.getCompound("tank"));
        this.compost = nbt.getShort("compost");
        this.progress = nbt.getFloat("progress");
        this.r = nbt.getShort("r");
        this.g = nbt.getShort("g");
        this.b = nbt.getShort("b");
    }

    public boolean isBrewing() {
        return this.tank.getFluidAmount() == 1000 && this.progress != 0.0f && !isBurning();
    }

    public boolean isBurning() {
        return isHotFluid(this.tank.getFluid().getFluid().getFluidType()) && this.progress != 0.0f;
    }

    // Composting is in progress if at 1000. When finished, compost is set back to 0
    public boolean isComposting() {
        return this.compost == 1000;
    }

    // Returns true if there are no solid ingredients (can a fluid be inserted?)
    public boolean hasNoSolids() {
        return this.compost <= 0 && this.item.getStackInSlot(0).isEmpty();
    }

    public boolean hasFullWater() {
        return this.tank.getFluidAmount() == 1000 && this.tank.getFluid().getFluid().is(FluidTags.WATER);
    }

    // Burning temp of wood according to google is 300 C or ~575 kelvin
    // Molten Constantan from Thermal Expansion is 650 kelvin, so this should be fine
    public static boolean isHotFluid(FluidType fluidType) {
        return fluidType.getTemperature() > 575;
    }

    private void spawnParticlesIfBurning() {
        if (isBurning()) {
            BlockPos pos = getBlockPos();
            int burnTicks = (int) (this.progress * 300);

            if (burnTicks % 30 == 0) {
                this.level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + 1.2, pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
            } else if (burnTicks % 5 == 0) {
                this.level.addParticle(ParticleTypes.SMOKE, pos.getX() + Math.random(), pos.getY() + 1.2, pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
            }
        }
    }

    public ItemStack getItem() {
        return this.item.getStackInSlot(0);
    }

    private void setItem(ItemStack item) {
        this.item.setStackInSlot(0, item);
    }

    public IFluidTank getTank() {
        return this.tank;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        // Collect an item
        if (!getItem().isEmpty()) {
            return giveResultItem(level);
        }

        // Handle item fluid interaction first
        if (hasNoSolids()) {
            var wasBurning = isBurning();

            this.isBeingFilledByPlayer = true;

            if (FluidUtil.interactWithFluidHandler(player, hand, this.tank)) {
                this.isBeingFilledByPlayer = false;
                tryInWorldFluidMixing();

                // If the item is a fluid handler, try to transfer fluids
                if (wasBurning && !isHotFluid(this.tank.getFluid().getFluid().getFluidType())) {
                    this.progress = 0.0f;
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            } else {
                this.isBeingFilledByPlayer = false;
                // try one more time to transfer fluids between item and barrel
                var playerItem = player.getItemInHand(hand);
                if (EConfig.SERVER.allowWaterBottleTransfer.get()) {
                    var fluid = new FluidStack(Fluids.WATER, 250);

                    if (playerItem.getItem() == Items.POTION && PotionUtils.getPotion(playerItem) == Potions.WATER) {
                        if (this.tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                            if (!player.getAbilities().instabuild) {
                                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                            }
                            this.tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);

                            markUpdated();
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                    } else if (playerItem.getItem() == Items.GLASS_BOTTLE) {
                        if (this.tank.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
                            extractWaterBottle(this.tank, level, player, playerItem, fluid);

                            markUpdated();
                            return InteractionResult.sidedSuccess(level.isClientSide);
                        }
                    }
                }

                // Otherwise, mix the item's fluid into the barrel's fluid
                var itemFluidCap = playerItem.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve();
                if (itemFluidCap.isPresent()) {
                    var fluidInTank = itemFluidCap.get().getFluidInTank(0);

                    if (this.tank.getFluidAmount() >= 1000) {
                        if (!level.isClientSide) {
                            tryFluidMixing(fluidInTank.getFluid());
                        }
                        // If a mix was successful, skip rest of logic
                        return InteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }

        // If the barrel has no solids and no fluid mixing/transfer happened
        var playerItem = player.getItemInHand(hand);
        if (!level.isClientSide) {
            // mix item ingredient into fluid OR turn into compost (delegated to item handler)
            var handItem = this.item.insertItem(0, player.getAbilities().instabuild ? playerItem.copy() : playerItem, false);

            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, handItem);
                giveResultItem(level);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Also used by Water Crucibles
    protected static void extractWaterBottle(IFluidHandler tank, Level level, Player player, ItemStack playerItem, FluidStack fluid) {
        if (!player.getAbilities().instabuild) {
            playerItem.shrink(1);
        }
        var bottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
        if (!player.addItem(bottle)) {
            player.drop(bottle, false);
        }
        tank.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);
    }

    // Pops the item out of the barrel (ex. dirt that has finished composting)
    private InteractionResult giveResultItem(Level level) {
        if (!level.isClientSide) {
            popOutItem(level, this.worldPosition, this.item.extract(false));

            // Empty contents
            setItem(ItemStack.EMPTY);
            markUpdated();
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private static void popOutItem(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide && !stack.isEmpty()) {
            var rand = level.random;
            var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, stack);
            itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
            level.addFreshEntity(itemEntity);
        }
    }

    /**
     * @param input    The input item to craft with
     * @param simulate Whether the craft should actually take place
     * @return Whether a craft was made or is possible
     */
    private boolean tryCrafting(ItemStack input, boolean simulate) {
        boolean crafted = false;
        if (!this.tank.isEmpty()) {
            crafted = tryMixing(input, simulate);
        } else if (!isComposting()) {
            crafted = tryComposting(input, simulate);
        }

        if (crafted) {
            if (!simulate) {
                markUpdated();
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param playerItem The item to try to mix with this barrel's fluid
     * @param simulate   Whether this is a test or the player is actually mixing with the barrel
     * @return Whether something was mixed (if simulated, whether the mix is possible)
     */
    private boolean tryMixing(ItemStack playerItem, boolean simulate) {
        if (isBurning()) {
            return false;
        }

        var recipe = RecipeUtil.getBarrelMixingRecipe(this.level.getRecipeManager(), playerItem, this.tank.getFluid());

        if (recipe != null) {
            if (!simulate) {
                // Empty barrel
                this.tank.drain(recipe.fluidAmount, IFluidHandler.FluidAction.EXECUTE);
                // Replace fluid with result
                setItem(new ItemStack(recipe.result));
                this.level.playSound(null, this.worldPosition, SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.BLOCKS, 0.8f, 0.8f);
            }
            // Mixing was successful, so return true
            return true;
        } else {
            return false;
        }
    }

    private boolean tryComposting(ItemStack stack, boolean simulate) {
        if (simulate) {
            return RecipeUtil.isCompostable(stack);
        } else {
            var recipe = RecipeUtil.getBarrelCompostRecipe(stack);
            if (recipe != null) {
                addCompost(stack, recipe.getVolume());
                return true;
            } else {
                return false;
            }
        }
    }

    private void addCompost(ItemStack playerItem, int volume) {
        int oldCompost = this.compost;
        this.compost = (short) Math.min(1000, this.compost + volume);

        if (this.compost != 0) {
            if (!CompostColors.isLoaded()) {
                CompostColors.loadColors();
            }

            float weightNew = (float) (this.compost - oldCompost) / this.compost;
            float weightOld = 1 - weightNew;
            var color = CompostColors.COLORS.getOrDefault(playerItem.getItem(), CompostColors.DEFAULT_COLOR);

            this.r = (short) (weightNew * color.x + weightOld * this.r);
            this.g = (short) (weightNew * color.y + weightOld * this.g);
            this.b = (short) (weightNew * color.z + weightOld * this.b);
        }

        this.level.playSound(null, this.worldPosition, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS);
    }

    /**
     * Called in three cases, which should cover all the possible edge cases:
     * <li> When the barrel is updated by a neighboring block (see {@link BarrelBlock#neighborChanged}) </li>
     * <li> When the barrel is first loaded from NBT (see {@link #onLoad}) </li>
     * <li> When the fluid in the barrel changes (see {@link FluidHandler#onContentsChanged()}) </li>
     */
    public void tryInWorldFluidMixing() {
        if (!this.tank.isEmpty() && this.item.getStackInSlot(0).isEmpty()) {
            var aboveFluid = this.level.getBlockState(this.worldPosition.above()).getFluidState().getType();

            if (aboveFluid != Fluids.EMPTY) {
                tryFluidMixing(aboveFluid);
            }
        }
    }

    private void tryFluidMixing(Fluid additive) {
        BarrelFluidMixingRecipe recipe = RecipeUtil.getFluidMixingRecipe(this.tank.getFluid(), additive);

        if (recipe != null) {
            this.tank.drain(recipe.baseFluidAmount, IFluidHandler.FluidAction.EXECUTE);
            setItem(new ItemStack(recipe.result));
        }
    }

    public static class Ticker implements BlockEntityTicker<BarrelBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, BarrelBlockEntity barrel) {
            if (!level.isClientSide) {
                // Turn compost to dirt
                if (barrel.isComposting()) {
                    barrel.doCompost();
                } else if (barrel.hasFullWater()) {
                    var rand = level.random;
                    var mycelium = 0f;

                    for (BlockPos cursor : BlockPos.betweenClosed(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1)) {
                        if (level.getBlockState(cursor).getBlock() == Blocks.MYCELIUM) {
                            mycelium += 0.15f;

                            if (rand.nextInt(1500) == 0) {
                                BlockPos above = cursor.above();

                                if (level.getBlockState(above).isAir()) {
                                    if (rand.nextBoolean()) {
                                        level.setBlockAndUpdate(above, Blocks.RED_MUSHROOM.defaultBlockState());
                                    } else {
                                        level.setBlockAndUpdate(above, Blocks.BROWN_MUSHROOM.defaultBlockState());
                                    }
                                }
                            }
                        }
                    }

                    // Try to perform a fluid transformation recipe
                    if (barrel.tank.getFluid().getFluid().getFluidType() == ForgeMod.WATER_TYPE.get()) {
                        if (mycelium == 0 && state.ignitedByLava() && rand.nextInt(500) == 0) {
                            var randomPos = pos.offset(rand.nextIntBetweenInclusive(-MOSS_SPREAD_RANGE, MOSS_SPREAD_RANGE), -1, rand.nextIntBetweenInclusive(-MOSS_SPREAD_RANGE, MOSS_SPREAD_RANGE));
                            var randomBlock = level.getBlockState(randomPos).getBlock();

                            if (randomBlock == Blocks.COBBLESTONE) {
                                level.setBlock(randomPos, Blocks.MOSSY_COBBLESTONE.defaultBlockState(), 3);
                            } else if (randomBlock == Blocks.STONE_BRICKS) {
                                level.setBlock(randomPos, Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 3);
                            }
                        }

                        if (barrel.progress != (barrel.progress += mycelium * getProgressStep())) {
                            barrel.markUpdated();
                        }

                        if (barrel.progress >= 1.0f) {
                            // Reset progress
                            barrel.progress = 0.0f;
                            level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0f, 0.6f);
                            barrel.tank.setFluid(new FluidStack(EFluids.WITCH_WATER.get(), barrel.tank.getFluidAmount()));
                        }
                    }
                } else if (isHotFluid(barrel.tank.getFluid().getFluid().getFluidType())) {
                    if (state.ignitedByLava()) {
                        if ((barrel.progress += getProgressStep()) >= 1.0f) {
                            if (barrel.tank.getFluidAmount() == 1000) {
                                var fluid = barrel.tank.getFluid().getFluid();
                                level.setBlockAndUpdate(pos, fluid.getFluidType().getBlockForFluidState(level, pos, fluid.defaultFluidState()));
                            } else {
                                level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                            }
                        }
                        barrel.markUpdated();
                    }
                } else if (level.isRainingAt(pos.above()) && barrel.item.getStackInSlot(0).isEmpty() && barrel.compost == 0) {
                    if (barrel.tank.isEmpty()) {
                        barrel.tank.setFluid(new FluidStack(Fluids.WATER, 1));
                        barrel.markUpdated();
                    } else if (barrel.tank.getFluid().getFluid() == Fluids.WATER) {
                        barrel.tank.getFluid().grow(1);
                        barrel.markUpdated();
                    }
                }
            } else {
                barrel.spawnParticlesIfBurning();
            }
        }
    }

    private static float getProgressStep() {
        return EConfig.SERVER.barrelProgressStep.get().floatValue();
    }

    private void doCompost() {
        this.progress += getProgressStep();
        markUpdated();

        if (this.progress >= 1.0f) {
            this.progress = 0.0f;
            this.compost = 0;
            setItem(new ItemStack(Items.DIRT));
            this.level.playSound(null, this.worldPosition, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS);
        }
    }

    private static ItemStack getRemainderItem(ItemStack stack) {
        return (stack.getItem() instanceof BowlFoodItem || stack.getItem() == Items.SUSPICIOUS_STEW) ? new ItemStack(Items.BOWL) : stack.getCraftingRemainingItem();
    }

    @Override
    public void onLoad() {
        tryInWorldFluidMixing();
    }

    // Inner class
    private class ItemHandler extends ItemStackHandler {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return tryCrafting(stack, true);
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty())
                return ItemStack.EMPTY;
            validateSlotIndex(slot);
            if (!this.stacks.get(slot).isEmpty())
                return stack;

            if (tryCrafting(stack, simulate)) {
                if (stack.getCount() == 1) {
                    return getRemainderItem(stack);
                } else {
                    popOutItem(BarrelBlockEntity.this.level, BarrelBlockEntity.this.worldPosition, getRemainderItem(stack));
                    return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
                }
            } else {
                stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(itemFluid -> tryFluidMixing(itemFluid.getFluidInTank(0).getFluid()));
                return stack;
            }
        }

        public ItemStack extract(boolean simulate) {
            return extractItem(0, 1, simulate);
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (!BarrelBlockEntity.this.level.isClientSide) {
                markUpdated();
            }
        }
    }

    // Inner class
    private class FluidHandler extends FluidTank {
        public FluidHandler() {
            super(1000);
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return !isBrewing() && BarrelBlockEntity.this.hasNoSolids();
        }

        @Override
        protected void onContentsChanged() {
            if (!BarrelBlockEntity.this.isBeingFilledByPlayer) {
                BarrelBlockEntity.this.tryInWorldFluidMixing();
            }
        }
    }
}
