package net.chaolux.vanilladelight.registry.sound;

import net.chaolux.vanilladelight.VanillaDelight;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
    public static final RegistryObject<SoundEvent> MUSIC_DISC_ANEW;

    public static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation("vanilladelight",name)));
    }

    static {
        SOUND_EVENTS=DeferredRegister.create(ForgeRegistries.SOUND_EVENTS,"vanilladelight");
        MUSIC_DISC_ANEW=SOUND_EVENTS.register("music_disc_anew",() -> SoundEvent.createVariableRangeEvent(new ResourceLocation(VanillaDelight.MOD_ID,"music_disc_anew")));
    }
}
