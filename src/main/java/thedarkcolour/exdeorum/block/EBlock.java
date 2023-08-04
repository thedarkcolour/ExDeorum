package thedarkcolour.exdeorum.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import thedarkcolour.exdeorum.blockentity.EBlockEntity;

import java.util.function.Supplier;

public abstract class EBlock extends Block implements EntityBlock {
    private final Supplier<? extends BlockEntityType<?>> blockEntityType;

    public EBlock(Properties properties, Supplier<? extends BlockEntityType<?>> blockEntityType) {
        super(properties);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return blockEntityType.get().create(pos, state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        var blockEntity = level.getBlockEntity(pos);

        if (blockEntity instanceof EBlockEntity entity) {
            return entity.use(level, player, hand);
        }

        return InteractionResult.PASS;
    }

    public static void dropItem(Level level, BlockPos pos, ItemStack stack) {
        Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
    }
}
