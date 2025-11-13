package com.sarinsa.tomfoolery.datagen.tag;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.tags.TomTags;
import com.sarinsa.tomfoolery.datagen.worldgen.TomFeatureProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TomPlacedFeatureTagsProvider extends TagsProvider<PlacedFeature> {
    
    public TomPlacedFeatureTagsProvider( DataGenerator dataGen, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper ) {
        super( dataGen.getPackOutput(), Registries.PLACED_FEATURE, lookupProvider, Tomfoolery.MODID, existingFileHelper );
    }
    
    @Override
    protected void addTags( HolderLookup.Provider holderLookup ) {
        try {
            // Overworld ore features
            addAll( TomTags.PlacedFeatures.OVERWORLD_ORES,
                    List.of(
                            TomFeatureProvider.PLACED_CAKE_ORE,
                            TomFeatureProvider.PLACED_ORE_ORE
                    )
            );
            
            // Ore features
            addTags( TomTags.PlacedFeatures.ORES,
                    TomTags.PlacedFeatures.OVERWORLD_ORES
            );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    /** Add all features in a list to a tag. */
    @SuppressWarnings( "SameParameterValue" )
    protected void addAll( TagKey<PlacedFeature> tagKey, List<ResourceKey<PlacedFeature>> features ) {
        final TagAppender<PlacedFeature> tag = tag( tagKey );
        features.forEach( tag::add );
    }
    
    /** Add all tags to another. */
    @SafeVarargs
    protected final void addTags( TagKey<PlacedFeature> tagKey, TagKey<PlacedFeature>... tagsToAdd ) {
        final TagAppender<PlacedFeature> tag = tag( tagKey );
        for( TagKey<PlacedFeature> tagToAdd : tagsToAdd ) tag.addTag( tagToAdd );
    }
}
