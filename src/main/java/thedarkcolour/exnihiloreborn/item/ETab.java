package thedarkcolour.exnihiloreborn.item;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.registry.EItems;

public final class ETab extends ItemGroup {
    public static final ETab INSTANCE = new ETab();

    private ETab() {
        super(ExNihiloReborn.ID);
    }

    @Override
    public ItemStack makeIcon() {
        return new ItemStack(EItems.CROOK.get());
    }
}