package thedarkcolour.exdeorum.registry;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.loot.CrookLootModifier;
import thedarkcolour.exdeorum.loot.HammerLootModifier;

public class EGlobalLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExDeorum.ID);

    public static final RegistryObject<Codec<CrookLootModifier>> CROOK = GLOBAL_LOOT_MODIFIERS.register("crook", () -> CrookLootModifier.CODEC);
    public static final RegistryObject<Codec<HammerLootModifier>> HAMMER = GLOBAL_LOOT_MODIFIERS.register("hammer", () -> HammerLootModifier.CODEC);
}
