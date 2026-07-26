package net.chaolux.vanilladelight.api.furniture;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Set;

public record FurnitureSection(String id, String material, AABB aabb, Set<String> styles, int priority) {
    public FurnitureSection {
        styles=Set.copyOf(styles);
    }

    public boolean matches(Vec3 vec3, String string) {
        boolean styleMatches=this.styles.isEmpty() || this.styles.contains(string);
        return styleMatches && this.aabb.contains(vec3);
    }
}
