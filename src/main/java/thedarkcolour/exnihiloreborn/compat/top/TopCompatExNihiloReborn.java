package thedarkcolour.exnihiloreborn.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class TopCompatExNihiloReborn implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe top) {
        top.registerProvider(new InfestedLeavesInfoProvider());

        return null;
    }
}
