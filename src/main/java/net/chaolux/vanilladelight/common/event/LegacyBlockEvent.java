package net.chaolux.vanilladelight.common.event;

import net.chaolux.vanilladelight.common.utility.LegacyBlockInfo;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.EventBusSubscriber.Bus;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = "vanilladelight", bus = Bus.GAME)
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
