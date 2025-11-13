package com.sarinsa.tomfoolery.datagen.tag;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.tags.TomTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class TomBlockTagsProvider extends BlockTagsProvider {
    
    public TomBlockTagsProvider( DataGenerator dataGenerator, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper ) {
        super( dataGenerator.getPackOutput(), lookupProvider, Tomfoolery.MODID, existingFileHelper );
    }
    
    @Override
    protected void addTags( HolderLookup.Provider provider ) {
        tag( TomTags.Blocks.ORES ).add(
                TomBlocks.ORE_ORE.get(),
                TomBlocks.CAKE_ORE.get()
        );
        tag( Tags.Blocks.ORES ).addTag( TomTags.Blocks.ORES );
        
        TomBlocks.BLOCK_TAGS.forEach( ( regObj, tagKeys ) -> {
            for( TagKey<Block> tagKey : tagKeys ) {
                tag( tagKey ).add( regObj.get() );
            }
        } );
    }
}
