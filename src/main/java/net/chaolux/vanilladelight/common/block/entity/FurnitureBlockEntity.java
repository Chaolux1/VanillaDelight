package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.ModularContainerFurnitureBlock;
import net.chaolux.vanilladelight.common.furniture.*;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class FurnitureBlockEntity extends BlockEntity implements FurnitureAppearanceHolder {
    public static final ModelProperty<FurnitureRenderData> RENDER_DATA=new ModelProperty<>();
    private final FurnitureAppearance furnitureAppearance;

    public FurnitureBlockEntity(BlockPos blockPos, BlockState blockState) {
        this(ModBlockEntityTypes.FURNITURE.get(),blockPos,blockState);
    }

    protected FurnitureBlockEntity(BlockEntityType<?> blockEntityType,BlockPos blockPos,BlockState blockState) {
        super(blockEntityType,blockPos,blockState);
        this.furnitureAppearance=new FurnitureAppearance(this.resolveDefinition(blockState));
    }

    public FurnitureAppearance getFurnitureAppearance() {
        return this.furnitureAppearance;
    }

    public boolean setMaterial(String string,BlockState blockState) {
        if (!this.furnitureAppearance.setMaterial(string,blockState)) return false;
        this.syncAppearance();
        return true;
    }

    public String cycleStyle() {
        String string=this.furnitureAppearance.cycleStyle();
        this.syncAppearance();
        return string;
    }

    @Override
    public void loadAdditional(CompoundTag compoundTag, HolderLookup.Provider provider) {
        super.loadAdditional(compoundTag,provider);
        if(compoundTag.contains("Furniture", Tag.TAG_COMPOUND)) this.furnitureAppearance.load(compoundTag.getCompound("Furniture"),this.resolveDefinition(this.getBlockState()));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        CompoundTag compoundTag=new CompoundTag();
        compoundTag.put("Furniture",this.furnitureAppearance.save());
        return compoundTag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void handleUpdateTag(CompoundTag compoundTag,HolderLookup.Provider provider) {
        if(compoundTag.contains("Furniture",Tag.TAG_COMPOUND)) {
            this.furnitureAppearance.load(compoundTag.getCompound("Furniture"),this.resolveDefinition(this.getBlockState()));
            this.refreshClientModel();
        }
    }

    @Override
    public @NotNull ModelData getModelData() {
        boolean open=this.getBlockState().hasProperty(ModularContainerFurnitureBlock.OPEN) && this.getBlockState().getValue(ModularContainerFurnitureBlock.OPEN);
        return ModelData.builder().with(RENDER_DATA,this.furnitureAppearance.renderData(open)).build();
    }

    @Override
    public void onDataPacket(Connection connection,ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket,HolderLookup.Provider provider) {
        super.onDataPacket(connection,clientboundBlockEntityDataPacket,provider);
        this.refreshClientModel();
    }

    protected FurnitureDefintion resolveDefinition(BlockState blockState) {
        if(blockState.getBlock() instanceof FurnitureDefintionProvider furnitureDefintionProvider) return FurnitureRegistry.get(furnitureDefintionProvider.getFurnitureDefintionId());
        throw new IllegalStateException("Furniture block does not provide id");
    }

    protected void syncAppearance() {
        this.setChanged();
        if(this.level == null) return;
        BlockState blockState=this.getBlockState();
        if(this.level.isClientSide) {
            this.refreshClientModel();
            return;
        }
        this.level.sendBlockUpdated(this.worldPosition,blockState,blockState, Block.UPDATE_CLIENTS);
    }

    public boolean isAppearanceLocked() {
        return this.furnitureAppearance.isLocked();
    }

    public boolean lockAppearance() {
        if(!this.furnitureAppearance.lock()) return false;
        this.syncAppearance();
        return true;
    }

    public boolean unlockAppearance() {
        if(!this.furnitureAppearance.unlock()) return false;
        this.syncAppearance();
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag,HolderLookup.Provider provider) {
        super.saveAdditional(compoundTag,provider);
        compoundTag.put("Furniture",this.furnitureAppearance.save());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if(this.level != null && this.level.isClientSide) this.refreshClientModel();
    }

    private void refreshClientModel() {
        this.requestModelDataUpdate();
        if(this.level == null || !this.level.isClientSide) return;
        BlockState blockState=this.getBlockState();
        this.level.sendBlockUpdated(this.worldPosition,blockState,blockState, Block.UPDATE_ALL);
    }

    public void dropInstallMaterial() {
        if(this.level == null || this.level.isClientSide) return;
        for (MaterialState materialState : this.furnitureAppearance.installMaterial().values()) {
            ItemStack itemStack=materialState.toItemStack();
            if(itemStack.isEmpty()) continue;
            Containers.dropItemStack(this.level,this.worldPosition.getX() + 0.5,this.worldPosition.getY() + 0.5,this.worldPosition.getZ() + 0.5,itemStack);
        }
        this.furnitureAppearance.clearInstallMaterial();
        this.setChanged();
    }
}
