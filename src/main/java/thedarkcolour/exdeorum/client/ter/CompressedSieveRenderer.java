package thedarkcolour.exdeorum.client.ter;

import thedarkcolour.exdeorum.blockentity.EBlockEntity;
import thedarkcolour.exdeorum.blockentity.logic.SieveLogic;

// mesh y = 10 / 16
public class CompressedSieveRenderer<T extends EBlockEntity & SieveLogic.Owner> extends SieveRenderer<T> {
    public CompressedSieveRenderer(float meshHeight, float contentsMaxY) {
        super(meshHeight, contentsMaxY);
    }

    @Override
    protected boolean shouldContentsRender3d(T sieve) {
        return true;
    }
}
