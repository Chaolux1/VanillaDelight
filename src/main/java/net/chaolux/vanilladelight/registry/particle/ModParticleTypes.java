package net.chaolux.vanilladelight.registry.particle;

import net.chaolux.vanilladelight.VanillaDelight;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;
    public static final Supplier<SimpleParticleType> MODULAR_FLAME;

    static {
        PARTICLE_TYPES=DeferredRegister.create(Registries.PARTICLE_TYPE, VanillaDelight.MOD_ID);
        MODULAR_FLAME=PARTICLE_TYPES.register("modular_flame",() -> new SimpleParticleType(false));
    }
}
