package com.sarinsa.tomfoolery.datagen.loot_mod;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomLootMods;
import com.sarinsa.tomfoolery.common.loot_mods.SimpleAddLootModifier;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import java.util.List;

public class TomLootModsProvider extends GlobalLootModifierProvider {

    public TomLootModsProvider(DataGenerator gen) {
        super(gen, Tomfoolery.MODID);
    }

    @Override
    protected void start() {
        add("add_launcher", new SimpleAddLootModifier(
                new LootItemCondition[] {},
                TomItems.GRENADE_LAUNCHER.get(),
                0.04,
                1,
                1,
                List.of(
                        new ResourceLocation("chests/abandoned_mineshaft"),
                        new ResourceLocation("chests/bastion_treasure"),
                        new ResourceLocation("chests/simple_dungeon")
                )
        ));
        add("add_launcher_igloo", new SimpleAddLootModifier(
                new LootItemCondition[] {},
                TomItems.GRENADE_LAUNCHER.get(),
                0.25,
                1,
                1,
                List.of(new ResourceLocation("chests/igloo_chest"))
        ));
    }
}
