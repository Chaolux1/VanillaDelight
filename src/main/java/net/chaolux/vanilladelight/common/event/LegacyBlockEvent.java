package net.chaolux.vanilladelight.common.event;

import net.chaolux.vanilladelight.common.utility.LegacyBlockInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.FORGE)
public class LegacyBlockEvent {
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player=event.getEntity();
        if(player.level().isClientSide) return;
        CompoundTag compoundTag=player.getPersistentData();
        CompoundTag tag=compoundTag.getCompound(Player.PERSISTED_NBT_TAG);
        if(tag.getBoolean(LegacyBlockInfo.VERSION_KEY)) return;
        tag.putBoolean(LegacyBlockInfo.VERSION_KEY,true);
        compoundTag.put(Player.PERSISTED_NBT_TAG,tag);
        player.sendSystemMessage(LegacyBlockInfo.join());
    }
}
