package net.laboulangerie.townychat.listeners;

import com.palmergames.bukkit.towny.event.NewNationEvent;
import com.palmergames.bukkit.towny.event.NewTownEvent;
import com.palmergames.bukkit.towny.event.PreDeleteNationEvent;
import com.palmergames.bukkit.towny.event.PreDeleteTownEvent;
import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import com.palmergames.bukkit.towny.event.nation.NationPreTownLeaveEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelManager;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyListener implements Listener {
    private ChannelManager channelManager;
    private ChatPlayerManager chatPlayerManager;

    public TownyListener() {
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
        this.channelManager = TownyChat.PLUGIN.getChannelManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTownRemoveResident(TownRemoveResidentEvent event) {
        Player player = event.getResident().getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        Channel currentChannel = chatPlayer.getCurrentChannel();

        if (currentChannel.getType() == ChannelTypes.TOWN) {
            chatPlayer.setCurrentChannel(ChannelTypes.GLOBAL);
        }

        chatPlayer.removeChannel(ChannelTypes.TOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTownLeaveNation(NationPreTownLeaveEvent event) {
        Town town = event.getTown();

        for (Resident resident : town.getResidents()) {

            Player player = resident.getPlayer();
            ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

            Channel currentChannel = chatPlayer.getCurrentChannel();

            if (currentChannel.getType() == ChannelTypes.NATION) {
                chatPlayer.setCurrentChannel(ChannelTypes.GLOBAL);
            }

            chatPlayer.removeChannel(ChannelTypes.NATION);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTownDelete(PreDeleteTownEvent event) {
        Town town = event.getTown();

        for (Resident resident : town.getResidents()) {

            Player player = resident.getPlayer();
            ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

            Channel currentChannel = chatPlayer.getCurrentChannel();

            if (currentChannel.getType() == ChannelTypes.TOWN) {
                chatPlayer.setCurrentChannel(ChannelTypes.GLOBAL);
            }

            chatPlayer.removeChannel(ChannelTypes.TOWN);
        }

        channelManager.removeChannel(town);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNationDelete(PreDeleteNationEvent event) {
        Nation nation = event.getNation();

        for (Resident resident : nation.getResidents()) {

            Player player = resident.getPlayer();
            ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

            Channel currentChannel = chatPlayer.getCurrentChannel();

            if (currentChannel.getType() == ChannelTypes.NATION) {
                chatPlayer.setCurrentChannel(ChannelTypes.GLOBAL);
            }

            chatPlayer.removeChannel(ChannelTypes.NATION);
        }
        channelManager.removeChannel(nation);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNewTown(NewTownEvent event) {
        Town town = event.getTown();

        Channel newTownChannel = new Channel(ChannelTypes.TOWN);
        channelManager.addChannel(town, newTownChannel);

        // The mayor is the only player in the town on town creation
        Player player = town.getMayor().getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        chatPlayer.addChannel(ChannelTypes.TOWN, newTownChannel);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNewNation(NewNationEvent event) {
        Nation nation = event.getNation();

        Channel newNationChannel = new Channel(ChannelTypes.NATION);
        channelManager.addChannel(nation, newNationChannel);

        for (Resident resident : nation.getResidents()) {
            Player player = resident.getPlayer();
            ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

            chatPlayer.addChannel(ChannelTypes.NATION, newNationChannel);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTownAddResident(TownAddResidentEvent event) {
        Player player = event.getResident().getPlayer();
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        Town town = event.getTown();
        Channel townChannel = channelManager.getChannel(town);
        chatPlayer.addChannel(ChannelTypes.TOWN, townChannel);

        if (town.hasNation()) {
            Nation nation = town.getNationOrNull();
            Channel nationChannel = channelManager.getChannel(nation);

            chatPlayer.addChannel(ChannelTypes.NATION, nationChannel);

        }
    }
}
