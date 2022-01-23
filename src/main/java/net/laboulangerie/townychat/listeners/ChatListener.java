package net.laboulangerie.townychat.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.laboulangerie.townychat.core.TownyChatRenderer;

public class ChatListener implements Listener {

    public ChatListener() {
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        TownyChatRenderer renderer = new TownyChatRenderer();
        event.renderer(renderer);
    }
}