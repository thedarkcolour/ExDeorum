package thedarkcolour.exnihiloreborn.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class GrassSpreaderItem extends Item {
    private final Supplier<BlockState> grassState;

    public GrassSpreaderItem(Properties pProperties, Supplier<BlockState> grassState) {
        super(pProperties);
        this.grassState = grassState;
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        var level = ctx.getLevel();
        var pos = ctx.getClickedPos();
        var player = ctx.getPlayer();

        if (level.getBlockState(pos).is(Blocks.DIRT)) {
            if (!level.isClientSide) {
                level.setBlock(pos, grassState.get(), 3);

                return InteractionResult.CONSUME;
            }

            // apparently shrinking is done on both sides?
            if (player == null || !player.getAbilities().instabuild) {
                ctx.getItemInHand().shrink(1);
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }
}
