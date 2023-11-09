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
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowlFoodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
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
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
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

    public BarrelBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.BARREL.get(), pos, state);
    }

    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> item);
    private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> tank);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.FLUID_HANDLER) {
            return fluidHandler.cast();
        } else if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("item", item.serializeNBT());
        nbt.put("tank", tank.writeToNBT(new CompoundTag()));
        nbt.putShort("compost", compost);
        nbt.putFloat("progress", progress);
        nbt.putShort("r", r);
        nbt.putShort("g", g);
        nbt.putShort("b", b);
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
        return tank.getFluidAmount() == 1000 && progress != 0.0f && !isBurning();
    }

    public boolean isBurning() {
        return isHotFluid(tank.getFluid().getFluid().getFluidType()) && progress != 0.0f;
    }

    // Composting is in progress if at 1000. When finished, compost is set back to 0
    public boolean isComposting() {
        return compost == 1000;
    }

    // Returns true if there are no solid ingredients (can a fluid be inserted?)
    public boolean isEmptySolids() {
        return compost <= 0 && item.getStackInSlot(0).isEmpty();
    }

    public boolean hasFullWater() {
        return tank.getFluidAmount() == 1000 && tank.getFluid().getFluid().is(FluidTags.WATER);
    }

    // Burning temp of wood according to google is 300 C or ~575 kelvin
    // Molten Constantan from Thermal Expansion is 650 kelvin, so this should be fine
    public static boolean isHotFluid(FluidType fluidType) {
        return fluidType.getTemperature() > 575;
    }

    private void spawnBurningParticles() {
        if (isBurning()) {
            BlockPos pos = getBlockPos();
            int burnTicks = (int) (progress * 300);

            if (burnTicks % 30 == 0) {
                level.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + Math.random(), pos.getY() + 1.2, pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
            } else if (burnTicks % 5 == 0) {
                level.addParticle(ParticleTypes.SMOKE, pos.getX() + Math.random(), pos.getY() + 1.2, pos.getZ() + Math.random(), 0.0, 0.0, 0.0);
            }
        }
    }

    public ItemStack getItem() {
        return item.getStackInSlot(0);
    }

    private void setItem(ItemStack item) {
        this.item.setStackInSlot(0, item);
    }

    public IFluidTank getTank() {
        return this.tank;
    }

    public InteractionResult use(Level level, BlockPos pos, Player player, InteractionHand hand) {
        // Collect an item
        if (!getItem().isEmpty()) {
            return giveResultItem(level, pos);
        }

        // Handle item fluid interaction
        if (isEmptySolids()) {
            var wasBurning = isBurning();

            if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
                if (wasBurning && !isHotFluid(tank.getFluid().getFluid().getFluidType())) {
                    progress = 0.0f;
                }

                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        var playerItem = player.getItemInHand(hand);
        if (!level.isClientSide) {
            var bottled = false;

            if (EConfig.SERVER.allowWaterBottleTransfer.get()) {
                var fluid = new FluidStack(Fluids.WATER, 250);

                if (playerItem.getItem() == Items.POTION && PotionUtils.getPotion(playerItem) == Potions.WATER) {
                    if (tank.fill(fluid, IFluidHandler.FluidAction.SIMULATE) > 0) {
                        if (!player.getAbilities().instabuild) {
                            player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
                        }
                        tank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
                        bottled = true;
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                } else if (playerItem.getItem() == Items.GLASS_BOTTLE) {
                    if (tank.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
                        if (!player.getAbilities().instabuild) {
                            playerItem.shrink(1);
                        }
                        var bottle = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
                        if (!player.addItem(bottle)) {
                            player.drop(bottle, false);
                        }
                        tank.drain(fluid, IFluidHandler.FluidAction.EXECUTE);
                        bottled = true;
                        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_EMPTY, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                }
            }

            if (!bottled) {
                var handItem = item.insertItem(0, player.getAbilities().instabuild ? playerItem.copy() : playerItem, false);

                if (!player.getAbilities().instabuild) {
                    player.setItemInHand(hand, handItem);
                    giveResultItem(level, pos);
                }
            } else {
                markUpdated();
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Pops the item out of the barrel (ex. dirt that has finished composting)
    private InteractionResult giveResultItem(Level level, BlockPos pos) {
        if (!level.isClientSide) {
            popOutItem(level, pos, item.extract(false));

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
     * @param input The input item to craft with
     * @param simulate Whether the craft should actually take place
     * @return Whether a craft was made or is possible
     */
    private boolean tryCrafting(ItemStack input, boolean simulate) {
        boolean crafted = false;
        if (!tank.isEmpty()) {
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
     * @param simulate Whether this is a test or the player is actually mixing with the barrel
     * @return Whether something was mixed (if simulated, whether the mix is possible)
     */
    private boolean tryMixing(ItemStack playerItem, boolean simulate) {
        if (isBurning()) {
            return false;
        }

        var recipe = RecipeUtil.getBarrelMixingRecipe(level.getRecipeManager(), playerItem, this.tank.getFluid());

        if (recipe != null) {
            if (!simulate) {
                // Empty barrel
                tank.drain(recipe.fluidAmount, IFluidHandler.FluidAction.EXECUTE);
                // Replace fluid with result
                setItem(new ItemStack(recipe.result));
                level.playSound(null, worldPosition, SoundEvents.AMBIENT_UNDERWATER_EXIT, SoundSource.BLOCKS, 0.8f, 0.8f);
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
            var recipe = RecipeUtil.getBarrelCompostRecipe(stack.getItem());
            if (recipe != null) {
                addCompost(stack, recipe.getVolume());
                return true;
            } else {
                return false;
            }
        }
    }

    private void addCompost(ItemStack playerItem, int volume) {
        int oldCompost = compost;
        compost = (short) Math.min(1000, compost + volume);

        if (compost != 0) {
            if (!CompostColors.isLoaded()) {
                CompostColors.loadColors();
            }

            float weightNew = (float) (compost - oldCompost) / compost;
            float weightOld = 1 - weightNew;
            var color = CompostColors.COLORS.getOrDefault(playerItem.getItem(), CompostColors.DEFAULT_COLOR);

            r = (short) (weightNew * color.x + weightOld * r);
            g = (short) (weightNew * color.y + weightOld * g);
            b = (short) (weightNew * color.z + weightOld * b);
        }

        level.playSound(null, worldPosition, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS);
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
                barrel.spawnBurningParticles();
            }
        }
    }

    private static float getProgressStep() {
        return EConfig.SERVER.barrelProgressStep.get().floatValue();
    }

    private void doCompost() {
        progress += getProgressStep();
        markUpdated();

        if (progress >= 1.0f) {
            progress = 0.0f;
            compost = 0;
            setItem(new ItemStack(Items.DIRT));
            level.playSound(null, worldPosition, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS);
        }
    }

    private static ItemStack getRemainderItem(ItemStack stack) {
        return (stack.getItem() instanceof BowlFoodItem || stack.getItem() == Items.SUSPICIOUS_STEW) ? new ItemStack(Items.BOWL) : stack.getCraftingRemainingItem();
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
            if (!stacks.get(slot).isEmpty())
                return stack;

            if (tryCrafting(stack, simulate)) {
                if (stack.getCount() == 1) {
                    return getRemainderItem(stack);
                } else {
                    popOutItem(level, worldPosition, getRemainderItem(stack));
                    return ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
                }
            } else {
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
            if (!level.isClientSide) {
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
            return !isBrewing() && BarrelBlockEntity.this.isEmptySolids();
        }
    }
}
