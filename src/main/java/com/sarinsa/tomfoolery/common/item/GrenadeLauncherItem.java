package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomSounds;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.entity.InstaSaplingEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.grower.JungleTreeGrower;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class GrenadeLauncherItem extends ProjectileWeaponItem {

    public static final Map<Item, ILauncherLogic> LAUNCHER_LOGICS = new HashMap<>();

    private static final Predicate<ItemStack> DEFAULT_VALID_AMMO = (itemStack) -> itemStack.getItem() instanceof GrenadeRoundItem
            || itemStack.getItem() instanceof SplashPotionItem
            || itemStack.getItem() instanceof LingeringPotionItem
            || (itemStack.getItem() instanceof BlockItem && ((BlockItem) itemStack.getItem()).getBlock() instanceof SaplingBlock);


    public GrenadeLauncherItem() {
        super(new Item.Properties()
                .stacksTo(1)
                .rarity(Rarity.EPIC)
                .setNoRepair()
                .tab(CreativeModeTab.TAB_COMBAT)
                .durability(800)
        );
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return (itemStack) -> LAUNCHER_LOGICS.containsKey(itemStack.getItem()) || DEFAULT_VALID_AMMO.test(itemStack);
    }

    @Override
    public int getDefaultProjectileRange() {
        return 20;
    }

    public ItemStack getProjectile(Player player) {
        Predicate<ItemStack> predicate = getSupportedHeldProjectiles();
        ItemStack itemstack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
        if (!itemstack.isEmpty()) {
            return itemstack;
        }
        else {
            predicate = getAllSupportedProjectiles();

            for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack stack = player.getInventory().getItem(i);

                if (predicate.test(stack)) {
                    return stack;
                }
            }
            return player.getAbilities().instabuild ? new ItemStack(TomItems.EXPLOSIVE_GRENADE_ROUND.get()) : ItemStack.EMPTY;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(itemStack.getItem()))
            return InteractionResultHolder.fail(itemStack);

        boolean hasAmmo = !getProjectile(player).isEmpty();

        if (hasAmmo) {
            ItemStack ammoStack = getProjectile(player);

            if (!level.isClientSide) {
                if (LAUNCHER_LOGICS.containsKey(ammoStack.getItem())) {
                    LAUNCHER_LOGICS.get(ammoStack.getItem()).onLaunch(level, player, hand);
                }

                else if (ammoStack.getItem() instanceof GrenadeRoundItem) {
                    GrenadeRoundEntity entity = new GrenadeRoundEntity(player, level);
                    entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 2.0F, 2.0F, 2.0F);
                    entity.setGrenadeType(((GrenadeRoundItem) ammoStack.getItem()).getGrenadeType());
                    level.addFreshEntity(entity);
                }
                else if (ammoStack.getItem() instanceof SplashPotionItem || ammoStack.getItem() instanceof LingeringPotionItem) {
                    ThrownPotion entity = new ThrownPotion(level, player);
                    entity.setItem(ammoStack);
                    entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 2.0F, 2.0F, 2.0F);
                    level.addFreshEntity(entity);
                }
                else if (ammoStack.getItem() instanceof BlockItem && ((BlockItem) ammoStack.getItem()).getBlock() instanceof SaplingBlock) {
                    AbstractTreeGrower tree = ((SaplingBlock) ((BlockItem) ammoStack.getItem()).getBlock()).treeGrower;
                    InstaSaplingEntity entity = new InstaSaplingEntity(player, level, tree);
                    entity.setItem(ammoStack);
                    entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 2.0F, 2.0F, 2.0F);
                    level.addFreshEntity(entity);
                }

                if (!player.getAbilities().instabuild) {
                    itemStack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                    ammoStack.shrink(1);
                }
            }
            player.getCooldowns().addCooldown(itemStack.getItem(), 15);
            level.playSound(player, player, TomSounds.LAUNCHER_THUMP.get(), SoundSource.MASTER, 1.0F, 1.0F);
        }
        else {
            level.playSound(player, player, SoundEvents.DISPENSER_FAIL, SoundSource.MASTER, 0.9F, 1.0F);
        }
        return InteractionResultHolder.consume(itemStack);
    }
}
