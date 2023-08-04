package thedarkcolour.exnihiloreborn.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exnihiloreborn.recipe.RecipeUtil;
import thedarkcolour.exnihiloreborn.recipe.crucible.CrucibleRecipe;
import thedarkcolour.exnihiloreborn.registry.EBlockEntities;

public class WaterCrucibleBlockEntity extends AbstractCrucibleBlockEntity {
    public WaterCrucibleBlockEntity(BlockPos pos, BlockState state) {
        super(EBlockEntities.WATER_CRUCIBLE.get(), pos, state);
    }

    @Override
    protected CrucibleRecipe getRecipe(ItemStack item) {
        return RecipeUtil.getWaterCrucibleRecipe(item);
    }

    @Override
    protected Block getDefaultMeltBlock() {
        return Blocks.OAK_LEAVES;
    }
}
