package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.joml.Vector3i;
import thedarkcolour.exnihiloreborn.client.CompostColors;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EFluids;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// todo test which refresh calls can be removed to avoid network spam
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
        return hasHotFluid() && progress != 0.0f;
    }

    public boolean isComposting() {
        return compost == 1000;
    }

    public boolean hasContents() {
        return compost > 0 || !item.getStackInSlot(0).isEmpty();
    }

    public boolean hasFullWater() {
        return tank.getFluidAmount() == 1000 && tank.getFluid().getFluid() == Fluids.WATER;
    }

    // Burning temp of wood according to google is 300 C or ~575 kelvin
    // Molten Constantan from Thermal Expansion is only 650 kelvin, but this should be fine
    public boolean hasHotFluid() {
        return tank.getFluid().getFluid().getFluidType().getTemperature() > 575;
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
        var isClientSide = level.isClientSide;

        // Collect an item
        if (!getItem().isEmpty()) {
            return giveItem(level, pos, isClientSide);
        }

        // Handle item fluid interaction
        if (!hasContents()) {
            var wasBurning = isBurning();

            if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
                if (wasBurning && !hasHotFluid()) {
                    progress = 0.0f;
                }

                return InteractionResult.sidedSuccess(isClientSide);
            }
        }

        var playerItem = player.getItemInHand(hand);

        // Handle compost
        if (tank.isEmpty()) {
            var inventory = new SimpleContainer(playerItem);

            if (compost < 1000) {
                if (!level.isClientSide) {
                    // todo cache items to recipe
                    level.getServer().getRecipeManager().getRecipeFor(ERecipeTypes.BARREL_COMPOST.get(), inventory, level).ifPresent(recipe -> {
                        if (!CompostColors.isLoaded()) {
                            CompostColors.loadColors();
                        }

                        int oldCompost = compost;
                        compost = (short) Math.min(1000, compost + recipe.getVolume());

                        if (compost != 0) {
                            float weightNew = (float) (compost - oldCompost) / compost;
                            float weightOld = 1 - weightNew;
                            var color = CompostColors.COLORS.getOrDefault(playerItem.getItem(), new Vector3i(53, 168, 42));

                            r = (short) (weightNew * color.x + weightOld * r);
                            g = (short) (weightNew * color.y + weightOld * g);
                            b = (short) (weightNew * color.z + weightOld * b);

                            markUpdated();

                            // Consume item
                            if (!player.getAbilities().instabuild) {
                                playerItem.shrink(1);
                            }
                        }
                    });
                }

                return InteractionResult.sidedSuccess(isClientSide);
            }
        }

        // Mixing
        if (!playerItem.isEmpty()) {
            var fluid = tank.getFluid();
            var container = playerItem.getCraftingRemainingItem();

            if (fluid.getAmount() == 1000) {
                // todo custom mixing recipes
                if (doMix(playerItem, isClientSide)) {
                    if (!isClientSide && playerItem.isEmpty()) {
                        player.setItemInHand(hand, container);
                    }
                    return InteractionResult.sidedSuccess(isClientSide);
                }
            }
        }

        return InteractionResult.CONSUME;
    }

    private InteractionResult giveItem(Level level, BlockPos pos, boolean isClientSide) {
        if (!isClientSide) {
            // Pop out item
            var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, item.extract(false));
            var rand = level.random;
            itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
            level.addFreshEntity(itemEntity);

            // Empty contents
            setItem(ItemStack.EMPTY);
        }

        return InteractionResult.sidedSuccess(isClientSide);
    }

    /**
     * @param fluid Fluid in the barrel
     * @param catalyst Item to mix with the fluid
     * @param result Item (preferable a block) crafted by this mix
     * @param playerItem Player's item to check and consume (test item if simulated)
     * @param simulate Whether to simulate the mix
     * @return if the mix succeeded (if simulated, whether the mix is possible)
     */
    private boolean mix(Fluid fluid, Item catalyst, Item result, ItemStack playerItem, boolean simulate) {
        if (tank.getFluid().getFluid() == fluid && playerItem.getItem() == catalyst) {
            if (!simulate) {
                // Consumer player input
                playerItem.shrink(1);
                // Empty barrel
                tank.setFluid(FluidStack.EMPTY);
                // Replace fluid with result
                setItem(new ItemStack(result));
            }

            return true;
        }

        return false;
    }

    private boolean doMix(ItemStack playerItem, boolean simulate) {
        return mix(Fluids.WATER, EItems.DUST.get(), Items.CLAY, playerItem, simulate) ||
                mix(Fluids.WATER, Items.MILK_BUCKET, Items.SLIME_BLOCK, playerItem, simulate) ||
                mix(EFluids.WITCH_WATER_STILL.get(), Items.SAND, Items.SOUL_SAND, playerItem, simulate) ||
                mix(Fluids.LAVA, Items.REDSTONE, Items.NETHERRACK, playerItem, simulate) ||
                mix(Fluids.LAVA, Items.GLOWSTONE_DUST, Items.END_STONE, playerItem, simulate) ||
                mix(Fluids.LAVA, Items.WATER_BUCKET, Items.OBSIDIAN, playerItem, simulate);
    }

    public static class Ticker implements BlockEntityTicker<BarrelBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, BarrelBlockEntity barrel) {
            if (!level.isClientSide) {
                // Turn compost to dirt
                if (barrel.isComposting()) {
                    barrel.progress += PROGRESS_STEP;
                    barrel.markUpdated();

                    if (barrel.progress >= 1.0f) {
                        barrel.progress = 0.0f;
                        barrel.compost = 0;
                        barrel.setItem(new ItemStack(Items.DIRT));
                    }
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

                } else if (barrel.hasHotFluid()) {
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

    // Inner class
    private class ItemHandler extends ItemStackHandler {
        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            FluidStack fluid = tank.getFluid();

            if (fluid.getAmount() == 1000) {
                return doMix(stack, true);
            }

            return false;
        }

        public ItemStack extract(boolean simulate) {
            return extractItem(0, 1, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            doMix(stacks.get(slot).copy(), level.isClientSide);
            markUpdated();
        }
    }

    // Inner class
    private class FluidHandler extends FluidTank {
        public FluidHandler() {
            super(1000);
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return !isBrewing() && !hasContents();
        }
    }
}
