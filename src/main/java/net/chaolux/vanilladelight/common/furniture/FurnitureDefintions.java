package net.chaolux.vanilladelight.common.furniture;

import net.chaolux.vanilladelight.VanillaDelight;
import net.chaolux.vanilladelight.api.furniture.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Set;

public class FurnitureDefintions {
    public static final ResourceLocation MODULAR_CABINET=new ResourceLocation(VanillaDelight.MOD_ID,"modular_cabinet");
    public static final ResourceLocation BODY_PLACEHOLDER=new ResourceLocation("minecraft","block/structure_block");
    public static final ResourceLocation TOP_PLACEHOLDER=new ResourceLocation("minecraft","block/jigsaw_top");
    public static final ResourceLocation ACCENT_PLACEHOLDER=new ResourceLocation("minecraft","block/command_block_front");
    private static boolean registered;
    private FurnitureDefintions() {

    }

    public static synchronized void registered() {
        if(registered) return;
        registered=true;
        EffectRule topEffect=new EffectRule(0xFFFFFFFF,0.96f,1.0f,false,null,0.0f,0.0f,1.0f,1.0f,0.0f,0.0f);
        EffectRule accentEffect=new EffectRule(0xFFFFFFFF,1.0f,1.0f,false,null,0.35f,0.0f,1.0f,1.0f,0.0f,0.0f);
        FurnitureDefintion defintion=FurnitureDefintion.builder(MODULAR_CABINET).material("body", Blocks.OAK_PLANKS).material("top",Blocks.SMOOTH_STONE).material("accent",Blocks.IRON_BLOCK).style(new StylePresent("single_door",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_single_door"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_single_door_open"))).style(new StylePresent("double_door",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_double_door"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_double_door_open"))).style(new StylePresent("three_drawers",new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_three_drawers"),new ResourceLocation(VanillaDelight.MOD_ID,"block/furniture/modular_cabinet_three_drawers_open"))).defaultStyle("single_door").part(new FurniturePart("body",BODY_PLACEHOLDER,"body", TextureMapping.DEFAULT, List.of(EffectRule.NONE))).part(new FurniturePart("top",TOP_PLACEHOLDER,"top",new TextureMapping(TextureMapping.Mode.PRESERVE_SCALE,1.0f,1.0f,0.0f,0.0f,0,false,false,0.0f,0.0f,1.0f,1.0f),List.of(topEffect))).part(new FurniturePart("accent",ACCENT_PLACEHOLDER,"accent",new TextureMapping(TextureMapping.Mode.STRETCH,1.0f,1.0f,0.0f,0.0f,0,false,false,0.0f,0.0f,1.0f,1.0f),List.of(accentEffect))).section(new FurnitureSection("accent","accent",aabb(10.5,5.0,0.0,13.5,8.0,1.0),Set.of("single_door"),40)).section(new FurnitureSection("accent","accent",aabb(5.5,5.0,0.0,10.5,8.0,1.0),Set.of("double_door"),40)).section(new FurnitureSection("accent","accent",aabb(5.5,0.0,0.0,10.5,12.0,1.0),Set.of("three_drawers"),40)).section(new FurnitureSection("top","top",aabb(0.0,12.0,0.0,16.0,16.01,16.0),Set.of(),20)).section(new FurnitureSection("body","body",aabb(0.0,0.0,0.0,16.0,12.0,16.0),Set.of(),10)).collision(aabb(0.0,0.0,0.0,16.0,16.0,16.0)).containerSize(27).build();
        FurnitureRegistry.register(defintion);
    }

    private static AABB aabb(double minX,double minY,double minZ,double maxX,double maxY, double maxZ) {
        return new AABB(minX / 16.0,minY / 16.0,minZ / 16.0,maxX / 16.0,maxY / 16.0, maxZ / 16.0);
    }

}
