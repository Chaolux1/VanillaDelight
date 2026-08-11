package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.furniture.FurnitureAppearance;
import net.chaolux.vanilladelight.common.furniture.FurnitureAppearanceHolder;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModularStoveBlockEntity extends CommonStoveBlockEntity implements FurnitureAppearanceHolder {
    private final FurnitureAppearance furnitureApperance;

    public ModularStoveBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.MODULAR_STOVE.get(),blockPos,blockState);
        this.furnitureApperance=new FurnitureAppearance(this.resolve(blockState));
    }

    @Override
    public FurnitureAppearance getFurnitureAppearance() {
        return this.furnitureApperance;
    }

    @Override
    public boolean setMaterial(String string,BlockState blockState) {
        if(!this.furnitureApperance.setMaterial(string,blockState)) return false;
        this.syncAppearance();
        return true;
    }

    @Override
    public String cycleStyle() {
        String string=this.furnitureApperance.cycleStyle();
        this.syncAppearance();
        return string;
    }

    @Override
    public boolean isAppearanceLocked() {
        return this.furnitureApperance.isLocked();
    }

    @Override
    public boolean lockAppearance() {
        if(!this.furnitureApperance.lock()) return false;
        this.syncAppearance();
        return true;
    }

    @Override
    public boolean unlockAppearance() {
        if(!this.furnitureApperance.unlock()) return false;
        this.syncAppearance();
        return true;
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag,provider);
        this.loadFurnitureData(compoundTag);
        if(this.level != null && this.level.isClientSide) this.refreshClientModel();
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag,HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag,provider);
        compoundTag.put("Furniture",this.furnitureApperance.save());
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag=super.getUpdateTag(provider);
        compoundTag.put("Furniture",this.furnitureApperance.save());
        return compoundTag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag compoundTag,HolderLookup.Provider provider) {
        super.handleUpdateTag(compoundTag,provider);
        this.loadFurnitureData(compoundTag);
        this.refreshClientModel();
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(FurnitureBlockEntity.RENDER_DATA,this.furnitureApperance.renderData(false)).build();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level != null && this.level.isClientSide) this.refreshClientModel();
    }

    public void dropInstallMaterial() {
        if(this.level == null || this.level.isClientSide) return;
        for (MaterialState materialState : this.furnitureApperance.installMaterial().values()) {
            ItemStack itemStack=materialState.toItemStack();
            if(itemStack.isEmpty()) continue;
            Containers.dropItemStack(this.level,this.worldPosition.getX() + 0.5D,this.worldPosition.getY() + 0.5D,this.worldPosition.getZ() + 0.5D,itemStack);
        }
        this.furnitureApperance.clearInstallMaterial();
        this.setChanged();
    }

    private FurnitureDefintion resolve(BlockState blockState) {
        if(blockState.getBlock() instanceof FurnitureDefintionProvider furnitureDefintionProvider) return FurnitureRegistry.get(furnitureDefintionProvider.getFurnitureDefintionId());
        throw new IllegalStateException("Modular stove does not provide furniture");
    }

    private void loadFurnitureData(CompoundTag compoundTag) {
        if(!compoundTag.contains("Furniture",Tag.TAG_COMPOUND)) return;
        this.furnitureApperance.load(compoundTag.getCompound("Furniture"),this.resolve(this.getBlockState()));
    }

    private void syncAppearance() {
        this.setChanged();
        if(this.level == null) return;
        BlockState blockState=this.getBlockState();
        if(this.level.isClientSide) {
            this.refreshClientModel();
            return;
        }
        this.level.sendBlockUpdated(this.worldPosition,blockState,blockState, Block.UPDATE_CLIENTS);
    }

    private void refreshClientModel() {
        this.requestModelDataUpdate();
        if(this.level == null || !this.level.isClientSide) return;
        BlockState blockState=this.getBlockState();
        this.level.sendBlockUpdated(this.worldPosition,blockState,blockState,Block.UPDATE_ALL);
    }

}
