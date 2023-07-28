package thedarkcolour.exnihiloreborn.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.loot.InfestedStringCount;

public class ELootFunctions {
    public static final DeferredRegister<LootItemFunctionType> LOOT_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, ExNihiloReborn.ID);
    public static final RegistryObject<LootItemFunctionType> INFESTED_STRING = LOOT_FUNCTIONS.register("infested_string", () -> new LootItemFunctionType(new InfestedStringCount.LootSerializer()));
}
