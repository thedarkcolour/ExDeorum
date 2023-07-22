package thedarkcolour.exnihiloreborn.blockentity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class WaterCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    public static final Cache<Pair<Item, CompoundNBT>, CrucibleRecipe> RECIPES_CACHE = CacheBuilder.newBuilder().maximumSize(12).build();

    public WaterCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.WATER_CRUCIBLE.get(), pos, state);
    }

    @Override
    public CrucibleRecipe getRecipe(ItemStack item) {
        return super.getRecipe(item);
    }

    @Override
    protected RecipeType<CrucibleRecipe> getRecipeType() {
        return ERecipeTypes.WATER_CRUCIBLE;
    }

    @Override
    protected Block getDefaultMeltBlock() {
        return Blocks.OAK_LEAVES;
    }

    @Override
    protected Cache<CacheKey, CrucibleRecipe> getRecipeCache() {
        return RECIPES_CACHE;
    }
}
