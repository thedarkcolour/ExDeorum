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

package thedarkcolour.exdeorum.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.registry.EFluids;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.function.Supplier;

public class PorcelainBucket extends Item {
    private final Supplier<? extends Fluid> fluid;

    public PorcelainBucket(Supplier<? extends Fluid> fluid, Properties properties) {
        super(properties);
        this.fluid = fluid;
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Cow) {
            if (!target.isBaby()) {
                var level = player.level();
                player.playSound(SoundEvents.COW_MILK, 1.0f, 1.0f);
                if (!level.isClientSide) {
                    // have to make a copy to prevent player voiding the item stack in line 1056
                    // when it calls interactLivingEntity and checks if the stack is empty afterwards
                    var result = ItemUtils.createFilledResult(stack.getCount() == 1 ? stack.copy() : stack, player, new ItemStack(EItems.PORCELAIN_MILK_BUCKET.get()));
                    player.setItemInHand(hand, result);
                }
                return InteractionResult.sidedSuccess(level.isClientSide);
            }
        }

        return InteractionResult.PASS;
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand pHand) {
        var stack = player.getItemInHand(pHand);
        var hitResult = getPlayerPOVHitResult(level, player, this.fluid.get() == Fluids.EMPTY ? ClipContext.Fluid.SOURCE_ONLY : ClipContext.Fluid.NONE);
        var ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(player, level, stack, hitResult);
        if (ret != null) return ret;
        if (hitResult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        } else if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(stack);
        } else {
            var pos = hitResult.getBlockPos();
            var face = hitResult.getDirection();
            var relative = pos.relative(face);
            if (level.mayInteract(player, pos) && player.mayUseItemAt(relative, face, stack)) {
                if (this.fluid.get() == Fluids.EMPTY) {
                    var state = level.getBlockState(pos);
                    var fluidType = state.getFluidState().getFluidType();

                    if (fluidType == ForgeMod.WATER_TYPE.get() || fluidType == ForgeMod.LAVA_TYPE.get() || fluidType == EFluids.WITCH_WATER_TYPE.get()) {
                        if (state.getBlock() instanceof BucketPickup pickup) {
                            var result = pickup.pickupBlock(level, pos, state);

                            if (fluidType == ForgeMod.WATER_TYPE.get()) {
                                result = new ItemStack(EItems.PORCELAIN_WATER_BUCKET.get());
                            } else if (fluidType == ForgeMod.LAVA_TYPE.get()) {
                                result = new ItemStack(EItems.PORCELAIN_LAVA_BUCKET.get());
                            } else {
                                result = new ItemStack(EItems.PORCELAIN_WITCH_WATER_BUCKET.get());
                            }

                            if (!result.isEmpty()) {
                                player.awardStat(Stats.ITEM_USED.get(this));
                                pickup.getPickupSound(state).ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
                                level.gameEvent(player, GameEvent.FLUID_PICKUP, pos);
                                var filled = ItemUtils.createFilledResult(stack, player, result);
                                if (!level.isClientSide) {
                                    CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, result);
                                }

                                return InteractionResultHolder.sidedSuccess(filled, level.isClientSide());
                            }
                        }
                    }

                    return InteractionResultHolder.fail(stack);
                } else {
                    var state = level.getBlockState(pos);
                    var placePos = canBlockContainFluid(level, pos, state) ? pos : relative;

                    if (emptyContents(player, level, placePos, hitResult, stack)) {
                        if (player instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, placePos, stack);
                        }

                        player.awardStat(Stats.ITEM_USED.get(this));
                        return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(stack, player), level.isClientSide());
                    } else {
                        return InteractionResultHolder.fail(stack);
                    }
                }
            } else {
                return InteractionResultHolder.fail(stack);
            }
        }
    }

    private ItemStack getEmptySuccessItem(ItemStack stack, Player player) {
        if (!player.getAbilities().instabuild) {
            if (fluid.get() == Fluids.LAVA) {
                return ItemStack.EMPTY;
            } else {
                return new ItemStack(EItems.PORCELAIN_BUCKET.get());
            }
        } else {
            return stack;
        }
    }

    public boolean emptyContents(@Nullable Player player, Level level, BlockPos pos, @Nullable BlockHitResult hitResult, @Nullable ItemStack container) {
        if (!(this.fluid.get() instanceof FlowingFluid)) {
            return false;
        } else {
            var state = level.getBlockState(pos);
            var block = state.getBlock();
            var replacing = state.canBeReplaced(this.fluid.get());
            var canPlaceAtPos = state.isAir() || replacing || block instanceof LiquidBlockContainer liquidContainer && liquidContainer.canPlaceLiquid(level, pos, state, this.fluid.get());
            var containedFluidStack = java.util.Optional.ofNullable(container).flatMap(net.minecraftforge.fluids.FluidUtil::getFluidContained);

            if (!canPlaceAtPos) {
                return hitResult != null && this.emptyContents(player, level, hitResult.getBlockPos().relative(hitResult.getDirection()), null, container);
            } else if (containedFluidStack.isPresent() && this.fluid.get().getFluidType().isVaporizedOnPlacement(level, pos, containedFluidStack.get())) {
                this.fluid.get().getFluidType().onVaporize(player, level, pos, containedFluidStack.get());
                return true;
            } else if (level.dimensionType().ultraWarm() && this.fluid.get().is(FluidTags.WATER)) {
                var i = pos.getX();
                var j = pos.getY();
                var k = pos.getZ();
                level.playSound(player, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (level.random.nextFloat() - level.random.nextFloat()) * 0.8F);

                for(int l = 0; l < 8; ++l) {
                    level.addParticle(ParticleTypes.LARGE_SMOKE, i + Math.random(), j + Math.random(), k + Math.random(), 0, 0, 0);
                }

                return true;
            } else if (block instanceof LiquidBlockContainer liquidContainer && liquidContainer.canPlaceLiquid(level, pos, state, this.fluid.get())) {
                liquidContainer.placeLiquid(level, pos, state, ((FlowingFluid)this.fluid.get()).getSource(false));
                playEmptySound(player, level, pos);
                return true;
            } else {
                if (!level.isClientSide && replacing && !state.liquid()) {
                    level.destroyBlock(pos, true);
                }

                if (!level.setBlock(pos, this.fluid.get().defaultFluidState().createLegacyBlock(), 11) && !state.getFluidState().isSource()) {
                    return false;
                } else {
                    playEmptySound(player, level, pos);
                    return true;
                }
            }
        }
    }

    protected void playEmptySound(@Nullable Player pPlayer, LevelAccessor pLevel, BlockPos pPos) {
        var sound = this.fluid.get().getFluidType().getSound(pPlayer, pLevel, pPos, net.minecraftforge.common.SoundActions.BUCKET_EMPTY);
        if (sound == null) {
            sound = this.fluid.get().is(FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        }
        pLevel.playSound(pPlayer, pPos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PLACE, pPos);
    }

    protected boolean canBlockContainFluid(Level level, BlockPos pos, BlockState state) {
        return state.getBlock() instanceof LiquidBlockContainer block && block.canPlaceLiquid(level, pos, state, fluid.get());
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new PorcelainBucket.CapabilityProvider(stack);
    }

    static class CapabilityProvider implements ICapabilityProvider, IFluidHandlerItem {
        private final LazyOptional<IFluidHandlerItem> holder = LazyOptional.of(() -> this);
        private ItemStack container;

        public CapabilityProvider(@NotNull ItemStack container) {
            this.container = container;
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ForgeCapabilities.FLUID_HANDLER_ITEM.orEmpty(cap, this.holder);
        }

        @Override
        public @NotNull ItemStack getContainer() {
            return this.container;
        }

        @Override
        public int getTanks() {
            return 1;
        }

        @Override
        public @NotNull FluidStack getFluidInTank(int tank) {
            return getFluid();
        }

        @Override
        public int getTankCapacity(int tank) {
            return 1000;
        }

        @Override
        public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return stack.getFluid() == Fluids.LAVA || stack.getFluid() == Fluids.WATER || stack.getFluid() == EFluids.WITCH_WATER.get();
        }

        @Override
        public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
            if (this.container.getCount() != 1 || resource.getAmount() < 1000 || !getFluid().isEmpty() || !isFluidValid(0, resource)) {
                return 0;
            }
            if (action.execute()) {
                setFluid(resource);
            }
            return 1000;
        }

        @Override
        public @NotNull FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
            if (this.container.getCount() != 1 || resource.getAmount() < 1000) {
                return FluidStack.EMPTY;
            }
            var stack = getFluid();
            if (!stack.isEmpty() && stack.isFluidEqual(resource)) {
                if (action.execute()) {
                    setFluid(FluidStack.EMPTY);
                }
                return stack;
            }

            return FluidStack.EMPTY;
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (this.container.getCount() != 1 || maxDrain < 1000) {
                return FluidStack.EMPTY;
            }

            FluidStack fluidStack = getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    setFluid(FluidStack.EMPTY);
                }
                return fluidStack;
            }

            return FluidStack.EMPTY;
        }

        FluidStack getFluid() {
            var item = this.container.getItem();

            if (item == EItems.PORCELAIN_LAVA_BUCKET.get()) {
                return new FluidStack(Fluids.LAVA, 1000);
            } else if (item == EItems.PORCELAIN_WATER_BUCKET.get()) {
                return new FluidStack(Fluids.WATER, 1000);
            } else if (item == EItems.PORCELAIN_WITCH_WATER_BUCKET.get()) {
                return new FluidStack(EFluids.WITCH_WATER.get(), 1000);
            }

            return FluidStack.EMPTY;
        }

        void setFluid(FluidStack fluidStack) {
            if (fluidStack.isEmpty()) {
                this.container = new ItemStack(EItems.PORCELAIN_BUCKET.get());
            } else if (fluidStack.getFluid() == Fluids.LAVA) {
                this.container = new ItemStack(EItems.PORCELAIN_LAVA_BUCKET.get());
            } else if (fluidStack.getFluid() == Fluids.WATER) {
                this.container = new ItemStack(EItems.PORCELAIN_WATER_BUCKET.get());
            } else if (fluidStack.getFluid() == EFluids.WITCH_WATER.get()) {
                this.container = new ItemStack(EItems.PORCELAIN_WITCH_WATER_BUCKET.get());
            }
        }
    }
}
