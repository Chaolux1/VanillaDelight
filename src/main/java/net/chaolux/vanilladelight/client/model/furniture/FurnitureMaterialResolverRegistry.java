package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.core.Direction;

import java.lang.module.ResolvedModule;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class FurnitureMaterialResolverRegistry {
    private static final List<MaterialResolver> RESOLVERS=new CopyOnWriteArrayList<>();
    private static final DefaultMaterialResolver DEFAULT=new DefaultMaterialResolver();
    private FurnitureMaterialResolverRegistry() {

    }

    public static void register(MaterialResolver materialResolver) {
        RESOLVERS.add(0,materialResolver);
    }

    public static ResolvedFurnitureMaterial furnitureMaterial(MaterialState materialState, Direction direction) {
        for(MaterialResolver materialResolver : RESOLVERS) {
            if(materialResolver.supports(materialState)) return materialResolver.resolve(materialState,direction);
        }
        return DEFAULT.resolve(materialState,direction);
    }

    public static void clear() {
        RESOLVERS.forEach(MaterialResolver::clear);
        DEFAULT.clear();
    }
}
