package com.unloged.overlay;

import com.unloged.overlay.config.Config;
import com.unloged.overlay.config.OverlayGUI;
import com.unloged.overlay.mods.impl.BedwarsOverlay;
import com.unloged.overlay.utilities.MinecraftInstance;
import com.unloged.overlay.utilities.PlayerUtils;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EventListener implements MinecraftInstance {

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent e) {
        if (!PlayerUtils.nullCheck()) {
            return;
        }
        if (Main.gui) {
            mc.func_147108_a(new OverlayGUI());
            Main.gui = false;
        }
        if (e.phase == TickEvent.Phase.END) {
            if (Config.overlayEnabled) {
                BedwarsOverlay.onRenderTick();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent e) {
        if (e.phase == TickEvent.Phase.END && PlayerUtils.nullCheck()) {
            if (Config.overlayEnabled) {
                BedwarsOverlay.onTick();
            }
        }
    }

    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity == null || mc.field_71439_g == null) {
            return;
        }
        if (event.entity == mc.field_71439_g) {
            BedwarsOverlay.playersInLobby.clear();
        }
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent e) {
        if (e.type == 2 || !PlayerUtils.nullCheck()) {
            return;
        }
        final String r = PlayerUtils.stripString(e.message.func_150260_c());
        if (r.isEmpty()) {
            return;
        }
        if (r.startsWith(mc.field_71439_g.func_70005_c_() + " reconnected.")) {
            BedwarsOverlay.onChatEvent();
        }
    }
}
