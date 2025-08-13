package net.chaolux.vanilladelight.client.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.MOD, value = Dist.CLIENT)
public class ModelSwap {
    private static final String[] COLORS= {
            "default","black","white","light_gray","gray","brown","red","orange","yellow","lime","green","cyan","light_blue","blue","purple","magenta","pink"
    };
    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        for(String color: COLORS) {
            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("vanilladelight", "block/cooking_pot_" + color)));
            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("vanilladelight", "block/cooking_pot_" + color + "_tray")));
            event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("vanilladelight", "block/cooking_pot_" + color + "_handle")));
        }
        event.register(ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("vanilladelight","item/cooking_pot_default")));
    }
    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> {
            if("farmersdelight".equals(id.id().getNamespace()) && "cooking_pot".equals(id.id().getPath()) && !"inventory".equals(id.variant())) {
                    return EmptyBakedModel.INSTANCE;
                }
            return model;
        });

        var models=event.getModels();
        var vdKey= ModelResourceLocation.standalone(ResourceLocation.fromNamespaceAndPath("vanilladelight", "item/cooking_pot_default"));
        var vdItemModel=models.get(vdKey);
        if(vdItemModel !=null) {
            var fdKey=new ModelResourceLocation(ResourceLocation.fromNamespaceAndPath("farmersdelight","cooking_pot"),"inventory");
            models.put(fdKey,vdItemModel);
        }
    }
}
