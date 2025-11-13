package com.sarinsa.tomfoolery.datagen;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomDamageTypes;
import com.sarinsa.tomfoolery.datagen.loot_mod.TomLootModsProvider;
import com.sarinsa.tomfoolery.datagen.loot_table.TomLootTableProvider;
import com.sarinsa.tomfoolery.datagen.recipe.TomRecipeProvider;
import com.sarinsa.tomfoolery.datagen.tag.TomBlockTagsProvider;
import com.sarinsa.tomfoolery.datagen.tag.TomItemTagsProvider;
import com.sarinsa.tomfoolery.datagen.tag.TomPlacedFeatureTagsProvider;
import com.sarinsa.tomfoolery.datagen.worldgen.TomFeatureProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber( modid = Tomfoolery.MODID, bus = Mod.EventBusSubscriber.Bus.MOD )
public class DataGatherer {
    
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add( Registries.DAMAGE_TYPE, TomDamageTypes::bootstrap )
            .add( Registries.CONFIGURED_FEATURE, TomFeatureProvider::bootstrapConfigured )
            .add( Registries.PLACED_FEATURE, TomFeatureProvider::bootstrapPlaced );
    
    
    @SubscribeEvent
    public static void onGatherData( GatherDataEvent event ) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        
        if( event.includeServer() ) {
            DatapackBuiltinEntriesProvider builtInProvider =
                    dataGenerator.addProvider( true, new DatapackBuiltinEntriesProvider( dataGenerator.getPackOutput(), lookupProvider, BUILDER, Set.of( Tomfoolery.MODID ) ) );
            
            
            BlockTagsProvider blockTagsProvider =
                    dataGenerator.addProvider( true, new TomBlockTagsProvider( dataGenerator, lookupProvider, fileHelper ) );
            dataGenerator.addProvider( true, new TomItemTagsProvider( dataGenerator, lookupProvider, blockTagsProvider.contentsGetter(), fileHelper ) );
            dataGenerator.addProvider( true, new TomPlacedFeatureTagsProvider( dataGenerator, builtInProvider.getRegistryProvider(), fileHelper ) );
            
            dataGenerator.addProvider( true, new TomLootTableProvider( dataGenerator ) );
            dataGenerator.addProvider( true, new TomRecipeProvider( dataGenerator ) );
            dataGenerator.addProvider( true, new TomLootModsProvider( dataGenerator ) );
        }
    }
}
