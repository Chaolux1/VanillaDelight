package net.chaolux.vanilladelight.client.model.furniture;

import net.chaolux.vanilladelight.api.furniture.FurniturePart;
import net.chaolux.vanilladelight.common.furniture.FurnitureRenderData;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class FurnitureModelCache {
    private final Map<Key, List<BakedQuad>> cache=new ConcurrentHashMap<>();

    public List<BakedQuad> bakedQuadList(FurnitureRenderData renderData, Direction direction, @Nullable Direction facing, @Nullable RenderType renderType, Supplier<List<BakedQuad>> listSupplier) {
        return this.cache.computeIfAbsent(new Key(renderData,direction,facing,renderType),key -> List.copyOf(listSupplier.get()));
    }

    public void clear() {
        this.cache.clear();
    }

    private record Key(FurnitureRenderData renderData,Direction direction,@Nullable Direction facing,@Nullable RenderType renderType) {

    }
}
