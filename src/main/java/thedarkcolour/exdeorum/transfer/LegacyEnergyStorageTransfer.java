package thedarkcolour.exdeorum.transfer;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

import java.util.function.IntConsumer;

public class LegacyEnergyStorageTransfer extends SnapshotJournal<Integer> implements EnergyHandler {
    private final IEnergyStorage storage;
    private final IntConsumer restore;

    public LegacyEnergyStorageTransfer(IEnergyStorage storage, IntConsumer restore) {
        this.storage = storage;
        this.restore = restore;
    }

    @Override
    public long getAmountAsLong() {
        return this.storage.getEnergyStored();
    }

    @Override
    public long getCapacityAsLong() {
        return this.storage.getMaxEnergyStored();
    }

    @Override
    public int insert(int amount, TransactionContext transaction) {
        if (amount <= 0) {
            return 0;
        }

        updateSnapshots(transaction);
        return this.storage.receiveEnergy(amount, false);
    }

    @Override
    public int extract(int amount, TransactionContext transaction) {
        if (amount <= 0) {
            return 0;
        }

        updateSnapshots(transaction);
        return this.storage.extractEnergy(amount, false);
    }

    @Override
    protected Integer createSnapshot() {
        return this.storage.getEnergyStored();
    }

    @Override
    protected void revertToSnapshot(Integer snapshot) {
        this.restore.accept(snapshot);
    }
}
