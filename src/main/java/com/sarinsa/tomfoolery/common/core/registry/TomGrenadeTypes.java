package com.sarinsa.tomfoolery.common.core.registry;

import com.sarinsa.tomfoolery.common.core.Tomfoolery;
import com.sarinsa.tomfoolery.common.core.registry.types.GrenadeType;
import com.sarinsa.tomfoolery.common.grenades.DoomGrenadeType;
import com.sarinsa.tomfoolery.common.grenades.ExplosiveGrenadeType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class TomGrenadeTypes {

    public static final DeferredRegister<GrenadeType> GRENADE_TYPES = DeferredRegister.create(GrenadeType.class, Tomfoolery.MODID);
    public static final Supplier<IForgeRegistry<GrenadeType>> GRENADE_TYPE_REGISTRY = GRENADE_TYPES.makeRegistry("grenade_types", () -> {
        return (new RegistryBuilder<GrenadeType>()).setType(GrenadeType.class).setDefaultKey(Tomfoolery.resourceLoc("empty"));
    });


    public static RegistryObject<GrenadeType> EXPLOSIVE = register("explosive", ExplosiveGrenadeType::new);
    public static RegistryObject<GrenadeType> DOOM = register("doom", DoomGrenadeType::new);



    private static RegistryObject<GrenadeType> register(String name, Supplier<GrenadeType> supplier) {
        return GRENADE_TYPES.register(name, supplier);
    }

    public static GrenadeType getOrDefault(ResourceLocation id) {
        for (Supplier<GrenadeType> type : GRENADE_TYPES.getEntries()) {
            if (type.get().getRegistryName().equals(id))
                return type.get();
        }
        return EXPLOSIVE.get();
    }
}
