package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.api.furniture.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Set;

public class FurnitureDefinitions {
    public static final ResourceLocation MODULAR_CABINET=new ResourceLocation(VanillaDelight.MOD_ID,"modular_cabinet");
    public static final ResourceLocation PATTERNED_CABINET=new ResourceLocation(VanillaDelight.MOD_ID,"patterned_cabinet");
    public static final ResourceLocation MODULAR_CUTTING_BOARD=new ResourceLocation(VanillaDelight.MOD_ID,"modular_cutting_board");
    public static final ResourceLocation MODULAR_STOVE=new ResourceLocation(VanillaDelight.MOD_ID,"modular_stove");
    public static final ResourceLocation BODY_PLACEHOLDER=new ResourceLocation("minecraft","block/structure_block");
    public static final ResourceLocation TOP_PLACEHOLDER=new ResourceLocation("minecraft","block/jigsaw_top");
    public static final ResourceLocation ACCENT_PLACEHOLDER=new ResourceLocation("minecraft","block/command_block_front");
    private static boolean registered;
    private FurnitureDefinitions() {

    }

    public static synchronized void registered() {
        if(registered) return;
        registered=true;
        registerModularCabinet();
        registerPatternedCabinet();
        registerModularCuttingBoard();
        registerModularStove();
    }

    private static void registerModularCabinet() {
        EffectRule topEffect=new EffectRule(0xFFFFFFFF,0.96f,1.0f,false,null,0.0f,0.0f,1.0f,1.0f,0.0f,0.0f);
        EffectRule accentEffect=new EffectRule(0xFFFFFFFF,1.0f,1.0f,false,null,0.35f,0.0f,1.0f,1.0f,0.0f,0.0f);
        FurnitureDefintion definition=FurnitureDefintion.builder(MODULAR_CABINET).material("body", Blocks.OAK_PLANKS).material("top",Blocks.SMOOTH_STONE).material("accent",Blocks.IRON_BLOCK).style(new StylePresent("single_door",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_single_door"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_single_door_open"))).style(new StylePresent("double_door",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_double_door"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_double_door_open"))).style(new StylePresent("three_drawers",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_three_drawers"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_three_drawers_open"))).defaultStyle("single_door").part(new FurniturePart("body",BODY_PLACEHOLDER,"body", TextureMapping.DEFAULT, List.of(EffectRule.NONE))).part(new FurniturePart("top",TOP_PLACEHOLDER,"top",new TextureMapping(TextureMapping.Mode.PRESERVE_SCALE,1.0f,1.0f,0.0f,0.0f,0,false,false,0.0f,0.0f,1.0f,1.0f),List.of(topEffect))).part(new FurniturePart("accent",ACCENT_PLACEHOLDER,"accent",new TextureMapping(TextureMapping.Mode.STRETCH,1.0f,1.0f,0.0f,0.0f,0,false,false,0.0f,0.0f,1.0f,1.0f),List.of(accentEffect))).section(new FurnitureSection("accent","accent",aabb(10.5,5.0,0.0,13.5,8.0,1.0),Set.of("single_door"),40)).section(new FurnitureSection("accent","accent",aabb(5.5,5.0,0.0,10.5,8.0,1.0),Set.of("double_door"),40)).section(new FurnitureSection("accent","accent",aabb(5.5,0.0,0.0,10.5,12.0,1.0),Set.of("three_drawers"),40)).section(new FurnitureSection("top","top",aabb(0.0,12.0,0.0,16.0,16.01,16.0),Set.of(),20)).section(new FurnitureSection("body","body",aabb(0.0,0.0,0.0,16.0,12.0,16.0),Set.of(),10)).collision(aabb(0.0,0.0,0.0,16.0,16.0,16.0)).containerSize(27).build();
        FurnitureRegistry.register(definition);
    }

    private static void registerPatternedCabinet() {
        FurnitureDefintionBuilder defintionBuilder=FurnitureDefintion.builder(PATTERNED_CABINET).material("body",Blocks.OAK_PLANKS).material("accent",Blocks.IRON_BLOCK).style(new StylePresent("acacia",null,null)).style(new StylePresent("bamboo",null,null)).style(new StylePresent("birch",null,null)).style(new StylePresent("cherry",null,null)).style(new StylePresent("crimson",null,null)).style(new StylePresent("dark_oak",null,null)).style(new StylePresent("jungle",null,null)).style(new StylePresent("mangrove",null,null)).style(new StylePresent("oak",null,null)).style(new StylePresent("spruce",null,null)).style(new StylePresent("warped",null,null)).defaultStyle("oak");
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(6.0,6.0,0.0,10.0,10.0,1.0),Set.of("acacia"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(0.0,5.0,0.0,2.0,11.0,1.0),Set.of("acacia"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(14.0,5.0,0.0,16.0,11.0,1.0),Set.of("acacia"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(7.0,9.0,0.0,9.0,10.0,1.0),Set.of("bamboo"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(6.0,7.0,0.0,7.0,9.0,1.0),Set.of("bamboo"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(9.0,7.0,0.0,10.0,9.0,1.0),Set.of("bamboo"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(7.0,6.0,0.0,9.0,7.0,1.0),Set.of("bamboo"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(5.0,5.0,0.0,7.0,11.0,1.0),Set.of("birch"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(9.0,5.0,0.0,11.0,11.0,1.0),Set.of("birch"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(0.0,5.0,0.0,2.0,11.0,1.0),Set.of("birch"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(14.0,5.0,0.0,16.0,11.0,1.0),Set.of("birch"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(5.0,7.0,0.0,11.0,9.0,1.0),Set.of("cherry"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(0.0,5.0,0.0,2.0,11.0,1.0),Set.of("cherry"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(14.0,5.0,0.0,16.0,11.0,1.0),Set.of("cherry"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(3.0,2.0,0.0,6.0,3.0,1.0),Set.of("dark_oak"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(10.0,2.0,0.0,13.0,3.0,1.0),Set.of("dark_oak"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(2.0,2.0,0.0,6.0,4.0,1.0),Set.of("jungle"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(10.0,2.0,0.0,14.0,4.0,1.0),Set.of("jungle"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(5.0,5.0,0.0,7.0,10.0,1.0),Set.of("oak"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(9.0,5.0,0.0,11.0,10.0,1.0),Set.of("oak"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(5.0,7.0,0.0,7.0,10.0,1.0),Set.of("spruce"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(9.0,7.0,0.0,11.0,10.0,1.0),Set.of("spruce"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(0.0,5.0,0.0,2.0,11.0,1.0),Set.of("spruce"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(14.0,5.0,0.0,16.0,11.0,1.0),Set.of("spruce"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(2.0,12.0,0.0,6.0,14.0,1.0),Set.of("warped"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(10.0,12.0,0.0,14.0,14.0,1.0),Set.of("warped"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(6.0,6.0,0.0,7.0,10.0,1.0),Set.of("crimson"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(9.0,6.0,0.0,10.0,10.0,1.0),Set.of("crimson"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(5.0,7.0,0.0,6.0,9.0,1.0),Set.of("crimson"),40));
        defintionBuilder.section(new FurnitureSection("accent","accent",aabb(10.0,7.0,0.0,11.0,9.0,1.0),Set.of("crimson"),40));
        defintionBuilder.section(new FurnitureSection("body","body",aabb(0.0,0.0,0.0,16.0,16.0,16.0),Set.of(),10));
        defintionBuilder.collision(aabb(0.0,0.0,0.0,16.0,16.0,16.0)).containerSize(27);
        FurnitureRegistry.register(defintionBuilder.build());
    }

    private static void registerModularCuttingBoard() {
        FurnitureDefintion furnitureDefintion=FurnitureDefintion.builder(MODULAR_CUTTING_BOARD).material("body",Blocks.OAK_PLANKS).style(new StylePresent("plain",null,null)).style(new StylePresent("spiral",null,null)).style(new StylePresent("frame",null,null)).style(new StylePresent("tiles",null,null)).style(new StylePresent("lines",null,null)).style(new StylePresent("diamond",null,null)).defaultStyle("plain").containerSize(0).build();
        FurnitureRegistry.register(furnitureDefintion);
    }

    private static void registerModularStove() {
        FurnitureDefintion furnitureDefinitions=FurnitureDefintion.builder(MODULAR_STOVE).material("body",Blocks.BRICKS).style(new StylePresent("orange",null,null)).style(new StylePresent("blue",null,null)).style(new StylePresent("purple",null,null)).style(new StylePresent("green",null,null)).style(new StylePresent("rainbow",null,null)).defaultStyle("orange").section(new FurnitureSection("body","body",aabb(0.0,0.0,0.0,16.0,16.0,16.0),Set.of(),10)).collision(aabb(0.0,0.0,0.0,16.0,16.0,16.0)).containerSize(0).build();
        FurnitureRegistry.register(furnitureDefinitions);
    }

    private static AABB aabb(double minX,double minY,double minZ,double maxX,double maxY, double maxZ) {
        return new AABB(minX / 16.0,minY / 16.0,minZ / 16.0,maxX / 16.0,maxY / 16.0, maxZ / 16.0);
    }

}
