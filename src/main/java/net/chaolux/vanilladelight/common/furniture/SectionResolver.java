package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureSection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public interface SectionResolver {
    Optional<FurnitureSection> resolve(FurnitureDefintion defintion, String style, Vec3 vec3, Direction direction);
}
