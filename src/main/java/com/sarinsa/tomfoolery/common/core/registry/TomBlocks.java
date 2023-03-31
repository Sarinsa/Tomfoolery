package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.block.OreOreBlock;
import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TomBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Tomfoolery.MODID);

    public static final Map<RegistryObject<? extends Block>, TagKey<Block>[]> BLOCK_TAGS = new HashMap<>();

    public static final RegistryObject<Block> ORE_ORE = registerBlock("ore_ore", CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new OreOreBlock(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_GRAY).strength(3.5F, 4.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), BlockTags.MINEABLE_WITH_PICKAXE);
    public static final RegistryObject<Block> CAKE_ORE = registerBlock("cake_ore", CreativeModeTab.TAB_BUILDING_BLOCKS, () -> new Block(BlockBehaviour.Properties.of(Material.STONE, MaterialColor.COLOR_BROWN).strength(1.8F, 1.0F).sound(SoundType.STONE).requiresCorrectToolForDrops()), BlockTags.MINEABLE_WITH_PICKAXE);


    private static <T extends Block>RegistryObject<T> registerBlock(String name, CreativeModeTab creativeTab, Supplier<T> blockSupplier) {
        RegistryObject<T> blockRegistryObject = BLOCKS.register(name, blockSupplier);
        TomItems.ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().tab(creativeTab)));
        return blockRegistryObject;
    }

    @SafeVarargs
    private static <T extends Block>RegistryObject<T> registerBlock(String name, CreativeModeTab creativeTab, Supplier<T> blockSupplier, TagKey<Block>... tagKeys) {
        RegistryObject<T> blockRegistryObject = BLOCKS.register(name, blockSupplier);
        TomItems.ITEMS.register(name, () -> new BlockItem(blockRegistryObject.get(), new Item.Properties().tab(creativeTab)));

        if (tagKeys != null && tagKeys.length > 0) {
            BLOCK_TAGS.put(blockRegistryObject, tagKeys);
        }
        return blockRegistryObject;
    }
}
