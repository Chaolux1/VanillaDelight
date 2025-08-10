package net.chaolux.vanilladelight.client.model;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.MOD, value = Dist.CLIENT)
public class ModelSwap {
    private static final String[] COLORS= {
            "default","black","white","light_gray","gray","brown","red","orange","yellow","lime","green","cyan","light_blue","blue","purple","magenta","pink"
    };
    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        for(String color: COLORS) {
            event.register(new ResourceLocation("vanilladelight", "block/cooking_pot_" + color));
            event.register(new ResourceLocation("vanilladelight", "block/cooking_pot_" + color + "_tray"));
            event.register(new ResourceLocation("vanilladelight", "block/cooking_pot_" + color + "_handle"));
        }
    }
    @SubscribeEvent
    public static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        event.getModels().replaceAll((id, model) -> {
            if("farmersdelight".equals(id.getNamespace())) {
                String path=id.getPath();
                if((path.equals("cooking_pot") || path.startsWith("cooking_pot#")) && !path.endsWith("#inventory")) {
                    return EmptyBakedModel.INSTANCE;
                }
            }
            return model;
        });
    }
}
