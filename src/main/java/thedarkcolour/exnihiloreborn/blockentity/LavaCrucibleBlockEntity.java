package thedarkcolour.exnihiloreborn.blockentity;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;
import thedarkcolour.exnihiloreborn.registry.ERecipeTypes;

public class LavaCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    public static final Cache<CacheKey, CrucibleRecipe> RECIPES_CACHE = CacheBuilder.newBuilder().maximumSize(12).build();

    // todo split this off somewhere else
    private static final Object2IntMap<Block> HEAT_REGISTRY = new Object2IntOpenHashMap<>();

    static {
        HEAT_REGISTRY.put(Blocks.TORCH, 1);
        HEAT_REGISTRY.put(Blocks.WALL_TORCH, 1);
        HEAT_REGISTRY.put(Blocks.SOUL_TORCH, 2);
        HEAT_REGISTRY.put(Blocks.SOUL_WALL_TORCH, 2);
        HEAT_REGISTRY.put(Blocks.LAVA, 3);
        HEAT_REGISTRY.put(Blocks.FIRE, 5);
        HEAT_REGISTRY.put(Blocks.SOUL_FIRE, 7);
    }


    public LavaCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.LAVA_CRUCIBLE.get(), pos, state);
    }

    @Override
    public int getMelt() {
        BlockState state = level.getBlockState(getBlockPos().below());

        return HEAT_REGISTRY.getInt(state.getBlock());
    }

    @Override
    protected RecipeType<CrucibleRecipe> getRecipeType() {
        return ERecipeTypes.LAVA_CRUCIBLE;
    }

    @Override
    protected Block getDefaultMeltBlock() {
        return Blocks.COBBLESTONE;
    }

    @Override
    protected Cache<CacheKey, CrucibleRecipe> getRecipeCache() {
        return RECIPES_CACHE;
    }
}
