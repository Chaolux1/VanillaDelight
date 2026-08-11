package net.chaolux.vanilladelight.api.furniture;

import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FurnitureDefintionBuilder {
    private final ResourceLocation id;
    private final Map<String, MaterialState> defaultMaterialState=new LinkedHashMap<>();
    private final Map<String,StylePresent> styles=new LinkedHashMap<>();
    private final Map<ResourceLocation,FurniturePart> parts=new LinkedHashMap<>();
    private final List<FurnitureSection> sections=new ArrayList<>();
    private final List<AABB> collision=new ArrayList<>();
    private String defaultStyle;
    private int containerSize;
    FurnitureDefintionBuilder(ResourceLocation id) {
        this.id=id;
    }

    public FurnitureDefintionBuilder material(String string, Block block) {
        return this.material(string,block.defaultBlockState());
    }

    public FurnitureDefintionBuilder material(String string, BlockState blockState) {
        this.defaultMaterialState.put(string, MaterialState.from(blockState));
        return this;
    }

    public FurnitureDefintionBuilder style(StylePresent style) {
        if(this.defaultStyle == null) this.defaultStyle = style.id();
        this.styles.put(style.id(),style);
        return this;
    }

    public FurnitureDefintionBuilder defaultStyle(String string) {
        this.defaultStyle=string;
        return this;
    }

    public FurnitureDefintionBuilder part(FurniturePart part) {
        this.parts.put(part.placeholder(),part);
        return this;
    }

    public FurnitureDefintionBuilder section(FurnitureSection section) {
        this.sections.add(section);
        return this;
    }

    public FurnitureDefintionBuilder collision(AABB aabb) {
        this.collision.add(aabb);
        return this;
    }

    public FurnitureDefintionBuilder containerSize(int size) {
        this.containerSize=size;
        return this;
    }

    public FurnitureDefintion build() {
        if(this.styles.isEmpty()) throw new IllegalStateException("Furniture definition requires at latest one style");
        if(this.defaultStyle == null || !this.styles.containsKey(this.defaultStyle)) throw new IllegalStateException("Furniture definition has invalid default style");
        for(FurnitureSection section : this.sections) {
            if(!this.defaultMaterialState.containsKey(section.material())) throw new IllegalStateException("Unknown material channel: " + section.material());
        }
        for(FurniturePart part : this.parts.values()) {
            if(!this.defaultMaterialState.containsKey(part.material())) throw new IllegalStateException("Unknown material channel: " + part.material());
        }
        return new FurnitureDefintion(this.id,this.defaultStyle,this.defaultMaterialState,this.styles,this.parts,this.sections,this.collision,this.containerSize);
    }
}