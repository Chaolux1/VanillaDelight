package net.chaolux.vanilladelight.registry.particle;

import net.chaolux.vanilladelight.VanillaDelight;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final RegistryObject<SimpleParticleType> MODULAR_FLAME;

    static {
        PARTICLE_TYPES=DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, VanillaDelight.MOD_ID);
        MODULAR_FLAME=PARTICLE_TYPES.register("modular_flame",() -> new SimpleParticleType(false));
    }
}
