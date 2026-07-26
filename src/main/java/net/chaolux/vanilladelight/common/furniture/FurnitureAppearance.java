package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureDefintionBuilder;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.minecraft.client.resources.model.Material;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class FurnitureAppearance {
    private ResourceLocation id;
    private String style;
    private boolean locked;
    private final Map<String, MaterialState> materials=new LinkedHashMap<>();
    private final Set<String> installMaterial=new LinkedHashSet<>();

    public FurnitureAppearance(FurnitureDefintion defintion) {
        this.reset(defintion);
    }

    public ResourceLocation id() {
        return this.id;
    }

    public FurnitureDefintion definition() {
        return FurnitureRegistry.get(this.id);
    }

    public String style() {
        return this.style;
    }

    public Map<String,MaterialState> materials() {
        return Map.copyOf(this.materials);
    }

    public boolean isLocked() {
        return this.locked;
    }

    public boolean lock() {
        if(this.locked) return false;
        this.locked=true;
        return true;
    }

    public boolean unlock() {
        if(!this.locked) return false;
        this.locked=false;
        return true;
    }

    public MaterialState material(String string) {
        MaterialState material=this.materials.get(string);
        if(material != null) return material;
        return this.definition().defaultMaterialState().get(string);
    }

    public boolean isInstallMaterial(String string) {
        return this.installMaterial.contains(string);
    }

    public Map<String,MaterialState> installMaterial() {
        Map<String,MaterialState> result=new LinkedHashMap<>();
        for(String string : this.installMaterial) {
            MaterialState materialState=this.materials.get(string);
            if(materialState != null) result.put(string,materialState);
        }
        return Map.copyOf(result);
    }

    public boolean setMaterial(String string, BlockState blockState) {
        if(this.locked) return false;
        if(!this.definition().defaultMaterialState().containsKey(string)) return false;
        MaterialState materialState=MaterialState.from(blockState);
        MaterialState state=this.material(string);
        if(this.installMaterial.contains(string) && materialState.equals(state)) return false;
        this.materials.put(string,materialState);
        this.installMaterial.add(string);
        return true;
    }

    public String cycleStyle() {
        if(this.locked) return this.style;
        List<String> styles=this.definition().styleList();
        int index=styles.indexOf(this.style);
        this.style=styles.get((index + 1) % styles.size());
        return this.style;
    }

    public FurnitureRenderData renderData(boolean open) {
        return new FurnitureRenderData(this.id,this.style,this.materials,open);
    }

    public CompoundTag save() {
        CompoundTag compoundTag=new CompoundTag();
        compoundTag.putString("Definition",this.id.toString());
        compoundTag.putString("Style",this.style);
        compoundTag.putBoolean("Locked",this.locked);
        CompoundTag materialsTag=new CompoundTag();
        this.materials.forEach((string,material) -> materialsTag.put(string,material.save()));
        compoundTag.put("Materials",materialsTag);
        CompoundTag installTag=new CompoundTag();
        for(String string : this.installMaterial) {
            installTag.putBoolean(string,true);
        }
        compoundTag.put("InstallMaterial",installTag);
        return compoundTag;
        }
        public void load(CompoundTag compoundTag,FurnitureDefintion defintion) {
        ResourceLocation id=ResourceLocation.tryParse(compoundTag.getString("Definition"));
        FurnitureDefintion defintions;
        if(id == null) {
            defintions=defintion;
        } else {
            defintions=FurnitureRegistry.findDefintions(id).orElse(defintion);
        }
        this.reset(defintions);
        String style=compoundTag.getString("Style");
        if(defintions.styles().containsKey(style)) this.style=style;
        this.locked=compoundTag.getBoolean("Locked");
        if(compoundTag.contains("Materials", Tag.TAG_COMPOUND)) {
            CompoundTag materialsTag=compoundTag.getCompound("Materials");
            for(String string : defintions.defaultMaterialState().keySet()) {
                if(materialsTag.contains(string,Tag.TAG_COMPOUND)) this.materials.put(string,MaterialState.load(materialsTag.getCompound(string)));
            }
        }
        if(compoundTag.contains("InstallMaterial",Tag.TAG_COMPOUND)) {
            CompoundTag tag=compoundTag.getCompound("InstallMaterial");
            for(String string : tag.getAllKeys()) {
                if(tag.getBoolean(string) && defintions.defaultMaterialState().containsKey(string)) {
                    this.installMaterial.add(string);
                }
            }
        } else {
            this.oldMaterial(defintions);
        }
    }

    private void reset(FurnitureDefintion defintion) {
        this.id=defintion.id();
        this.style=defintion.defaultStyle();
        this.locked=false;
        this.materials.clear();
        this.materials.putAll(defintion.defaultMaterialState());
        this.installMaterial.clear();
    }

    public void clearInstallMaterial() {
        FurnitureDefintion furnitureDefintion=this.definition();
        for (String string : this.installMaterial) {
            MaterialState materialState= furnitureDefintion.defaultMaterialState().get(string);
            if(materialState != null) this.materials.put(string,materialState);
        }
        this.installMaterial.clear();
    }

    private void oldMaterial(FurnitureDefintion furnitureDefintion) {
        for(Map.Entry<String,MaterialState> entry : this.materials.entrySet()) {
            MaterialState materialState= furnitureDefintion.defaultMaterialState().get(entry.getKey());
            if(materialState != null && !materialState.equals(entry.getValue())) this.installMaterial.add(entry.getKey());
        }
    }

}
