package thedarkcolour.exdeorum.compat.top;

import mcjty.theoneprobe.api.CompoundText;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import mcjty.theoneprobe.api.TextStyleClass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import thedarkcolour.exdeorum.ExDeorum;
import thedarkcolour.exdeorum.blockentity.AbstractCrucibleBlockEntity;
import thedarkcolour.exdeorum.blockentity.BarrelBlockEntity;
import thedarkcolour.exdeorum.blockentity.InfestedLeavesBlockEntity;
import thedarkcolour.exdeorum.registry.EBlocks;

public class ExDeorumInfoProvider implements IProbeInfoProvider {
    @Override
    public ResourceLocation getID() {
        return new ResourceLocation(ExDeorum.ID, "info_provider");
    }

    @Override
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo info, Player playerEntity, Level level, BlockState state, IProbeHitData data) {
        var te = level.getBlockEntity(data.getPos());

        if (state.getBlock() == EBlocks.INFESTED_LEAVES.get()) {
            if (te instanceof InfestedLeavesBlockEntity) {
                int progress = (int) (((InfestedLeavesBlockEntity) te).getProgress() * 100.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Progress: ").style(TextStyleClass.WARNING).text(progress + "%"));
            }
        } else if (te instanceof BarrelBlockEntity barrel) {
            short volume = barrel.compost;

            if (volume == 1000 || barrel.isBrewing()) {
                int progress = (int) (barrel.progress * 100.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Progress: ").style(TextStyleClass.WARNING).text(progress + "%"));
            } else if (volume > 0) {
                int volumePercent = (int) (volume / 10.0f);

                info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Volume: ").style(TextStyleClass.WARNING).text(volumePercent + "%"));
            } else if (barrel.isBurning()) {
                int progress = 300 - (int) (barrel.progress * 300.0f);

                info.text(CompoundText.create().style(TextStyleClass.ERROR).text("Burning! ").style(TextStyleClass.WARNING).text(progress / 20 + "s"));
            }
        } else if (te instanceof AbstractCrucibleBlockEntity crucible) {
            info.text(CompoundText.create().style(TextStyleClass.LABEL).text("Rate: ").style(TextStyleClass.WARNING).text(crucible.getMeltingRate() + "x"));
        }
    }
}
