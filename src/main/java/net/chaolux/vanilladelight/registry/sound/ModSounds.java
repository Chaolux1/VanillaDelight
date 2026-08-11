package net.chaolux.vanilladelight.registry.sound;

import net.chaolux.vanilladelight.VanillaDelight;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public static final Supplier<SoundEvent> MUSIC_DISC_ANEW;

    public static Supplier<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("vanilladelight",name)));
    }

    static {
        SOUND_EVENTS=DeferredRegister.create(Registries.SOUND_EVENT,"vanilladelight");
        MUSIC_DISC_ANEW=SOUND_EVENTS.register("music_disc_anew",() -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(VanillaDelight.MOD_ID,"music_disc_anew")));
    }
}
