package com.sarinsa.tomfoolery.common.core.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class TomClientConfig {

    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = clientPair.getLeft();
        CLIENT_SPEC = clientPair.getRight();
    }

    public static final class Client {

        private final ForgeConfigSpec.BooleanValue deathFlipDegreesEnabled;
        private final ForgeConfigSpec.DoubleValue deathFlipDegrees;

        private Client(ForgeConfigSpec.Builder configBuilder) {
            configBuilder.push("general_silliness");

            this.deathFlipDegreesEnabled = configBuilder.comment("Toggles death flip degrees.")
                    .define("death_flip_degrees_enabled", true);

            this.deathFlipDegrees = configBuilder.comment("If death flip degrees is enabled, this value will override how many degrees entities turn when they die.")
                    .defineInRange("death_flip_degrees", 360.0D, -10000.0D, 10000.0D);

            configBuilder.pop();
        }

        public boolean deathFlipDegreesEnabled() {
            return this.deathFlipDegreesEnabled.get();
        }

        public float getDeathFlipDegrees() {
            return (float) this.deathFlipDegrees.get().doubleValue();
        }
    }
}
