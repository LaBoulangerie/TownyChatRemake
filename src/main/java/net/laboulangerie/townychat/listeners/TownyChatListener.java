package net.laboulangerie.townychat.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChatListener implements Listener {
    private ChatPlayerManager chatPlayerManager;

    public TownyChatListener() {
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);
        event.setCancelled(true);
        chatPlayer.sendMessage(event.message());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        chatPlayerManager.loadPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        chatPlayerManager.unloadPlayer(player);

    }
}