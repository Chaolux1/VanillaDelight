package net.chaolux.vanilladelight.api.furniture;

import net.chaolux.vanilladelight.common.furniture.MaterialState;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.*;

public class FurnitureDefintion {
    private final ResourceLocation id;
    private final String defaultStyle;
    private final Map<String, MaterialState> defaultMaterialState;
    private final Map<String,StylePresent> styles;
    private final Map<ResourceLocation,FurniturePart> parts;
    private final List<FurnitureSection> sections;
    private final List<AABB> collision;
    private final int containerSize;
    private final Map<Direction, VoxelShape> shapes;
    FurnitureDefintion(ResourceLocation id,String defaultStyle,Map<String, MaterialState> defaultMaterialState,Map<String,StylePresent> styles,Map<ResourceLocation,FurniturePart> parts,List<FurnitureSection> sections,List<AABB> collision,int containerSize) {
        this.id=id;
        this.defaultStyle=defaultStyle;
        this.defaultMaterialState=Collections.unmodifiableMap(new LinkedHashMap<>(defaultMaterialState));
        this.styles=Collections.unmodifiableMap(new LinkedHashMap<>(styles));
        this.parts=Collections.unmodifiableMap(new LinkedHashMap<>(parts));
        this.sections=sections.stream().sorted(Comparator.comparingInt(FurnitureSection::priority).reversed()).toList();
        this.collision=List.copyOf(collision);
        this.containerSize=Math.max(0,containerSize);
        this.shapes=new EnumMap<>(Direction.class);
        this.shapes.put(Direction.NORTH,this.createShape(Direction.NORTH));
        this.shapes.put(Direction.EAST,this.createShape(Direction.EAST));
        this.shapes.put(Direction.WEST,this.createShape(Direction.WEST));
        this.shapes.put(Direction.SOUTH,this.createShape(Direction.SOUTH));
    }

    public ResourceLocation id() {
        return this.id;
    }

    public String defaultStyle() {
        return this.defaultStyle;
    }

    public Map<String, MaterialState> defaultMaterialState() {
        return this.defaultMaterialState;
    }

    public Map<String,StylePresent> styles() {
        return this.styles;
    }

    public List<String> styleList() {
        return new ArrayList<>(this.styles.keySet());
    }

    public StylePresent stylePreset(String string) {
        return this.styles.getOrDefault(string,this.styles.get(this.defaultStyle));
    }

    public Map<ResourceLocation,FurniturePart> parts() {
        return this.parts;
    }

    public FurniturePart part(ResourceLocation resourceLocation) {
        return this.parts.get(resourceLocation);
    }

    public List<FurnitureSection> sections() {
        return this.sections;
    }

    public List<AABB> collision() {
        return this.collision;
    }

    public int containerSize() {
        return this.containerSize;
    }

    public String sectionTranslationKey(String string) {
        return "section." + this.id.getNamespace() + "." + this.id.getPath() + "." + string;
    }

    public String styleTranslationKey(String string) {
        return "style." + this.id.getNamespace() + "." + this.id.getPath() + "." + string;
    }

    public VoxelShape shape(Direction direction) {
        return this.shapes.getOrDefault(direction,this.shapes.get(Direction.NORTH));
    }

    private VoxelShape createShape(Direction direction) {
        VoxelShape shape=Shapes.empty();
        for(AABB aabb : this.collision) {
            AABB rotated=rotate(aabb,direction);
            shape=Shapes.or(shape,Shapes.create(rotated));
        }
        if(shape.isEmpty()) return Shapes.block();
        return shape.optimize();
    }

    private static AABB rotate(AABB aabb,Direction direction) {
        return switch (direction) {
            case EAST -> new AABB(1.0 - aabb.maxZ,aabb.minY,aabb.minX,1.0 - aabb.minZ,aabb.maxY,aabb.maxX);
            case SOUTH -> new AABB(1.0 - aabb.maxX,aabb.minY,1.0 - aabb.maxZ,1.0 - aabb.minX,aabb.maxY,1.0 - aabb.minZ);
            case WEST -> new AABB(aabb.minZ,aabb.minY,1.0 - aabb.maxX,aabb.maxZ,aabb.maxY,1.0 - aabb.minX);
            default -> aabb;
        };
    }

    public static FurnitureDefintionBuilder builder(ResourceLocation resourceLocation) {
        return new FurnitureDefintionBuilder(resourceLocation);
    }
}
