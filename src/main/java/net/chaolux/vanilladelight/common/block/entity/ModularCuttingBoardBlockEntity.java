package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.chaolux.vanilladelight.common.block.CuttingBoardPattern;
import net.chaolux.vanilladelight.common.furniture.FurnitureAppearance;
import net.chaolux.vanilladelight.common.furniture.FurnitureDefintionProvider;
import net.chaolux.vanilladelight.common.item.ModularCuttingBoardItem;
import net.chaolux.vanilladelight.registry.block.ModBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModularCuttingBoardBlockEntity extends CommonCuttingBoardBlockEntity {
    private final FurnitureAppearance furnitureAppearance;

    public ModularCuttingBoardBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntityTypes.MODULAR_CUTTING_BOARD.get(), blockPos, blockState);
        this.furnitureAppearance = new FurnitureAppearance(this.resolveDefinition(blockState));
    }

    public FurnitureAppearance getFurnitureAppearance() {
        return this.furnitureAppearance;
    }

    public boolean setPattern(CuttingBoardPattern cuttingBoardPattern) {
        if (!this.furnitureAppearance.setStyle(cuttingBoardPattern.id())) return false;
        this.syncAppearance();
        return true;
    }

    public CuttingBoardPattern getPattern() {
        return CuttingBoardPattern.boardPattern(this.furnitureAppearance.style());
    }

    public boolean applyItemAppearance(ItemStack itemStack) {
        CompoundTag compoundTag= ModularCuttingBoardItem.getFurnitureTag(itemStack);
        if(compoundTag == null) return false;
        this.furnitureAppearance.load(compoundTag,this.resolveDefinition(this.getBlockState()));
        this.syncAppearance();
        return true;
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        if (compoundTag.contains("Furniture", Tag.TAG_COMPOUND))
            this.furnitureAppearance.load(compoundTag.getCompound("Furniture"), this.resolveDefinition(this.getBlockState()));
    }

    @Override
    public void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.put("Furniture", this.furnitureAppearance.save());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compoundTag = super.getUpdateTag();
        compoundTag.put("Furniture", this.furnitureAppearance.save());
        return compoundTag;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection connection,ClientboundBlockEntityDataPacket clientboundBlockEntityDataPacket) {
        CompoundTag compoundTag=clientboundBlockEntityDataPacket.getTag();
        if(compoundTag == null) return;
        this.load(compoundTag);
        this.refreshClientModel();
    }

    @Override
    public void handleUpdateTag(CompoundTag compoundTag) {
        this.load(compoundTag);
        this.refreshClientModel();
    }

    @Override
    public @NotNull ModelData getModelData() {
        return ModelData.builder().with(FurnitureBlockEntity.RENDER_DATA, this.furnitureAppearance.renderData(false)).build();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && this.level.isClientSide) this.refreshClientModel();
    }

    private FurnitureDefintion resolveDefinition(BlockState blockState) {
        if (blockState.getBlock() instanceof FurnitureDefintionProvider furnitureDefintionProvider)
            return FurnitureRegistry.get(furnitureDefintionProvider.getFurnitureDefintionId());
        throw new IllegalArgumentException("Modular Cutting Board does not provide furniture ID");
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
