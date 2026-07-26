package net.chaolux.vanilladelight.api.furniture;

import net.minecraft.resources.ResourceLocation;

import java.util.*;

public final class FurnitureRegistry {
    private static final Map<ResourceLocation,FurnitureDefintion> DEFAULTS=new LinkedHashMap<>();
    private static volatile Map<ResourceLocation,FurnitureDefintion> loadedDefintions=Map.of();
    private FurnitureRegistry() {}

    public static synchronized FurnitureDefintion register(FurnitureDefintion defintion) {
        FurnitureDefintion previous=DEFAULTS.putIfAbsent(defintion.id(),defintion);
        if(previous != null) {
            throw new IllegalStateException("Duplicate furniture definition: " + defintion.id());
        }
        return defintion;
    }

    public static Optional<FurnitureDefintion> findDefintions(ResourceLocation resourceLocation) {
        FurnitureDefintion defintion=loadedDefintions.get(resourceLocation);
        if(defintion != null) return Optional.of(defintion);
        return Optional.ofNullable(DEFAULTS.get(resourceLocation));
    }

    public static FurnitureDefintion get(ResourceLocation resourceLocation) {
        return findDefintions(resourceLocation).orElseThrow(() -> new IllegalStateException("Unknown furniture definition: " + resourceLocation));
    }

    public static Collection<FurnitureDefintion> values() {
        Map<ResourceLocation,FurnitureDefintion> result=new LinkedHashMap<>(DEFAULTS);
        result.putAll(loadedDefintions);
        return List.copyOf(result.values());
    }

    public static synchronized void replaceLoaded(Map<ResourceLocation,FurnitureDefintion> defintionMap) {
        loadedDefintions=Map.copyOf(defintionMap);
    }

}
