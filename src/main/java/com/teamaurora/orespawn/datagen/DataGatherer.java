package com.teamaurora.orespawn.datagen;

import com.teamaurora.orespawn.common.core.Orespawn;
import com.teamaurora.orespawn.datagen.loot_table.OrespawnLootTableProvider;
import com.teamaurora.orespawn.datagen.recipe.DORecipeProvider;
import com.teamaurora.orespawn.datagen.tag.OrespawnBlockTagsProvider;
import com.teamaurora.orespawn.datagen.tag.OrespawnItemTagsProvider;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Orespawn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGatherer {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new OrespawnBlockTagsProvider(dataGenerator, existingFileHelper);
            dataGenerator.addProvider(blockTagsProvider);
            dataGenerator.addProvider(new OrespawnItemTagsProvider(dataGenerator, blockTagsProvider, existingFileHelper));
            dataGenerator.addProvider(new OrespawnLootTableProvider(dataGenerator));
            dataGenerator.addProvider(new DORecipeProvider(dataGenerator));
        }
        if (event.includeClient()) {

        }
    }
}
