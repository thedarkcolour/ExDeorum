package thedarkcolour.exnihiloreborn.data;

import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import thedarkcolour.exnihiloreborn.ExNihiloReborn;

import javax.annotation.Nullable;

public class EBlockTagsProvider extends BlockTagsProvider {
    public EBlockTagsProvider(DataGenerator gen, @Nullable ExistingFileHelper helper) {
        super(gen, ExNihiloReborn.ID, helper);
    }

    @Override
    protected void addTags() {
        super.addTags();
    }
}
