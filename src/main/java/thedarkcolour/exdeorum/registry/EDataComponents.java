package thedarkcolour.exdeorum.registry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import thedarkcolour.exdeorum.ExDeorum;

import java.util.function.UnaryOperator;

public class EDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(BuiltInRegistries.DATA_COMPONENT_TYPE, ExDeorum.ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> WATERING_CAN = register("watering_can", builder -> builder
            .networkSynchronized(SimpleFluidContent.STREAM_CODEC)
            .persistent(SimpleFluidContent.CODEC)
    );

    private static <T> DeferredHolder<DataComponentType<?>, DataComponentType<T>> register(String path, UnaryOperator<DataComponentType.Builder<T>> configure) {
        return DATA_COMPONENTS.register(path, () -> configure.apply(new DataComponentType.Builder<>()).build());
    }
}
