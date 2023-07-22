package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import thedarkcolour.exnihiloreborn.recipe.Reward;
import thedarkcolour.exnihiloreborn.recipe.sieve.AbstractSieveRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.EItems;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class SieveBlockEntity extends EBlockEntity {
    public static final short MAX_SIEVE_CAPACITY = 100;
    public static final short SIEVE_INTERVAL = 10;

    private final SieveBlockEntity.ItemHandler item = new SieveBlockEntity.ItemHandler();
    private ItemStack mesh = ItemStack.EMPTY;
    private short progress = 0; // Max is 100

    // Does not persist in NBT, just a cache
    // todo invalidate on /reload
    private List<? extends AbstractSieveRecipe> currentRecipe = Collections.emptyList();

    public SieveBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.SIEVE.get(), pos, state);
    }

    // Capabilities
    private final LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> item);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return itemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);

        nbt.put("item", item.serializeNBT());
        nbt.putShort("Progress", progress);
        if (!mesh.isEmpty()) {
            nbt.put("mesh", mesh.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        item.deserializeNBT(nbt.getCompound("item"));
        progress = nbt.getShort("progress");
        if (nbt.contains("mesh")) {
            mesh = ItemStack.of(nbt.getCompound("Mesh"));
        }

        super.load(nbt);
    }

    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack playerItem = player.getItemInHand(hand);
        boolean isClientSide = level.isClientSide;

        // Try insert mesh
        if (mesh.isEmpty()) {
            if (isMesh(playerItem)) {
                mesh = playerItem.copy();
                mesh.setCount(1);

                // Remove stack
                if (!player.isCreative()) {
                    player.setItemInHand(hand, ItemStack.EMPTY);
                }
            }
        }

        if (progress == 0) {
            // Insert an item
            if (!isClientSide) {
                // Check against cached recipe
                if (!currentRecipe.isEmpty()) {
                    for (AbstractSieveRecipe recipe : currentRecipe) {
                        if (!recipe.test(mesh.getItem(), playerItem)) {
                            return InteractionResult.CONSUME;
                        }
                    }

                    player.setItemInHand(hand, fillWithItem(playerItem));
                    markUpdated();
                } else if (!(currentRecipe = getResults(playerItem)).isEmpty()) {
                    player.setItemInHand(hand, fillWithItem(playerItem));
                    markUpdated();
                }
            }
        } else {
            // todo mesh efficiency
            progress -= SIEVE_INTERVAL;

            if (progress <= 0) {
                progress = 0;

                if (!isClientSide) {
                    giveItems();
                }
            }
        }

        return InteractionResult.sidedSuccess(isClientSide);
    }

    // Consumes an item and fills the sieve.
    private ItemStack fillWithItem(ItemStack stack) {
        progress = MAX_SIEVE_CAPACITY;

        return item.insertItem(0, stack, false);
    }

    private void giveItems() {
        var pos = getBlockPos();
        var rand = level.random;

        for (AbstractSieveRecipe recipe : currentRecipe) {
            for (Reward reward : recipe.getRewards()) {
                if (rand.nextFloat() < reward.getChance()) {
                    var itemEntity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, reward.getItem().copy());
                    itemEntity.setDeltaMovement(rand.nextGaussian() * 0.05, 0.2, rand.nextGaussian() * 0.05);
                    level.addFreshEntity(itemEntity);
                }
            }
        }

        item.setStackInSlot(0, ItemStack.EMPTY);
        markUpdated();
    }

    private boolean isMesh(ItemStack stack) {
        var item = stack.getItem();
        return item == EItems.STRING_MESH.get() || item == EItems.FLINT_MESH.get() || item == EItems.IRON_MESH.get() || item == EItems.DIAMOND_MESH.get() || item == EItems.NETHERITE_MESH.get();
    }

    public short getProgress() {
        return progress;
    }

    public ItemStack getItem() {
        return item.getStackInSlot(0);
    }

    public List<? extends AbstractSieveRecipe> getResults(ItemStack stack) {
        return RecipeUtil.getSieveResults(level.getServer(), getRecipeType(), mesh, stack);
    }

    public RecipeType<? extends AbstractSieveRecipe> getRecipeType() {
        return ERecipeTypes.SIEVE;
    }

    private class ItemHandler extends ItemStackHandler {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return !getResults(stack).isEmpty();
        }

        @Override
        protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
            return 1;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return ItemStack.EMPTY;
        }
    }
}
