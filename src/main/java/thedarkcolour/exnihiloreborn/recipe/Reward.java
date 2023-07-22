package thedarkcolour.exnihiloreborn.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class Reward {
    public static final Codec<Reward> CODEC = RecordCodecBuilder.create(builder -> builder.group(
        ItemStack.CODEC.fieldOf("item").forGetter(Reward::getItem),
        Codec.FLOAT.fieldOf("chance").forGetter(Reward::getChance)
    ).apply(builder, Reward::new));
    private final ItemStack item;
    private final float chance;

    public Reward(ItemStack item, float chance) {
        this.item = item;
        this.chance = chance;
    }

    public static Reward of(RegistryObject<Item> item) {
        return of(item, 1.0f);
    }

    public static Reward of(RegistryObject<Item> item, float chance) {
        return new Reward(new ItemStack(item.get()), chance);
    }

    public static Reward of(ItemLike item, float chance) {
        return new Reward(new ItemStack(item), chance);
    }

    public Reward(ItemLike item) {
        this(new ItemStack(item), 1.0f);
    }

    public static ImmutableList<Reward> withExtraChances(Supplier<Item> supplier, float[] chances) {
        Item item = supplier.get();
        ImmutableList.Builder<Reward> builder = ImmutableList.builder();
        builder.add(new Reward(item));

        for (float chance : chances) {
            builder.add(new Reward(new ItemStack(item), chance));
        }

        return builder.build();
    }

    public ItemStack getItem() {
        return item;
    }

    public float getChance() {
        return chance;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeItem(getItem());
        buffer.writeFloat(getChance());
    }

    public static Reward fromNetwork(FriendlyByteBuf buffer) {
        return new Reward(buffer.readItem(), buffer.readFloat());
    }
}
