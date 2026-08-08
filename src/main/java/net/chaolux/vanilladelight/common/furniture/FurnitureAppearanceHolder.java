package net.chaolux.vanilladelight.common.furniture;

import net.minecraft.world.level.block.state.BlockState;

public interface FurnitureAppearanceHolder {
    FurnitureAppearance getFurnitureAppearance();
    boolean setMaterial(String string, BlockState blockState);
    String cycleStyle();
    boolean isAppearanceLocked();
    boolean lockAppearance();
    boolean unlockAppearance();
    void dropInstallMaterial();
    default boolean canReplaceMaterial() {
        return false;
    }
    default boolean canSneakToReplace() {
        return false;
    }
}
