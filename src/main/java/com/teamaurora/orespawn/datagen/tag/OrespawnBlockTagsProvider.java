package com.teamaurora.orespawn.datagen.tag;

import com.teamaurora.orespawn.common.core.Orespawn;
import com.teamaurora.orespawn.common.core.registry.OrespawnBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class OrespawnBlockTagsProvider extends BlockTagsProvider {

    public OrespawnBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, Orespawn.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Tags.Blocks.ORES).add(OrespawnBlocks.ORE_ORE.get());
        this.tag(Tags.Blocks.ORES).add(OrespawnBlocks.CAKE_ORE.get());
    }
}
