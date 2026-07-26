package net.chaolux.vanilladelight.common.block.entity;

import net.chaolux.vanilladelight.common.block.ModularContainerFurnitureBlock;
import net.chaolux.vanilladelight.common.block.ModularFurnitureBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.registry.ModSounds;

import javax.annotation.Nullable;

public class FurnitureContainerBlockEntity extends FurnitureBlockEntity implements Container, MenuProvider {
    private NonNullList<ItemStack> itemStacks;
    private Component component;

    private final ContainerOpenersCounter openersCounter=new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level level, BlockPos blockPos, BlockState blockState) {
            FurnitureContainerBlockEntity.this.playSound(blockState, ModSounds.BLOCK_CABINET_OPEN.get());
            FurnitureContainerBlockEntity.this.updateOpenState(blockState,true);
        }

        @Override
        protected void onClose(Level level, BlockPos blockPos, BlockState blockState) {
            FurnitureContainerBlockEntity.this.playSound(blockState,ModSounds.BLOCK_CABINET_CLOSE.get());
            FurnitureContainerBlockEntity.this.updateOpenState(blockState,false);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos blockPos, BlockState blockState, int i, int i1) {

        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return player.containerMenu instanceof ChestMenu chestMenu && chestMenu.getContainer() == FurnitureContainerBlockEntity.this;
        }
    };

    public FurnitureContainerBlockEntity(BlockPos blockPos,BlockState blockState) {
        super(blockPos,blockState);
        this.itemStacks=NonNullList.withSize(this.resolveContainerSize(),ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        ContainerHelper.saveAllItems(compoundTag,this.itemStacks);
        if(this.component != null) {
            compoundTag.putString("CustomName",Component.Serializer.toJson(this.component));
        }
    }

    @Override
    public void load(CompoundTag compoundTag) {
        super.load(compoundTag);
        this.itemStacks=NonNullList.withSize(this.resolveContainerSize(),ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compoundTag,this.itemStacks);
        if(compoundTag.contains("CustomName",8)) this.component=Component.Serializer.fromJson(compoundTag.getString("CustomName"));
    }

    @Override
    public int getContainerSize() {
        return this.itemStacks.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemStack : this.itemStacks) {
            if(!itemStack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.itemStacks.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot,int value) {
        ItemStack itemStack=ContainerHelper.removeItem(this.itemStacks,slot,value);
        if(!itemStack.isEmpty()) this.setChanged();
        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.itemStacks,slot);
    }

    @Override
    public void setItem(int slot,ItemStack itemStack) {
        this.itemStacks.set(slot,itemStack);
        if(itemStack.getCount() > this.getMaxStackSize()) itemStack.setCount(this.getMaxStackSize());
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        if(this.level == null || this.level.getBlockEntity(this.worldPosition) != this) return false;
        return player.distanceToSqr(this.worldPosition.getX() + 0.5,this.worldPosition.getY() + 0.5,this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public void clearContent() {
        this.itemStacks.clear();
    }

    @Override
    public void startOpen(Player player) {
        if(this.level != null && !this.remove && !player.isSpectator()) this.openersCounter.incrementOpeners(player,this.level,this.worldPosition,this.getBlockState());
    }

    @Override
    public void stopOpen(Player player) {
        if(this.level != null && !this.remove && !player.isSpectator()) this.openersCounter.decrementOpeners(player,this.level,this.worldPosition,this.getBlockState());
    }

    public void recheckOpen() {
        if(this.level != null && !this.remove) this.openersCounter.recheckOpeners(this.level,this.worldPosition,this.getBlockState());
    }

    public NonNullList<ItemStack> itemStacks() {
        return this.itemStacks;
    }

    public void setCustomName(Component component) {
        this.component=component;
    }

    @Override
    public Component getDisplayName() {
        if(this.component != null) return this.component;
        return this.getBlockState().getBlock().getName();
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory,Player player) {
        int rows=Math.max(1,Math.min(6,this.getContainerSize() / 9));
        return switch (rows) {
            case 1 -> new ChestMenu(MenuType.GENERIC_9x1,id,inventory,this,1);
            case 2 -> new ChestMenu(MenuType.GENERIC_9x2,id,inventory,this,2);
            case 3 -> new ChestMenu(MenuType.GENERIC_9x3,id,inventory,this,3);
            case 4 -> new ChestMenu(MenuType.GENERIC_9x4,id,inventory,this,4);
            case 5 -> new ChestMenu(MenuType.GENERIC_9x5,id,inventory,this,5);
            case 6 -> new ChestMenu(MenuType.GENERIC_9x6,id,inventory,this,6);
            default -> throw new IllegalStateException("Invalid container row count: " + rows);
        };
    }

    private int resolveContainerSize() {
        int size=this.getFurnitureAppearance().definition().containerSize();
        if(size <= 0) return 27;
        return Math.max(9,Math.min(54,size / 9 * 9));
    }

    private void updateOpenState(BlockState blockState,boolean open) {
        if(this.level != null && blockState.hasProperty(ModularContainerFurnitureBlock.OPEN)) this.level.setBlock(this.worldPosition,blockState.setValue(ModularContainerFurnitureBlock.OPEN,open),3);
    }

    private void playSound(BlockState blockState, SoundEvent soundEvents) {
        if(this.level == null) return;
        Direction direction=blockState.getValue(ModularFurnitureBlock.FACING);
        Vec3i vec3i=direction.getNormal();
        double x=this.worldPosition.getX() + 0.5 + vec3i.getX() / 2.0;
        double y=this.worldPosition.getY() + 0.5;
        double z=this.worldPosition.getZ() + 0.5 + vec3i.getZ() / 2.0;
        this.level.playSound(null,x,y,z,soundEvents, SoundSource.BLOCKS,0.5f,this.level.random.nextFloat() * 0.1f + 0.9f);
    }
}