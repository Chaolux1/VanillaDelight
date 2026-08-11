package net.chaolux.vanilladelight.client.model.cabinet;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.function.Function;

public class UnbakedModularCabinetModel implements IUnbakedGeometry<UnbakedModularCabinetModel> {
    private final ResourceLocation resourceLocation;
    private final ResourceLocation location;

    public UnbakedModularCabinetModel(ResourceLocation resourceLocation,ResourceLocation location) {
        this.resourceLocation=resourceLocation;
        this.location=location;
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> unbakedModelFunction, IGeometryBakingContext iGeometryBakingContext) {

    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> atlasSpriteFunction, ModelState modelState, ItemOverrides itemOverrides) {
        TextureAtlasSprite textureAtlasSprite=atlasSpriteFunction.apply(new Material(InventoryMenu.BLOCK_ATLAS,this.location));
        return new DynamicModularCabinetBakedModel(this.resourceLocation,textureAtlasSprite,iGeometryBakingContext.getTransforms());
    }
}
