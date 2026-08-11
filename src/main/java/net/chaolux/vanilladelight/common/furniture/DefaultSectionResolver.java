package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureSection;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public class DefaultSectionResolver implements SectionResolver {
    @Override
    public Optional<FurnitureSection> resolve(FurnitureDefintion defintion, String style, Vec3 vec3, Direction direction) {
        Vec3 vec4=this.toModelSpace(vec3,direction);
        return defintion.sections().stream().filter(section -> section.matches(vec4,style)).findFirst();
    }

    private Vec3 toModelSpace(Vec3 vec3,Direction direction) {
        return switch (direction) {
            case EAST -> new Vec3(vec3.z,vec3.y,1.0 - vec3.x);
            case SOUTH -> new Vec3(1.0 - vec3.x,vec3.y,1.0 - vec3.z);
            case WEST -> new Vec3(1.0 - vec3.z,vec3.y,vec3.x);
            default -> vec3;
        };
    }
}