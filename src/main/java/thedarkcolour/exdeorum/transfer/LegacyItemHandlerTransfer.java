package thedarkcolour.exdeorum.transfer;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class LegacyItemHandlerTransfer extends SnapshotJournal<NonNullList<ItemStack>> implements ResourceHandler<ItemResource> {
    private final ItemStackHandler handler;

    public LegacyItemHandlerTransfer(ItemStackHandler handler) {
        this.handler = handler;
    }

    @Override
    public int size() {
        return this.handler.getSlots();
    }

    @Override
    public ItemResource getResource(int index) {
        return ItemResource.of(this.handler.getStackInSlot(index));
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.handler.getStackInSlot(index).getCount();
    }

    @Override
    public long getCapacityAsLong(int index, ItemResource resource) {
        return Math.min(this.handler.getSlotLimit(index), resource.getMaxStackSize());
    }

    @Override
    public boolean isValid(int index, ItemResource resource) {
        return !resource.isEmpty() && this.handler.isItemValid(index, resource.toStack());
    }

    @Override
    public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }

        updateSnapshots(transaction);
        var remainder = this.handler.insertItem(index, resource.toStack(amount), false);
        return amount - remainder.getCount();
    }

    @Override
    public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty() || amount <= 0 || index < 0 || index >= this.handler.getSlots()) {
            return 0;
        }

        var current = this.handler.getStackInSlot(index);
        if (current.isEmpty() || !resource.matches(current)) {
            return 0;
        }

        updateSnapshots(transaction);
        return this.handler.extractItem(index, amount, false).getCount();
    }

    @Override
    protected NonNullList<ItemStack> createSnapshot() {
        var snapshot = NonNullList.withSize(this.handler.getSlots(), ItemStack.EMPTY);
        for (int i = 0; i < this.handler.getSlots(); i++) {
            snapshot.set(i, this.handler.getStackInSlot(i).copy());
        }
        return snapshot;
    }

    @Override
    protected void revertToSnapshot(NonNullList<ItemStack> snapshot) {
        for (int i = 0; i < snapshot.size(); i++) {
            this.handler.setStackInSlot(i, snapshot.get(i).copy());
        }
    }
}
