package thedarkcolour.exdeorum.transfer;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.transfer.ItemAccessResourceHandler;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.util.function.Function;

public class LegacyFluidItemAccessTransfer extends ItemAccessResourceHandler<FluidResource> {
    private final Function<ItemStack, IFluidHandlerItem> factory;

    public LegacyFluidItemAccessTransfer(ItemAccess itemAccess, Function<ItemStack, IFluidHandlerItem> factory) {
        super(itemAccess, 1);
        this.factory = factory;
    }

    @Override
    protected FluidResource getResourceFrom(ItemResource itemResource, int amount) {
        return FluidResource.of(getHandler(itemResource, amount).getFluidInTank(0));
    }

    @Override
    protected int getAmountFrom(ItemResource itemResource, int amount) {
        return getHandler(itemResource, amount).getFluidInTank(0).getAmount();
    }

    @Override
    protected ItemResource update(ItemResource itemResource, int amount, FluidResource resource, int resourceAmount) {
        var handler = getHandler(itemResource, amount);
        var current = handler.getFluidInTank(0);

        if (!current.isEmpty()) {
            handler.drain(current, IFluidHandler.FluidAction.EXECUTE);
        }
        if (!resource.isEmpty() && resourceAmount > 0) {
            handler.fill(resource.toStack(resourceAmount), IFluidHandler.FluidAction.EXECUTE);
        }

        return ItemResource.of(handler.getContainer());
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
        return !resource.isEmpty() && getHandler(this.itemAccess.getResource(), this.itemAccess.getAmount()).isFluidValid(0, resource.toStack(1));
    }

    @Override
    protected int getCapacity(int index, FluidResource resource) {
        return getHandler(this.itemAccess.getResource(), this.itemAccess.getAmount()).getTankCapacity(0);
    }

    private IFluidHandlerItem getHandler(ItemResource itemResource, int amount) {
        return this.factory.apply(itemResource.toStack(Math.max(amount, 1)));
    }
}
