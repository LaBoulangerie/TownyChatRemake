package net.laboulangerie.townychat.listeners;

import com.palmergames.bukkit.towny.event.NationAddTownEvent;
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

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.Nullable;

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
        ChatPlayer chatPlayer = residentToChatPlayer(event.getResident());
        if (chatPlayer == null)
            return;
        chatPlayer.removeChannel(ChannelTypes.TOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTownLeaveNation(NationPreTownLeaveEvent event) {
        Town town = event.getTown();
        removeChannelFromResidents(town.getResidents(), ChannelTypes.NATION);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTownDelete(PreDeleteTownEvent event) {
        Town town = event.getTown();
        removeChannelFromResidents(town.getResidents(), ChannelTypes.TOWN);
        channelManager.removeChannel(town);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onNationDelete(PreDeleteNationEvent event) {
        Nation nation = event.getNation();
        removeChannelFromResidents(nation.getResidents(), ChannelTypes.NATION);
        channelManager.removeChannel(nation);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNewTown(NewTownEvent event) {
        Town town = event.getTown();
        Channel newTownChannel = new Channel(ChannelTypes.TOWN, town);
        channelManager.addChannel(town, newTownChannel);

        // The mayor is the only player in the town on town creation
        ChatPlayer chatPlayer = residentToChatPlayer(town.getMayor());
        if (chatPlayer == null)
            return;
        chatPlayer.addChannel(ChannelTypes.TOWN, newTownChannel);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNewNation(NewNationEvent event) {
        Nation nation = event.getNation();
        Channel newNationChannel = new Channel(ChannelTypes.NATION, nation);
        channelManager.addChannel(nation, newNationChannel);
        addChannelToResidents(nation.getResidents(), newNationChannel);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onTownAddResident(TownAddResidentEvent event) {
        ChatPlayer chatPlayer = residentToChatPlayer(event.getResident());
        if (chatPlayer == null)
            return;

        Town town = event.getTown();
        Channel townChannel = channelManager.getChannel(town);
        chatPlayer.addChannel(ChannelTypes.TOWN, townChannel);

        if (town.hasNation()) {
            Nation nation = town.getNationOrNull();
            Channel nationChannel = channelManager.getChannel(nation);
            chatPlayer.addChannel(ChannelTypes.NATION, nationChannel);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onNationAddTown(NationAddTownEvent event) {
        Town town = event.getTown();
        Nation nation = event.getNation();
        Channel nationChannel = channelManager.getChannel(nation);
        addChannelToResidents(town.getResidents(), nationChannel);
    }

    private @Nullable ChatPlayer residentToChatPlayer(Resident resident) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(resident.getUUID());

        if (!offlinePlayer.isOnline())
            return null;

        return chatPlayerManager.getChatPlayer(offlinePlayer.getPlayer());
    }

    private void addChannelToResidents(List<Resident> residents, Channel channel) {
        for (Resident resident : residents) {
            ChatPlayer chatPlayer = residentToChatPlayer(resident);
            if (chatPlayer == null)
                continue;
            chatPlayer.addChannel(channel.getType(), channel);
        }
    }

    private void removeChannelFromResidents(List<Resident> residents, ChannelTypes channelType) {
        for (Resident resident : residents) {
            ChatPlayer chatPlayer = residentToChatPlayer(resident);
            if (chatPlayer == null)
                continue;
            chatPlayer.removeChannel(channelType);
        }
    }
}
