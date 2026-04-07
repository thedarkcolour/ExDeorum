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
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
import net.neoforged.neoforge.transfer.access.ItemAccess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.blockentity.helper.FluidHelper;
import thedarkcolour.exdeorum.config.EConfig;
import thedarkcolour.exdeorum.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exdeorum.registry.EBlockEntities;
import thedarkcolour.exdeorum.registry.EItems;

import java.util.HashMap;
import java.util.function.Consumer;

public abstract class AbstractCrucibleBlockEntity extends ETankBlockEntity {
    // todo replace
    public static final Lazy<HashMap<Item, Block>> MELT_OVERRIDES = Lazy.of(() -> {
        var map = new HashMap<Item, Block>();
        addMeltOverrides(map);
        return map;
    });

    public static final int MAX_SOLIDS = 1000;
    public static final int MAX_FLUID_CAPACITY = 4000;

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
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);

        this.tank.serialize(output.child("tank"));
        if (this.lastMelted != null) {
            output.putString("lastMelted", BuiltInRegistries.BLOCK.getKey(this.lastMelted).toString());
        }
        if (this.fluid != null) {
            output.putString("fluid", BuiltInRegistries.FLUID.getKey(this.fluid).toString());
        }
        output.putShort("solids", this.solids);
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);

        this.tank.deserialize(input.childOrEmpty("tank"));
        this.lastMelted = input.getString("lastMelted")
                .map(Identifier::parse)
                .flatMap(id -> BuiltInRegistries.BLOCK.get(id).map(reference -> reference.value()))
                .orElse(null);
        this.fluid = input.getString("fluid")
                .map(Identifier::parse)
                .flatMap(id -> BuiltInRegistries.FLUID.get(id).map(reference -> reference.value()))
                .orElse(null);
        this.solids = (short) input.getShortOr("solids", (short) 0);

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
    public void writeVisualData(RegistryFriendlyByteBuf buffer) {
        buffer.writeById(BuiltInRegistries.FLUID::getId, this.tank.getFluid().getFluid());
        buffer.writeVarInt(this.tank.getFluidAmount());
        buffer.writeById(BuiltInRegistries.BLOCK::getId, this.lastMelted != null ? this.lastMelted : Blocks.AIR);
        buffer.writeShort(this.solids);
    }

    @Override
    public void readVisualData(RegistryFriendlyByteBuf buffer) {
        var fluid = buffer.readById(BuiltInRegistries.FLUID::byId);
        this.tank.setFluid(new FluidStack(fluid, buffer.readVarInt()));
        var lastMelted = buffer.readById(BuiltInRegistries.BLOCK::byId);
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

    @Override
    public InteractionResult useItemOn(Level level, Player player, ItemStack stack, InteractionHand hand) {
        var playerItem = player.getItemInHand(hand);

        if (getFluidHandler(playerItem) != null) {
            return FluidUtil.interactWithFluidHandler(player, hand, this.tank) ? InteractionResult.SUCCESS : InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (playerItem.getItem() == Items.GLASS_BOTTLE && this.getType() == EBlockEntities.WATER_CRUCIBLE.get() && EConfig.SERVER.allowWaterBottleTransfer.get()) {
            var fluid = new FluidStack(Fluids.WATER, 250);

            if (this.tank.drain(fluid, IFluidHandler.FluidAction.SIMULATE).getAmount() == 250) {
                if (!level.isClientSide()) {
                    BarrelBlockEntity.extractWaterBottle(this.tank, level, player, playerItem, fluid);
                    markUpdated();
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            var result = canInsertItem(playerItem);

            if (result == InsertionResult.YES) {
                if (tryMelt(playerItem, player.getAbilities().instabuild ? playerStack -> {} : playerStack -> playerStack.shrink(1))) {
                    return InteractionResult.SUCCESS;
                }
            } else if (result == InsertionResult.FULL) {
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.TRY_WITH_EMPTY_HAND;
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
    private boolean tryMelt(ItemStack item, Consumer<ItemStack> shrinkAction) {
        if (item.isEmpty()) return false;

        var meltItem = item.getItem();
        var recipe = getRecipe(item);
        if (recipe == null) {
            this.item.setStackInSlot(0, ItemStack.EMPTY);
            return false;
        }
        if (this.level != null && this.level.isClientSide()) {
            return true;
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

        return true;
    }

    private InsertionResult canInsertItem(ItemStack item) {
        if (item.isEmpty()) return InsertionResult.NO;

        var recipe = getRecipe(item);

        if (recipe != null) {
            var result = recipe.getResult();
            var contained = this.tank.getFluid();

            if (FluidStack.isSameFluidSameComponents(result, contained) || contained.isEmpty()) {
                return result.getAmount() + this.solids <= MAX_SOLIDS ? InsertionResult.YES : InsertionResult.FULL;
            }
        }

        return InsertionResult.NO;
    }

    public abstract int getMeltingRate();

    public int getSolids() {
        return this.solids;
    }

    @Override
    public FluidTank getTank() {
        return this.tank;
    }

    public ItemStackHandler getItem() {
        return this.item;
    }

    private static IFluidHandler getFluidHandler(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }

        var handler = stack.getCapability(Capabilities.Fluid.ITEM, ItemAccess.forStack(stack));
        return handler == null ? null : IFluidHandler.of(handler);
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
                var key = sapling.getKey().identifier();

                if (key.getPath().endsWith("sapling")) {
                    try {
                        var leaves = BuiltInRegistries.BLOCK.get(Identifier.fromNamespaceAndPath(key.getNamespace(), key.getPath().replace("sapling", "leaves")))
                                .map(reference -> reference.value())
                                .orElse(Blocks.AIR);
                        if (leaves != Blocks.AIR) {
                            overrides.put(item, leaves);
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }
    }

    private static class FluidHandler extends FluidHelper {
        public FluidHandler() {
            super(MAX_FLUID_CAPACITY);
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
            return canInsertItem(stack) == InsertionResult.YES;
        }

        public ItemStack getItem() {
            return this.stacks.getFirst();
        }
    }

    // Only ticks on server
    public static class Ticker implements BlockEntityTicker<AbstractCrucibleBlockEntity> {
        @Override
        public void tick(Level level, BlockPos pos, BlockState state, AbstractCrucibleBlockEntity crucible) {
            // Update twice per second
            if (!level.isClientSide()) {
                var tank = crucible.tank;

                if ((level.getGameTime() % 10L) == 0L) {
                    short delta = (short) Math.min(crucible.solids, crucible.getMeltingRate());

                    // Skip if no heat
                    if (delta <= 0) return;

                    if (tank.getSpace() >= delta) {
                        // Remove solids
                        crucible.solids -= delta;

                        // Add lava
                        if (tank.isEmpty()) {
                            if (crucible.fluid != null) {
                                tank.setFluid(new FluidStack(crucible.fluid, delta));
                                updateLight(level, pos, crucible.fluid);
                            }
                        } else {
                            tank.getFluid().grow(delta);
                        }

                        // Sync to client
                        crucible.markUpdated();
                    }
                }
                if (EConfig.SERVER.cruciblesCollectRainWater.get() && tank.getFluidAmount() < MAX_FLUID_CAPACITY && crucible instanceof WaterCrucibleBlockEntity && level.isRainingAt(pos.above())) {
                    BarrelBlockEntity.fillRainWater(crucible, tank);
                }
            }
        }
    }

    private enum InsertionResult {
        NO, YES, FULL
    }
}
