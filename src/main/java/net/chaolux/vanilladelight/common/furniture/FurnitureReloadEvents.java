package net.chaolux.vanilladelight.common.furniture;

import net.minecraftforge.event.AddReloadListenerEvent;

public class FurnitureReloadEvents {
    private FurnitureReloadEvents() {

    }

    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(new FurnitureDefintionReloadListener());
    }
}
