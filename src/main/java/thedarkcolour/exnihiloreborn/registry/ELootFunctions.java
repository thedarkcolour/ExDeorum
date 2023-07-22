package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.loot.InfestedString;

public class ELootFunctions {
    public static final LootFunctionType INFESTED_STRING = register("infested_string", new LootFunctionType(new InfestedString.Serializer()));

    // Trigger classloading
    public static void init() {}

    private static LootFunctionType register(String name, LootFunctionType type) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(ExNihiloReborn.ID, name), type);
    }
}
