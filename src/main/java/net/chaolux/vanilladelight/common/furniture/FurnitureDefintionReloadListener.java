package net.chaolux.vanilladelight.common.furniture;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.chaolux.vanilladelight.api.furniture.FurnitureDefintion;
import net.chaolux.vanilladelight.api.furniture.FurnitureRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

public class FurnitureDefintionReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON=new GsonBuilder().create();
    private static final Logger LOGGER= LogUtils.getLogger();

    public FurnitureDefintionReloadListener() {
        super(GSON,"furniture");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, FurnitureDefintion> defintionMap=new LinkedHashMap<>();
        jsonElementMap.forEach((id,element) -> {
            try {
                JsonObject jsonObject=element.getAsJsonObject();
                FurnitureDefintion defintion=FurnitureDefintionJsonParser.parse(id,jsonObject);
                defintionMap.put(id,defintion);
            } catch (RuntimeException runtimeException) {
                LOGGER.error("Failed to load furniture definition {}",id,runtimeException);
            }
        });
        FurnitureRegistry.replaceLoaded(defintionMap);
    }
}
