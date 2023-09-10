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

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.SpreadingSnowyDirtBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.data.TranslationKeys;
import thedarkcolour.exdeorum.tag.EBlockTags;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class WateringCanItem extends Item {
    private static final int WATERING_INTERVAL = 4;
    private static final int STARTUP_TIME = 10;
    // only used on the clientside
    private static boolean isWatering = false;

    private final int capacity;
    private final boolean renewing;
    private final boolean usableInMachines;

    public WateringCanItem(int capacity, Properties properties) {
        super(properties);

        this.capacity = capacity;
        this.renewing = capacity >= 4000;
        this.usableInMachines = false;
    }

    protected WateringCanItem(boolean usableInMachines, Properties properties) {
        super(properties);

        this.capacity = 4000;
        this.renewing = true;
        this.usableInMachines = usableInMachines;
    }

    public static ItemStack getFull(Supplier<? extends Item> wateringCan) {
        var stack = new ItemStack(wateringCan.get());
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(handler -> handler.fill(new FluidStack(Fluids.WATER, Integer.MAX_VALUE), IFluidHandler.FluidAction.EXECUTE));
        return stack;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return renewing ? stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(handler -> handler.getFluidInTank(0).getAmount() < capacity).orElse(true) : true;
    }

    @Override
    public int getBarColor(ItemStack pStack) {
        return 0x3F76E4;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(fluidHandler -> {
            return Math.round((float) fluidHandler.getFluidInTank(0).getAmount() * 13.0F / (float) capacity);
        }).orElse(0);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {
            var water = Component.translatable(ForgeMod.WATER_TYPE.get().getDescriptionId());

            tooltip.add(water.append(Component.translatable(TranslationKeys.WATERING_CAN_FLUID_DISPLAY, fluidHandler.getFluidInTank(0).getAmount(), capacity)).withStyle(ChatFormatting.GRAY));
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        var itemInHand = player.getItemInHand(hand);
        return itemInHand.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).map(fluidHandler -> {
            if (fluidHandler.getFluidInTank(0).getAmount() < this.capacity) {
                var hitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);

                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    var pos = hitResult.getBlockPos();
                    var state = level.getBlockState(pos);

                    if (state.getFluidState().getType() == Fluids.WATER && state.getBlock() instanceof BucketPickup pickup) {
                        if (!level.isClientSide) {
                            fluidHandler.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);
                            pickup.pickupBlock(level, pos, state);
                            pickup.getPickupSound(state).ifPresent(sound -> player.playSound(sound, 1.0F, 1.0F));
                        }

                        return InteractionResultHolder.sidedSuccess(itemInHand, level.isClientSide);
                    }
                }
            }

            if (!fluidHandler.getFluidInTank(0).isEmpty()) {
                var realPlayer = !(player instanceof FakePlayer);

                if (realPlayer) {
                    player.startUsingItem(hand);
                } else if (this.usableInMachines) {
                    onUseTick(level, player, itemInHand, 72000);
                }

                return InteractionResultHolder.consume(itemInHand);
            }
            return InteractionResultHolder.pass(itemInHand);
        }).orElse(InteractionResultHolder.pass(itemInHand));
    }

    @Override
    public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingTicks) {
        var useTicks = 72000 - remainingTicks;

        if (useTicks >= STARTUP_TIME || living instanceof FakePlayer) {
            stack.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).ifPresent(fluidHandler -> {
                if (!fluidHandler.getFluidInTank(0).isEmpty()) {
                    // do watering can
                    var reachDist = living instanceof Player player ? player.getBlockReach() : living.getAttributeValue(ForgeMod.BLOCK_REACH.get());
                    var hit = living.pick(reachDist, 0, true);

                    if (hit instanceof BlockHitResult blockHit && blockHit.getType() == HitResult.Type.BLOCK) {
                        var pos = blockHit.getBlockPos();
                        var state = level.getBlockState(pos);

                        if (!level.isClientSide) {
                            if (useTicks % WATERING_INTERVAL == 0) {
                                tryWatering((ServerLevel) level, pos, state);

                                if (!this.renewing || fluidHandler.getFluidInTank(0).getAmount() != capacity) {
                                    if (!(living instanceof Player player && player.getAbilities().instabuild)) {
                                        ((CapabilityProvider) fluidHandler).drain();
                                    }
                                }
                            }
                            if (useTicks % 2 == 0) {
                                waterParticles(level, pos, state);
                            }
                            if ((useTicks - STARTUP_TIME) % 20 == 0) {
                                level.playSound(null, pos, SoundEvents.WEATHER_RAIN, living.getSoundSource(), this.getClass() == WideWateringCanItem.class ? 0.6f : 0.3f, 1.5f);
                            }
                        } else {
                            isWatering = true;
                        }
                    } else {
                        isWatering = false;
                    }
                } else {
                    living.stopUsingItem();
                    isWatering = false;
                }
            });
        }
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeCharged) {
        if (timeCharged > STARTUP_TIME) {
            level.playLocalSound(living.getX(), living.getY(), living.getZ(), SoundEvents.BUCKET_FILL, living.getSoundSource(), 0.6f, 0.7f, false);
        }
    }

    protected void tryWatering(ServerLevel level, BlockPos pos, BlockState state) {
        if (state.is(EBlockTags.WATERING_CAN_TICKABLE)) {
            if (state.is(BlockTags.SAPLINGS)) {
                if (level.random.nextInt(3) == 0) {
                    state.randomTick(level, pos, level.random);
                    level.levelEvent(LevelEvent.PARTICLES_AND_SOUND_PLANT_GROWTH, pos, 0);
                }
            } else if (state.getBlock() instanceof SugarCaneBlock block) {
                var cursor = pos.mutable();
                while (level.isInWorldBounds(cursor.move(0, 1, 0)) && level.getBlockState(cursor).getBlock() == block) {
                    // just keep looping
                }
                // randomTick only works on the top sugarcane block
                var topState = level.getBlockState(cursor.move(0, -1, 0));
                topState.randomTick(level, cursor, level.random);
            } else {
                state.randomTick(level, pos, level.random);
            }
        } else {
            if (BarrelBlockEntity.isHotFluid(state.getFluidState().getFluidType())) {
                level.levelEvent(LevelEvent.LAVA_FIZZ, pos, 0);
            } else if (state.getBlock() == Blocks.FARMLAND) {
                hydrateFarmland(level, pos, state);
            }
        }
        var below = pos.below();
        var belowState = level.getBlockState(below);
        if (belowState.getBlock() == Blocks.FARMLAND) {
            hydrateFarmland(level, below, belowState);
        }
    }

    private static void hydrateFarmland(ServerLevel level, BlockPos pos, BlockState state) {
        var randomPos = pos.offset(level.random.nextIntBetweenInclusive(-1, 1), 0, level.random.nextIntBetweenInclusive(-1, 1));

        if (randomPos != pos) {
            pos = randomPos;
            state = level.getBlockState(pos);

            if (state.getBlock() != Blocks.FARMLAND) {
                return;
            }
        }

        if (state.getValue(FarmBlock.MOISTURE) < 7) {
            level.setBlockAndUpdate(pos, state.setValue(FarmBlock.MOISTURE, 7));
        }
    }

    protected void waterParticles(Level level, BlockPos pos, BlockState state) {
        if (level instanceof ServerLevel serverLevel) {
            double x = pos.getX() + 0.5 + level.random.nextGaussian() / 8f;
            double y = pos.getY();
            double z = pos.getZ() + 0.5 + level.random.nextGaussian() / 8f;
            var collisionShape = state.getCollisionShape(level, pos);
            if (!collisionShape.isEmpty()) {
                y += collisionShape.max(Direction.Axis.Y);
            }
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (level.random.nextBoolean()) {
                        serverLevel.sendParticles(ParticleTypes.RAIN, x + i * 0.33, y, z + j * 0.33, 2, 0, 0, 0, 0.2);
                    }
                }
            }
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new CapabilityProvider(stack, this.capacity);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(ClientExtensions.INSTANCE);
    }

    private static class CapabilityProvider extends FluidHandlerItemStack {
        public CapabilityProvider(@NotNull ItemStack container, int capacity) {
            super(container, capacity);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            return fluid.getFluid() == Fluids.WATER;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluid) {
            return false;
        }

        public void drain() {
            var contained = getFluid();
            var drainAmount = Math.min(contained.getAmount(), 1);

            var drained = contained.copy();
            drained.setAmount(drainAmount);

            contained.shrink(drainAmount);

            if (contained.isEmpty()) {
                setContainerToEmpty();
            } else {
                setFluid(contained);
            }
        }
    }

    private enum ClientExtensions implements IClientItemExtensions {
        INSTANCE;

        @Override
        public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
            if (player.isUsingItem()) {
                var useTicks = 72000 - 1 - player.getUseItemRemainingTicks();
                var step = useTicks + partialTick;
                var startProgress = easeOutCubic(Math.min(step, STARTUP_TIME) / STARTUP_TIME);

                poseStack.translate(-0.2 * startProgress, -0.2 * startProgress, 0);

                if (startProgress == 1.0f && isWatering) {
                    var sin = Mth.sin(0.35f * (step - 10f));
                    poseStack.rotateAround(Axis.XP.rotationDegrees(10 * sin), 0f, 0f, -0.2f);
                    //poseStack.translate(0, 0.2 * sin, 0);
                }

                var rotate = Mth.lerp(startProgress, 0, Mth.DEG_TO_RAD);

                poseStack.rotateAround(Axis.ZP.rotation(rotate * 15f), -0.75f, 0f, 0);

                int i = arm == HumanoidArm.RIGHT ? 1 : -1;
                poseStack.translate((float) i * 0.56F, -0.52F + (player.isUsingItem() ? 0 : equipProcess) * -0.6F, -0.72F);

                return true;
            }


            return false;
        }

        // https://easings.net/#easeOutCubic
        private static float easeOutCubic(float progress) {
            var opposite = 1 - progress;
            return 1 - opposite * opposite * opposite;
        }
    }
}
