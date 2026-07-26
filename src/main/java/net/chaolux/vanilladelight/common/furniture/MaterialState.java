package net.chaolux.vanilladelight.common.furniture;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.PropertyPermission;

public record MaterialState(ResourceLocation resourceLocation, Map<String,String> properties) {
    public MaterialState {
        properties=Map.copyOf(properties);
    }

    public static MaterialState from(BlockState blockState) {
        Map<String,String> values=new LinkedHashMap<>();
        for (Map.Entry<Property<?>,Comparable<?>> entry : blockState.getValues().entrySet()) {
            values.put(entry.getKey().getName(),propertyValue(entry.getKey(),entry.getValue()));
        }
        return new MaterialState(BuiltInRegistries.BLOCK.getKey(blockState.getBlock()),values);
    }

    public BlockState state() {
        Block block=BuiltInRegistries.BLOCK.getOptional(this.resourceLocation).orElse(Blocks.OAK_PLANKS);
        BlockState blockState=block.defaultBlockState();
        for (Map.Entry<String,String> entry : this.properties.entrySet()) {
            Property<?> property=block.getStateDefinition().getProperty(entry.getKey());
            if(property != null) blockState=apply(blockState,property,entry.getValue());
        }
        return blockState;
    }

    public CompoundTag save() {
        CompoundTag compoundTag=new CompoundTag();
        compoundTag.putString("Block",this.resourceLocation.toString());
        CompoundTag tag=new CompoundTag();
        this.properties.forEach(tag::putString);
        compoundTag.put("Properties",tag);
        return compoundTag;
    }

    public static MaterialState load(CompoundTag compoundTag) {
        ResourceLocation location=ResourceLocation.tryParse(compoundTag.getString("Block"));
        Map<String,String> stringMap=new LinkedHashMap<>();
        if(compoundTag.contains("Properties", Tag.TAG_COMPOUND)) {
            CompoundTag tag=compoundTag.getCompound("Properties");
            for (String string : tag.getAllKeys()) {
                stringMap.put(string,tag.getString(string));
            }
        }
        ResourceLocation resourceLocation=BuiltInRegistries.BLOCK.getKey(Blocks.OAK_PLANKS);
        return new MaterialState(location == null ? resourceLocation : location, stringMap);
    }

    private static <T extends Comparable<T>> String propertyValue(Property<T> property, Comparable<?> comparable) {
        return property.getName((T) comparable);
    }

    private static <T extends Comparable<T>> BlockState apply(BlockState blockState,Property<T> property,String string) {
        Optional<T> optionalT=property.getValue(string);
        return optionalT.map(result -> blockState.setValue(property,result)).orElse(blockState);
    }

    public ItemStack toItemStack() {
        BlockState blockState=this.state();
        ItemStack itemStack=new ItemStack(blockState.getBlock());
        if(itemStack.isEmpty() || itemStack.is(Items.AIR)) return ItemStack.EMPTY;
        CompoundTag compoundTag=new CompoundTag();
        this.properties.forEach(compoundTag::putString);
        if(!compoundTag.isEmpty()) itemStack.addTagElement("BlockStateTag",compoundTag);
        return itemStack;
    }
}
