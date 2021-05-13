package com.teamaurora.orespawn.datagen.tag;

import com.teamaurora.orespawn.common.core.Orespawn;
import com.teamaurora.orespawn.common.core.registry.OrespawnBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class OrespawnItemTagsProvider extends ItemTagsProvider {

    public OrespawnItemTagsProvider(DataGenerator dataGenerator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagsProvider, Orespawn.MODID, existingFileHelper);
    }

    protected void addTags() {
        this.tag(Tags.Items.ORES).add(OrespawnBlocks.ORE_ORE.get().asItem());
        this.tag(Tags.Items.ORES).add(OrespawnBlocks.CAKE_ORE.get().asItem());
    }
}
