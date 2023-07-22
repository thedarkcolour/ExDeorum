package thedarkcolour.exnihiloreborn.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.Tags;
import thedarkcolour.exnihiloreborn.registry.EItems;

// Silk worms have a 1 in 100 chance to drop from regular leaves, 1 in 15 if the block is infested
// Infested leaves have a 1 in 4 * progress to drop 1 string
// Infested leaves have a 1 in 16 * progress to drop another string
public class CrookItem extends Item {
    public CrookItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(ItemStack tool, ItemStack material) {
        if (this == EItems.BONE_CROOK.get()) {
            return material.getItem().is(Tags.Items.BONES);
        } else {
            return material.getItem().is(ItemTags.PLANKS);
        }
    }

    // Pulls the entity towards the player like a cartoon hook
    @Override
    public ActionResultType interactLivingEntity(ItemStack stack, PlayerEntity playerIn, LivingEntity living, Hand hand) {
        Vector3d difference = playerIn.position().subtract(living.position());
        double distance = Math.sqrt(Entity.getHorizontalDistanceSqr(difference));

        double scalarX = difference.x / distance;
        double scalarZ = difference.z / distance;

        double dx = scalarX * 1.5;
        double dz = scalarZ * 1.5;

        living.setDeltaMovement(living.getDeltaMovement().add(dx, 0.0, dz));

        return ActionResultType.SUCCESS;
    }
}
