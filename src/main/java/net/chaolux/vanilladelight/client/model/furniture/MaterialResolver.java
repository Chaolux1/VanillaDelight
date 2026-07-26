package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.core.Direction;

public interface MaterialResolver {
    boolean supports(MaterialState materialState);
    ResolvedFurnitureMaterial resolve(MaterialState materialState, Direction direction);
    default void clear() {

    }
}
