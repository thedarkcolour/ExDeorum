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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.util.Lazy;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.helper.FluidHelper;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class AbstractCrucibleBlockEntity extends EBlockEntity {
    public static final Lazy<HashMap<Item, Block>> MELT_OVERRIDES = Lazy.concurrentOf(() -> {
        var map = new HashMap<Item, Block>();
        addMeltOverrides(map);
        return map;
    });

    public static final int MAX_SOLIDS = 1_000;

    private final AbstractCrucibleBlockEntity.ItemHandler item = new AbstractCrucibleBlockEntity.ItemHandler();
    private final AbstractCrucibleBlockEntity.FluidHandler tank = new AbstractCrucibleBlockEntity.FluidHandler();

    @Nullable
    private Block lastMelted;
    @Nullable
    private Fluid fluid = null;
    private short solids;

    public AbstractCrucibleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    // NBT
    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("Tank", this.tank.writeToNBT(new CompoundTag()));
        if (this.lastMelted != null) {
            nbt.putString("LastMelted", BuiltInRegistries.BLOCK.getKey(this.lastMelted).toString());
        }
        if (this.fluid != null) {
            nbt.putString("Fluid", BuiltInRegistries.FLUID.getKey(this.fluid).toString());
        }
        nbt.putShort("Solids", this.solids);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        this.tank.readFromNBT(nbt.getCompound("Tank"));
        this.lastMelted = BuiltInRegistries.BLOCK.get(new ResourceLocation(nbt.getString("LastMelted")));
        this.fluid = BuiltInRegistries.FLUID.get(new ResourceLocation(nbt.getString("Fluid")));
        this.solids = nbt.getShort("Solids");

        updateLight(this.level, this.worldPosition, this.fluid);
    }

    public static void updateLight(@Nullable Level level, BlockPos pos, Fluid fluid) {
        if (level != null) {
            var lightManager = level.getAuxLightManager(pos);

            if (lightManager != null) {
                lightManager.setLightAt(pos, fluid.getFluidType().getLightLevel());
            }
        }
    }

    @Override
    public void writeVisualData(FriendlyByteBuf buffer) {
        buffer.writeId(BuiltInRegistries.FLUID, this.tank.getFluid().getFluid());
        buffer.writeVarInt(this.tank.getFluidAmount());
        buffer.writeId(BuiltInRegistries.BLOCK, this.lastMelted != null ? this.lastMelted : Blocks.AIR);
        buffer.writeShort(this.solids);
    }

    @Override
    public void readVisualData(FriendlyByteBuf buffer) {
        Fluid fluid = buffer.readById(BuiltInRegistries.FLUID);
        if (fluid == null) {
            this.tank.setFluid(FluidStack.EMPTY);
            buffer.readVarInt();
        } else {
            this.tank.setFluid(new FluidStack(fluid, buffer.readVarInt()));
        }
        var lastMelted = buffer.readById(BuiltInRegistries.BLOCK);
        this.lastMelted = lastMelted == Blocks.AIR ? null : lastMelted;
        this.solids = buffer.readShort();

        // needed on client
        updateLight(this.level, this.worldPosition, this.tank.getFluid().getFluid());
    }

    @Override
    public void copyVisualData(BlockEntity fromIntegratedServer) {
        if (fromIntegratedServer instanceof AbstractCrucibleBlockEntity from) {
            this.tank.setFluid(from.tank.getFluid().copy());
            this.lastMelted = from.lastMelted;
            this.solids = from.solids;
            // needed on client
            updateLight(this.level, this.worldPosition, this.tank.getFluid().getFluid());
        }
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var playerItem = player.getItemInHand(hand);

        if (playerItem.getCapability(Capabilities.FluidHandler.ITEM) != null) {
            return FluidUtil.interactWithFluidHandler(player, hand, this.tank) ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
        }

        if (!level.isClientSide) {
            if (playerItem.getItem() == Items.GLASS_BOTTLE && this.getType() == EBlockEntities.WATER_CRUCIBLE.get() && EConfig.SERVER.allowWaterBottleTransfer.get()) {
                var fluid = new FluidStack(Fluids.WATER, 250);

                if (this.tank.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
                    BarrelBlockEntity.extractWaterBottle(this.tank, level, player, playerItem, fluid);
                    markUpdated();
                }
            } else if (canInsertItem(playerItem)) {
                tryMelt(playerItem, player.getAbilities().instabuild ? stack -> {} : stack -> stack.shrink(1));
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    // Gets a crucible recipe, using the cache if possible
    @Nullable
    protected abstract CrucibleRecipe getRecipe(ItemStack item);

    /**
     * Tries to melt the specified item into the crucible.
     *
     * @param item         Item to melt
     * @param shrinkAction What to do when item is melted
     */
    private void tryMelt(ItemStack item, Consumer<ItemStack> shrinkAction) {
        if (item.isEmpty()) return;

        var meltItem = item.getItem();
        var recipe = getRecipe(item);
        if (recipe == null) {
            this.item.setStackInSlot(0, ItemStack.EMPTY);
            return;
        }
        var result = recipe.getResult();
        var contained = this.tank.getFluid();
        shrinkAction.accept(item);
        this.solids = (short) Math.min(this.solids + result.getAmount(), MAX_SOLIDS);

        if (contained.isEmpty()) {
            this.fluid = result.getFluid();
            updateLight(this.level, this.worldPosition, this.fluid);
        }

        var melts = MELT_OVERRIDES.get();
        if (melts.containsKey(meltItem)) {
            this.lastMelted = melts.get(meltItem);
        } else if (meltItem.getClass() == BlockItem.class) {
            this.lastMelted = ((BlockItem) meltItem).getBlock();
        } else {
            // If we already have something else inside just use that instead of switching to default
            if (this.lastMelted == null) {
                this.lastMelted = getDefaultMeltBlock();
            }
        }

        markUpdated();
    }

    private boolean canInsertItem(ItemStack item) {
        if (item.isEmpty()) return false;

        var recipe = getRecipe(item);

        if (recipe != null) {
            var result = recipe.getResult();
            var contained = this.tank.getFluid();

            return (result.isFluidEqual(contained) || contained.isEmpty()) && result.getAmount() + this.solids <= MAX_SOLIDS;
        }

        return false;
    }

    public int getMeltingRate() {
        return 1;
    }

    public int getSolids() {
        return this.solids;
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public IItemHandler getItem() {
        return this.item;
    }

    public abstract Block getDefaultMeltBlock();

    @Nullable
    public Block getLastMelted() {
        return this.lastMelted;
    }

    private static void addMeltOverrides(HashMap<Item, Block> overrides) {
        overrides.put(Items.OAK_SAPLING, Blocks.OAK_LEAVES);
        overrides.put(Items.SPRUCE_SAPLING, Blocks.SPRUCE_LEAVES);
        overrides.put(Items.ACACIA_SAPLING, Blocks.ACACIA_LEAVES);
        overrides.put(Items.JUNGLE_SAPLING, Blocks.JUNGLE_LEAVES);
        overrides.put(Items.DARK_OAK_SAPLING, Blocks.DARK_OAK_LEAVES);
        overrides.put(Items.BIRCH_SAPLING, Blocks.BIRCH_LEAVES);
        overrides.put(Items.CHERRY_SAPLING, Blocks.CHERRY_LEAVES);
        overrides.put(Items.MANGROVE_PROPAGULE, Blocks.MANGROVE_LEAVES);
        overrides.put(Items.SWEET_BERRIES, Blocks.SPRUCE_LEAVES);
        overrides.put(Items.GLOW_BERRIES, Blocks.MOSS_BLOCK);
        overrides.put(EItems.GRASS_SEEDS.get(), Blocks.GRASS_BLOCK);
        overrides.put(EItems.MYCELIUM_SPORES.get(), Blocks.MYCELIUM);
        overrides.put(EItems.WARPED_NYLIUM_SPORES.get(), Blocks.WARPED_NYLIUM);
        overrides.put(EItems.CRIMSON_NYLIUM_SPORES.get(), Blocks.CRIMSON_NYLIUM);

        for (var sapling : BuiltInRegistries.BLOCK.entrySet()) {
            var item = sapling.getValue().asItem();

            if (!overrides.containsKey(item)) {
                var key = sapling.getKey().location();

                if (key.getPath().endsWith("sapling")) {
                    try {
                        overrides.put(item, BuiltInRegistries.BLOCK.get(new ResourceLocation(key.getNamespace(), key.getPath().replace("sapling", "leaves"))));
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    private static class FluidHandler extends FluidHelper {
        public FluidHandler() {
            super(4_000);
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return false;
        }
    }

    // inner class
    private class ItemHandler extends ItemStackHandler {
        @Override
        protected void onContentsChanged(int slot) {
            tryMelt(getItem(), item -> setStackInSlot(0, ItemStack.EMPTY));
        }

        @Override
        protected int getStackLimit(int slot, @NotNull ItemStack stack) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return canInsertItem(stack);
        }

        public ItemStack getItem() {
            return this.stacks.get(0);
        }
    }

    // Only ticks on server
    public static class Ticker implements BlockEntityTicker<AbstractCrucibleBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, AbstractCrucibleBlockEntity crucible) {
            // Update twice per tick
            if (!level.isClientSide) {
                if ((level.getGameTime() % 10L) == 0L) {
                    short delta = (short) Math.min(crucible.solids, crucible.getMeltingRate());

                    // Skip if no heat
                    if (delta <= 0) return;

                    if (crucible.tank.getSpace() >= delta) {
                        // Remove solids
                        crucible.solids -= delta;

                        // Add lava
                        if (crucible.tank.isEmpty()) {
                            if (crucible.fluid != null) {
                                crucible.tank.setFluid(new FluidStack(crucible.fluid, delta));
                                updateLight(level, pos, crucible.fluid);
                            }
                        } else {
                            crucible.tank.getFluid().grow(delta);
                        }

                        // Sync to client
                        crucible.markUpdated();
                    }
                }
                if (crucible instanceof WaterCrucibleBlockEntity && level.isRainingAt(pos.above())) {
                    BarrelBlockEntity.fillRainWater(crucible, crucible.tank);
                }
            }
        }
    }
}
