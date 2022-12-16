package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomSounds;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.entity.InstaSaplingEntity;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class GrenadeLauncherItem extends ShootableItem {

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
                .tab(ItemGroup.TAB_COMBAT)
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

    public ItemStack getProjectile(PlayerEntity player) {
        Predicate<ItemStack> predicate = getSupportedHeldProjectiles();
        ItemStack itemstack = ShootableItem.getHeldProjectile(player, predicate);
        if (!itemstack.isEmpty()) {
            return itemstack;
        }
        else {
            predicate = getAllSupportedProjectiles();

            for(int i = 0; i < player.inventory.getContainerSize(); ++i) {
                ItemStack stack = player.inventory.getItem(i);

                if (predicate.test(stack)) {
                    return stack;
                }
            }
            return player.abilities.instabuild ? new ItemStack(TomItems.EXPLOSIVE_GRENADE_ROUND.get()) : ItemStack.EMPTY;
        }
    }

    @Override
    public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(itemStack.getItem()))
            return ActionResult.fail(itemStack);

        boolean hasAmmo = !getProjectile(player).isEmpty();

        if (hasAmmo) {
            ItemStack ammoStack = getProjectile(player);

            if (!world.isClientSide) {
                if (LAUNCHER_LOGICS.containsKey(ammoStack.getItem())) {
                    LAUNCHER_LOGICS.get(ammoStack.getItem()).onLaunch(world, player, hand);
                }

                else if (ammoStack.getItem() instanceof GrenadeRoundItem) {
                    GrenadeRoundEntity entity = new GrenadeRoundEntity(player, world);
                    entity.shootFromRotation(player, player.xRot, player.yRot, 2.0F, 2.0F, 2.0F);
                    entity.setGrenadeType(((GrenadeRoundItem) ammoStack.getItem()).getGrenadeType());
                    world.addFreshEntity(entity);
                }
                else if (ammoStack.getItem() instanceof SplashPotionItem || ammoStack.getItem() instanceof LingeringPotionItem) {
                    PotionEntity entity = new PotionEntity(world, player);
                    entity.setItem(ammoStack);
                    entity.shootFromRotation(player, player.xRot, player.yRot, 2.0F, 2.0F, 2.0F);
                    world.addFreshEntity(entity);
                }
                else if (ammoStack.getItem() instanceof BlockItem && ((BlockItem) ammoStack.getItem()).getBlock() instanceof SaplingBlock) {
                    Tree tree = ((SaplingBlock) ((BlockItem) ammoStack.getItem()).getBlock()).treeGrower;
                    InstaSaplingEntity entity = new InstaSaplingEntity(player, world, tree);
                    entity.setItem(ammoStack);
                    entity.shootFromRotation(player, player.xRot, player.yRot, 2.0F, 2.0F, 2.0F);
                    world.addFreshEntity(entity);
                }

                if (!player.abilities.instabuild) {
                    itemStack.hurtAndBreak(1, player, (p) -> {
                        p.broadcastBreakEvent(hand);
                    });
                    ammoStack.shrink(1);
                }
            }
            player.getCooldowns().addCooldown(itemStack.getItem(), 15);
            world.playSound(player, player, TomSounds.LAUNCHER_THUMP.get(), SoundCategory.MASTER, 1.0F, 1.0F);
        }
        else {
            world.playSound(player, player, SoundEvents.DISPENSER_FAIL, SoundCategory.MASTER, 0.9F, 1.0F);
        }
        return ActionResult.consume(itemStack);
    }
}
