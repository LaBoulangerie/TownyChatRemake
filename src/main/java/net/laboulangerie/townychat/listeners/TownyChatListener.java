package net.laboulangerie.townychat.listeners;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.core.TownyChatRenderer;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChatListener implements Listener {
    private ChatPlayerManager chatPlayerManager;
    private TownyChatRenderer townyChatRenderer;

    public TownyChatListener() {
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
        this.townyChatRenderer = TownyChat.PLUGIN.getTownyChatRenderer();
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        if (event.isCancelled())
            return;

        event.renderer(townyChatRenderer);
        event.viewers().clear();

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        Resident resident = TownyChat.PLUGIN.getTownyAPI().getResident(player.getUniqueId());

        Set<Resident> recipients = new HashSet<>();

        switch (chatPlayer.getCurrentChannel().getType()) {
            case TOWN:
                Town town = resident.getTownOrNull();

                if (town == null)
                    return;

                recipients.addAll(town.getResidents());
                break;

            case NATION:
                Nation nation = resident.getNationOrNull();

                if (nation == null)
                    return;

                recipients.addAll(nation.getResidents());
                break;

            case GLOBAL:
                recipients.addAll(TownyUniverse.getInstance().getResidents());
            default:
                break;
        }

        event.viewers().addAll(
                recipients.stream().map(Resident::getPlayer).filter(Objects::nonNull).collect(Collectors.toSet()));
        event.viewers().add(Bukkit.getConsoleSender());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        chatPlayerManager.loadChatPlayer(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        chatPlayerManager.unloadChatPlayer(player);

    }
}