package thedarkcolour.exnihiloreborn.blockentity;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;

public class LavaCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    // todo add KubeJS support for this
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
    public int getMeltingRate() {
        BlockState state = level.getBlockState(getBlockPos().below());

        return HEAT_REGISTRY.getInt(state.getBlock());
    }

    @Override
    protected CrucibleRecipe getRecipe(ItemStack item) {
        return RecipeUtil.getLavaCrucibleRecipe(item);
    }

    @Override
    protected Block getDefaultMeltBlock() {
        return Blocks.COBBLESTONE;
    }
}
