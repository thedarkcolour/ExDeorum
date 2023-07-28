package thedarkcolour.exnihiloreborn.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;
import thedarkcolour.exnihiloreborn.loot.CrookLootModifier;
import thedarkcolour.exnihiloreborn.loot.HammerLootModifier;

public class EGlobalLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExNihiloReborn.ID);

    public static final RegistryObject<Codec<CrookLootModifier>> CROOK = GLOBAL_LOOT_MODIFIERS.register("crook", () -> CrookLootModifier.CODEC);
    public static final RegistryObject<Codec<HammerLootModifier>> HAMMER = GLOBAL_LOOT_MODIFIERS.register("hammer", () -> HammerLootModifier.CODEC);
}
