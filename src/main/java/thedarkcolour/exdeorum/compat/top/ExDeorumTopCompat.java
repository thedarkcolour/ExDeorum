package thedarkcolour.exdeorum.compat.top;

import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class ExDeorumTopCompat implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe top) {
        top.registerProvider(new ExDeorumInfoProvider());

        return null;
    }
}
