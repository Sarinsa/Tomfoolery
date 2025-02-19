package com.sarinsa.tomfoolery.common.core.config;

import com.sarinsa.tomfoolery.common.core.registry.TomBlocks;
import com.sarinsa.tomfoolery.common.core.registry.TomGrenadeTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class TomCommonConfig {

    public static final TomCommonConfig.Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<TomCommonConfig.Common, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(TomCommonConfig.Common::new);
        COMMON = clientPair.getLeft();
        COMMON_SPEC = clientPair.getRight();
    }

    public static final class Common {

        private ForgeConfigSpec.ConfigValue<List<? extends String>> allowedGrenades;
        private ForgeConfigSpec.ConfigValue<List<? extends String>> spawningOres;

        public ForgeConfigSpec.BooleanValue spawnGhastinators;

        public ForgeConfigSpec.DoubleValue grenadeLauncherZombieChance;


        private Common(ForgeConfigSpec.Builder configBuilder) {
            spawningOres = configBuilder.defineList("spawning_ores", defaultSpawningOres(), (o) -> existInRegistry(o, ForgeRegistries.BLOCKS));

            allowedGrenades = configBuilder.comment("A list of grenade types for the Redicu-Launcher that are allowed to be used and crafted.")
                    .define("allowed_grenades", defaultAllowedGrenades(), (o) -> existInRegistry(o, TomGrenadeTypes.GRENADE_TYPE_REGISTRY.get()));

            spawnGhastinators = configBuilder.comment("If enabled, the almighty Ghastinator will spawn at night when it is a new moon. This is a friggin massive ghast that can see you from really far away" +
                    " and shoots mega fireballs. Not recommended if you enjoy your base not being exploded!")
                    .define("spawn_ghastinators", false);

            grenadeLauncherZombieChance = configBuilder.comment("The chance for zombies to spawn with a grenade launcher. Set to 0 to disable this altogether")
                    .defineInRange("grenade_launcher_zombie_chance", 0.05, 0.0, 1.0);
        }

        private static boolean existInRegistry(Object o, IForgeRegistry<?> registry) {
            if (o instanceof String string && ResourceLocation.tryParse(string) != null) {
                ResourceLocation id = ResourceLocation.tryParse(string);
                return registry.containsKey(id);
            }
            return false;
        }

        private static List<? extends String> defaultSpawningOres() {
            List<String> list = new ArrayList<>();

            list.add(TomBlocks.ORE_ORE.getId().toString());
            list.add(TomBlocks.CAKE_ORE.getId().toString());

            return list;
        }

        private static List<? extends String> defaultAllowedGrenades() {
            List<String> list = new ArrayList<>();

            list.add(TomGrenadeTypes.EXPLOSIVE.getId().toString());
            list.add(TomGrenadeTypes.DOOM.getId().toString());

            return list;
        }

        public List<? extends String> getSpawningOres() {
            return spawningOres.get();
        }

        public List<? extends String> getAllowedGrenades() {
            return allowedGrenades.get();
        }
    }
}
