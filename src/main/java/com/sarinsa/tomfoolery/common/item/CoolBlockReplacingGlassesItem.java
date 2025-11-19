package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CoolBlockReplacingGlassesItem extends CoolGlassesItem {
    
    private final Supplier<Block> blockSupplier;
    private final String armorTexture;
    
    public CoolBlockReplacingGlassesItem( Supplier<Block> blockSupplier, String materialName ) {
        this.blockSupplier = blockSupplier;
        this.armorTexture = Tomfoolery.rl( "textures/models/armor/cool_" + materialName + "_glasses.png" ).toString();
    }
    
    @Override
    public @Nullable String getArmorTexture( ItemStack stack, Entity entity, EquipmentSlot slot, String type ) {
        return armorTexture;
    }
    
    @Override
    public void gaze( Player player, Level level, BlockHitResult hitResult ) {
        BlockPos lookAtPos = hitResult.getBlockPos();
        BlockState lookAtState = level.getBlockState( lookAtPos );
        
        if( !lookAtState.isAir() && lookAtState.getBlock() != blockSupplier.get() && !lookAtState.getCollisionShape( level, lookAtPos ).isEmpty() ) {
            if( level.getBlockEntity( lookAtPos ) != null ) {
                BlockEntity blockEntity = level.getBlockEntity( lookAtPos );
                
                if( blockEntity instanceof Container ) {
                    Containers.dropContents( level, lookAtPos, (Container) blockEntity );
                }
            }
            level.setBlock( lookAtPos, blockSupplier.get().defaultBlockState(), 2 );
        }
    }
    
    public Supplier<Block> getBlockSupplier() {
        return this.blockSupplier;
    }
}
