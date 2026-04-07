package thedarkcolour.exdeorum.transfer;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class LegacyFluidTankTransfer extends SnapshotJournal<FluidStack> implements ResourceHandler<FluidResource> {
    private final FluidTank tank;

    public LegacyFluidTankTransfer(FluidTank tank) {
        this.tank = tank;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public FluidResource getResource(int index) {
        return FluidResource.of(this.tank.getFluid());
    }

    @Override
    public long getAmountAsLong(int index) {
        return this.tank.getFluidAmount();
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
        return this.tank.getCapacity();
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return !resource.isEmpty() && this.tank.isFluidValid(resource.toStack(1));
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }

        updateSnapshots(transaction);
        return this.tank.fill(resource.toStack(amount), FluidTank.FluidAction.EXECUTE);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
        if (resource.isEmpty() || amount <= 0) {
            return 0;
        }

        updateSnapshots(transaction);
        return this.tank.drain(resource.toStack(amount), FluidTank.FluidAction.EXECUTE).getAmount();
    }

    @Override
    protected FluidStack createSnapshot() {
        return this.tank.getFluid().copy();
    }

    @Override
    protected void revertToSnapshot(FluidStack snapshot) {
        this.tank.setFluid(snapshot.copy());
    }
}
