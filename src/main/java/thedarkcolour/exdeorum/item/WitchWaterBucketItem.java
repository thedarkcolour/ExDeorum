package thedarkcolour.exdeorum.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;
import org.jetbrains.annotations.Nullable;
import thedarkcolour.exdeorum.registry.EFluids;

public class WitchWaterBucketItem extends BucketItem {
    public WitchWaterBucketItem(Properties properties) {
        super(EFluids.WITCH_WATER_STILL, properties);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }
}
