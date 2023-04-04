package com.sarinsa.tomfoolery.common.item;

import com.sarinsa.tomfoolery.api.ILauncherLogic;
import com.sarinsa.tomfoolery.common.core.registry.TomItems;
import com.sarinsa.tomfoolery.common.core.registry.TomSounds;
import com.sarinsa.tomfoolery.common.entity.GrenadeRoundEntity;
import com.sarinsa.tomfoolery.common.entity.InstaSaplingEntity;
import com.sarinsa.tomfoolery.common.util.TranslationReferences;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class GrenadeLauncherItem extends ProjectileWeaponItem {

    private static final String AMMO_DATA_TAG = "LauncherAmmo";

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
    public UseAnim getUseAnimation(ItemStack p_41452_) {
        return UseAnim.CROSSBOW;
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(itemStack.getItem()))
            return InteractionResultHolder.fail(itemStack);

        ItemStack ammoStack = getNextAmmo(itemStack);

        if (ammoStack != null) {
            if (!level.isClientSide) {
                if (LAUNCHER_LOGICS.containsKey(ammoStack.getItem())) {
                    LAUNCHER_LOGICS.get(ammoStack.getItem()).onLaunch(level, player, hand);
                }

                else if (ammoStack.getItem() instanceof GrenadeRoundItem) {
                    GrenadeRoundEntity entity = new GrenadeRoundEntity(player, level);
                    entity.shootFromRotation(player, player.getXRot(), player.getYRot(), 2.5F, 2.5F, 2.5F);
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
                }
                level.playSound(null, player, TomSounds.LAUNCHER_THUMP.get(), SoundSource.MASTER, 1.0F, 1.0F);
            }
            player.getCooldowns().addCooldown(itemStack.getItem(), 8);}
        else {
            level.playSound(player, player, SoundEvents.DISPENSER_FAIL, SoundSource.MASTER, 0.9F, 1.0F);
            if (!level.isClientSide) {
                reload(itemStack, player);
            }
            player.getCooldowns().addCooldown(itemStack.getItem(), 50);
        }
        return InteractionResultHolder.consume(itemStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        List<ItemStack> ammo = getAmmo(itemStack);
        components.add(Component.literal(""));

        if (ammo != null && !ammo.isEmpty()) {
            components.add(Component.literal(ChatFormatting.GRAY + TranslationReferences.LAUNCHER_AMMO_INFO.getString() + ":"));

            for (ItemStack stack : ammo) {
                components.add(Component.literal(ChatFormatting.YELLOW + Component.translatable(stack.getDescriptionId()).getString()));
            }
        }
        else {
            components.add(Component.literal(ChatFormatting.RED + TranslationReferences.LAUNCHER_EMPTY.getString()));
        }
        components.add(Component.literal(""));
    }

    private void reload(ItemStack itemStack, Player player) {
        CompoundTag tag = itemStack.getOrCreateTag();
        ListTag ammoData;

        if (tag.contains(AMMO_DATA_TAG, Tag.TAG_LIST)) {
            ammoData = tag.getList(AMMO_DATA_TAG, Tag.TAG_COMPOUND);
        }
        else {
            ammoData = new ListTag();

            for (int i = 0; i < 6; i++) {
                ammoData.add(ItemStack.EMPTY.save(new CompoundTag()));
            }
        }

        for (int i = 0; i < 6; i++) {
            ItemStack stack = getProjectile(player);
            ammoData.set(i, stack.save(new CompoundTag()));

            if (!player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        tag.put(AMMO_DATA_TAG, ammoData);
        itemStack.setTag(tag);
    }

    /**
     * Fetches the next available ammo stack from the
     * grenade launcher ItemStack's NBT. This clears the
     * ammo item's entry from the launcher's NBT.
     */
    @Nullable
    private ItemStack getNextAmmo(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();

        if (tag.contains(AMMO_DATA_TAG, Tag.TAG_LIST)) {
            ListTag ammoData = tag.getList(AMMO_DATA_TAG, Tag.TAG_COMPOUND);

            if (ammoData.isEmpty())
                return null;

            for (int i = 0; i < 6; i++) {
                ItemStack ammo = ItemStack.of(ammoData.getCompound(i));

                if (ammo.isEmpty()) {
                    continue;
                }
                ammoData.set(i, ItemStack.EMPTY.save(new CompoundTag()));
                return ammo;
            }
        }
        return null;
    }

    @Nullable
    private List<ItemStack> getAmmo(ItemStack itemStack) {
        CompoundTag tag = itemStack.getOrCreateTag();

        if (tag.contains(AMMO_DATA_TAG, Tag.TAG_LIST)) {
            ListTag ammoData = tag.getList(AMMO_DATA_TAG, Tag.TAG_COMPOUND);

            if (ammoData.isEmpty())
                return null;

            List<ItemStack> ammoStacks = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                ItemStack ammo = ItemStack.of(ammoData.getCompound(i));

                if (ammo.getItem() != Items.AIR && ammo != ItemStack.EMPTY)
                    ammoStacks.add(ammo);
            }
            return ammoStacks;
        }
        return null;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(
                new IClientItemExtensions() {
                    @Override
                    public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                        return HumanoidModel.ArmPose.CROSSBOW_HOLD;
                    }
                }
        );
    }
}
