package com.sarinsa.dumbores.datagen.tag;

import com.sarinsa.dumbores.common.core.registry.DOBlocks;
import com.sarinsa.dumbores.common.core.DumbOres;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class DOBlockTagsProvider extends BlockTagsProvider {

    public DOBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, DumbOres.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(Tags.Blocks.ORES).add(DOBlocks.ORE_ORE.get());
        this.tag(Tags.Blocks.ORES).add(DOBlocks.CAKE_ORE.get());
    }
}
