package com.sarinsa.tomfoolery.datagen.tag;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class TomBlockTagsProvider extends BlockTagsProvider {

    public TomBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, Tomfoolery.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Tags.Blocks.ORES).add(TomBlocks.ORE_ORE.get());
        this.tag(Tags.Blocks.ORES).add(TomBlocks.CAKE_ORE.get());
    }
}
