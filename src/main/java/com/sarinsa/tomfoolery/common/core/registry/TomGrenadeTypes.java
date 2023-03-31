package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.grenades.DoomGrenadeType;
import com.sarinsa.tomfoolery.common.grenades.ExplosiveGrenadeType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class TomGrenadeTypes {

    public static final DeferredRegister<GrenadeType> GRENADE_TYPES = DeferredRegister.create(new ResourceLocation(Tomfoolery.MODID, "grenade_types"), Tomfoolery.MODID);
    public static final Supplier<IForgeRegistry<GrenadeType>> GRENADE_TYPE_REGISTRY = GRENADE_TYPES.makeRegistry(() -> {
        return (new RegistryBuilder<GrenadeType>()).setDefaultKey(Tomfoolery.resourceLoc("empty"));
    });


    public static RegistryObject<GrenadeType> EXPLOSIVE = register("explosive", ExplosiveGrenadeType::new);
    public static RegistryObject<GrenadeType> DOOM = register("doom", DoomGrenadeType::new);



    private static RegistryObject<GrenadeType> register(String name, Supplier<GrenadeType> supplier) {
        return GRENADE_TYPES.register(name, supplier);
    }

    public static GrenadeType getOrDefault(ResourceLocation id) {
        for (Supplier<GrenadeType> type : GRENADE_TYPES.getEntries()) {
            if (GRENADE_TYPE_REGISTRY.get().getKey(type.get()).equals(id))
                return type.get();
        }
        return EXPLOSIVE.get();
    }
}
