package net.laboulangerie.townychat.player;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.kyori.adventure.text.Component;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.commands.ShortcutCommand;

public class ChatPlayer {
    private UUID uniqueId;

    private FileConfiguration config;

    private HashMap<String, Channel> channels;
    private Channel currentChannel;
    private HashSet<Channel> activeChannels;

    public ChatPlayer(OfflinePlayer player) {
        this.uniqueId = player.getUniqueId();

        config = TownyChat.PLUGIN.getConfig();

        loadChannels();
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public Channel getChannel(String id) {
        return this.channels.get(id);
    }

    public void setCurrentChannel(Channel channel) {
        this.currentChannel = channel;
    }

    public Channel getCurrentChannel() {
        return this.currentChannel;
    }

    public String toggleChannel(Channel channel) {
        String message;
        if (activeChannels.contains(channel)) {
            activeChannels.remove(channel);
            message = "Disabled " + channel.getName();
        } else {
            activeChannels.add(channel);
            message = "Enabled " + channel.getName();
        }

        return message;
    }

    public HashMap<String, Channel> getChannels() {
        return this.channels;
    }

    public HashSet<Channel> getActiveChannels() {
        return this.activeChannels;
    }

    public void loadChannels() {
        ConfigurationSection channelsSection = config.getConfigurationSection("channels");
        Set<String> channelIds = channelsSection.getKeys(false);

        channels = new HashMap<String, Channel>();
        activeChannels = new HashSet<Channel>();

        for (String channelId : channelIds) {
            Channel channel = new Channel(channelId,
                    channelsSection.getString(channelId + ".name"),
                    channelsSection.getString(channelId + ".format"));

            this.channels.put(channelId, channel);
            this.activeChannels.add(channel);

            String shortcutCommandName = channelsSection.getString(channelId + ".command");
            if (shortcutCommandName != null) {
                TownyChat.PLUGIN.registerCommand(shortcutCommandName, new ShortcutCommand(channel));
            }
        }
        this.currentChannel = channels.get("general");
    }

    public void sendMessage(Component message) {

        Resident resident = TownyChat.PLUGIN.getTownyAPI().getResident(this.getUniqueId());

        switch (this.getCurrentChannel().getId()) {
            case "town":
                Town town = resident.getTownOrNull();

                if (town == null)
                    return;

                this.getCurrentChannel().sendMessage(Bukkit.getPlayer(this.getUniqueId()), town.getResidents(),
                        message);
                break;

            case "nation":
                Nation nation = resident.getNationOrNull();

                if (nation == null)
                    return;

                this.getCurrentChannel().sendMessage(Bukkit.getPlayer(this.getUniqueId()), nation.getResidents(),
                        message);
                break;

            case "general":
                Collection<Resident> residents = TownyUniverse.getInstance().getResidents();
                this.getCurrentChannel().sendMessage(Bukkit.getPlayer(this.getUniqueId()), residents, message);

            default:
                break;
        }
    }
}
