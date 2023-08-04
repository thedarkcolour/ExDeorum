package thedarkcolour.exdeorum.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import thedarkcolour.exdeorum.client.CompostColors;
import thedarkcolour.exdeorum.recipe.RecipeUtil;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EFluids;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrelBlockEntity extends EBlockEntity {
    private static final float PROGRESS_STEP = 1.0f / 300.0f;

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
        return tank.getFluidAmount() == 1000 && tank.getFluid().getFluid() == Fluids.WATER;
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

    public InteractionResult use(Level level, BlockPos pos, Player player, InteractionHand hand) {
        // Collect an item
        if (!getItem().isEmpty()) {
            return giveResultItem(level, pos, player);
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
            var remainder = item.insertItem(0, player.getAbilities().instabuild ? playerItem.copy() : playerItem, false);
            if (!player.getAbilities().instabuild) {
                player.setItemInHand(hand, remainder);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void addCompost(ItemStack playerItem, int volume, boolean consume) {
        int oldCompost = compost;
        compost = (short) Math.min(1000, compost + volume);

        if (compost != 0) {
            if (!CompostColors.isLoaded()) {
                CompostColors.loadColors();
            }

            float weightNew = (float) (compost - oldCompost) / compost;
            float weightOld = 1 - weightNew;
            var color = CompostColors.COLORS.getOrDefault(playerItem.getItem(), new Vector3i(53, 168, 42));

            r = (short) (weightNew * color.x + weightOld * r);
            g = (short) (weightNew * color.y + weightOld * g);
            b = (short) (weightNew * color.z + weightOld * b);

            // Consume item
            if (consume) {
                playerItem.shrink(1);
            }
        }

        level.playSound(null, worldPosition, SoundEvents.COMPOSTER_FILL, SoundSource.BLOCKS);
    }

    // Pops the item out of the barrel (ex. dirt that has finished composting)
    private InteractionResult giveResultItem(Level level, BlockPos pos, Player player) {
        var rand = level.random;
        if (!level.isClientSide) {
            // Pop out item
            var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, item.extract(false));
            itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
            level.addFreshEntity(itemEntity);

            // Empty contents
            setItem(ItemStack.EMPTY);
            markUpdated();
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /**
     * @param input The input item to craft with
     * @param simulate Whether the craft should actually take place
     * @return The resulting item after crafting
     */
    private ItemStack tryCrafting(ItemStack input, boolean simulate) {
        boolean crafted = false;
        var remainder = input.getCraftingRemainingItem();
        if (!tank.isEmpty()) {
            crafted = tryMixing(input, simulate);
        } else if (!isComposting()) {
            crafted = tryComposting(input, simulate);
        }

        if (crafted) {
            if (!simulate) {
                markUpdated();
            }
            return remainder;
        } else {
            return input;
        }
    }

    /**
     * @param playerItem The item to try to mix with this barrel's fluid
     * @param simulate Whether this is a test or the player is actually mixing with the barrel
     * @return Whether something was mixed (if simulated, whether the mix is possible)
     */
    private boolean tryMixing(ItemStack playerItem, boolean simulate) {
        var recipe = RecipeUtil.getBarrelMixingRecipe(level.getRecipeManager(), playerItem, this.tank.getFluid());

        if (recipe != null) {
            if (isBurning()) {
                return false;
            }
            if (!simulate) {
                // Consumer player input
                playerItem.shrink(1);
                // Empty barrel
                tank.setFluid(FluidStack.EMPTY);
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
            var recipe = RecipeUtil.getBarrelCompostRecipe(stack);
            if (recipe != null) {
                addCompost(stack, recipe.getVolume(), true);
                return true;
            } else {
                return false;
            }
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
                        // todo make this not hardcoded
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

                    if (barrel.progress != (barrel.progress += mycelium * PROGRESS_STEP)) {
                        barrel.markUpdated();
                    }

                    if (barrel.progress >= 1.0f) {
                        // Reset progress
                        barrel.progress = 0.0f;

                        barrel.tank.setFluid(new FluidStack(EFluids.WITCH_WATER_STILL.get(), barrel.tank.getFluidAmount()));
                    }

                } else if (isHotFluid(barrel.tank.getFluid().getFluid().getFluidType())) {
                    if (state.ignitedByLava()) {
                        if ((barrel.progress += PROGRESS_STEP) >= 1.0f) {
                            if (barrel.tank.getFluidAmount() == 1000) {
                                var fluid = barrel.tank.getFluid().getFluid();
                                level.setBlockAndUpdate(pos, fluid.getFluidType().getBlockForFluidState(level, pos, fluid.defaultFluidState()));
                            } else {
                                level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
                            }
                        }
                        barrel.markUpdated();
                    }
                }
            } else {
                barrel.spawnBurningParticles();
            }
        }
    }

    private void doCompost() {
        progress += PROGRESS_STEP;
        markUpdated();

        if (progress >= 1.0f) {
            progress = 0.0f;
            compost = 0;
            setItem(new ItemStack(Items.DIRT));
            level.playSound(null, worldPosition, SoundEvents.COMPOSTER_READY, SoundSource.BLOCKS);
        }
    }

    // Inner class
    private class ItemHandler extends ItemStackHandler {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            // if the resulting item from crafting is different (ex. EMPTY or bucket) then we can craft with it
            return !ItemStack.matches(tryCrafting(stack, true), stack);
        }

        // Copy of ItemStackHandler.insertItem which handles barrel crafting remainders better
        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            if (stack.isEmpty())
                return ItemStack.EMPTY;
            validateSlotIndex(slot);
            if (!stacks.get(slot).isEmpty())
                return stack;

            return tryCrafting(stack, simulate);
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
