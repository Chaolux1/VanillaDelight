package net.chaolux.vanilladelight.client.model.furniture;

import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

public enum FurnitureClientReloadListener implements ResourceManagerReloadListener {
    INSTANCE;

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        FurnitureMaterialResolverRegistry.clear();
    }
}
