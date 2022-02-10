package net.laboulangerie.townychat.listeners;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
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

import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.core.TownyChatRenderer;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChatListener implements Listener {
    private ChatPlayerManager chatPlayerManager;
    private TownyChatRenderer townyChatRenderer;
    private TownyAPI townyAPI;

    public TownyChatListener() {
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
        this.townyChatRenderer = TownyChat.PLUGIN.getTownyChatRenderer();
        this.townyAPI = TownyChat.PLUGIN.getTownyAPI();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncChatEvent event) {
        event.renderer(ChatRenderer.viewerUnaware(townyChatRenderer));
        event.viewers().clear();

        Player player = event.getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        Channel currentChannel = chatPlayer.getCurrentChannel();

        if (!(chatPlayer.getActiveChannels().contains(currentChannel))) {
            String errMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_channel_disabled");

            TextComponent switchMessageComponent = (TextComponent) MiniMessage.get().parse(errMessage,
                    Template.of("channel", currentChannel.getType().name().toLowerCase()));

            TownyMessaging.sendErrorMsg(player, switchMessageComponent.content());
            event.setCancelled(true);
            return;
        }

        Resident resident = townyAPI.getResident(player.getUniqueId());

        if (chatPlayer.getCurrentChannel().getType() != ChannelTypes.GLOBAL) {
            for (ChatPlayer spy : chatPlayerManager.getSpies()) {
                Player spyPlayer = Bukkit.getPlayer(spy.getUniqueId());

                if (shouldSpy(spyPlayer, player)) {
                    spyPlayer.sendMessage(townyChatRenderer.spyRender(player, event.message()));
                }
            }
        }

        Set<Resident> recipients = new HashSet<>();

        switch (currentChannel.getType()) {
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

    private Boolean shouldSpy(Player spy, Player spied) {
        Resident spyRes = townyAPI.getResident(spy.getUniqueId());
        Resident spiedRes = townyAPI.getResident(spied.getUniqueId());

        return spyRes.getTownOrNull() != spiedRes.getTownOrNull()
                || spyRes.getNationOrNull() != spiedRes.getNationOrNull();
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