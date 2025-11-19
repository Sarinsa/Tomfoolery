package com.sarinsa.tomfoolery.datagen.loot_table;

import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.tags.TomTags;
import fathertoast.crust.api.datagen.loot.LootEntryTagBuilder;
import fathertoast.crust.api.datagen.loot.LootPoolBuilder;
import fathertoast.crust.api.datagen.loot.LootTableBuilder;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.Tags;

import java.util.HashSet;
import java.util.Set;

public class TomBlockLootTables extends BlockLootSubProvider {
    
    // Vanilla
    private static final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches( ItemPredicate.Builder.item().hasEnchantment( new EnchantmentPredicate( Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast( 1 ) ) ) );
    private static final LootItemCondition.Builder NO_SILK_TOUCH = SILK_TOUCH.invert();
    private static final LootItemCondition.Builder SHEARS = MatchTool.toolMatches( ItemPredicate.Builder.item().of( Tags.Items.SHEARS ) );
    private static final LootItemCondition.Builder SILK_TOUCH_OR_SHEARS = SHEARS.or( SILK_TOUCH );
    private static final LootItemCondition.Builder NOT_SILK_TOUCH_OR_SHEARS = SILK_TOUCH_OR_SHEARS.invert();
    private static final float[] DEFAULT_SAPLING_DROP_RATES = new float[] { 0.05F, 0.0625F, 0.083333336F, 0.1F };
    private static final float[] RARE_SAPLING_DROP_RATES = new float[] { 0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F };
    
    private final Set<Block> knownBlocks = new HashSet<>();
    
    
    protected TomBlockLootTables( Set<Item> set, FeatureFlagSet featureFlagSet ) {
        super( set, featureFlagSet );
    }
    
    @Override
    protected void add( Block block, LootTable.Builder table ) {
        super.add( block, table );
        this.knownBlocks.add( block );
    }
    
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return this.knownBlocks;
    }
    
    @Override
    @SuppressWarnings( "all" )
    protected void generate() {
        add( TomBlocks.ORE_ORE.get(), new LootTableBuilder()
                .addPool( new LootPoolBuilder( "drop" )
                        .addEntry( new LootEntryTagBuilder( Tags.Items.ORES )
                                .setCount( 1 )
                                .addCondition( () -> ExplosionCondition.survivesExplosion().build() )
                                .toLootEntry() )
                        .toLootPool() )
                .toLootTable() );
        
        add( TomBlocks.CAKE_ORE.get(), new LootTableBuilder()
                .addPool( new LootPoolBuilder( "drop" )
                        .addEntry( new LootEntryTagBuilder( TomTags.Items.CAKES )
                                .setCount( 1 )
                                .addCondition( () -> ExplosionCondition.survivesExplosion().build() )
                                .toLootEntry() )
                        .toLootPool() )
                .toLootTable() );
    }
}
