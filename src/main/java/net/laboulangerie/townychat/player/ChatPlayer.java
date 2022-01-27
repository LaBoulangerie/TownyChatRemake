package net.laboulangerie.townychat.player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelManager;
import net.laboulangerie.townychat.channels.ChannelTypes;

public class ChatPlayer {
    private UUID uniqueId;

    private FileConfiguration config;
    private ChannelManager channelManager;

    private Channel currentChannel;
    private Map<ChannelTypes, Channel> channels;
    private Set<Channel> activeChannels;

    public ChatPlayer(OfflinePlayer player) {
        this.uniqueId = player.getUniqueId();

        this.config = TownyChat.PLUGIN.getConfig();
        this.channelManager = TownyChat.PLUGIN.getChannelManager();

        this.channels = new HashMap<ChannelTypes, Channel>();
        this.activeChannels = new HashSet<Channel>();

        loadChannels();
    }

    public UUID getUniqueId() {
        return this.uniqueId;
    }

    public void setCurrentChannel(Channel channel) {
        this.currentChannel = channel;
    }

    public Channel getCurrentChannel() {
        return this.currentChannel;
    }

    public Channel getChannel(ChannelTypes channelType) {
        return this.channels.get(channelType);
    }

    public Map<ChannelTypes, Channel> getChannels() {
        return this.channels;
    }

    public Set<Channel> getActiveChannels() {
        return this.activeChannels;
    }

    public boolean toggleChannel(ChannelTypes channelType) {
        Channel channel = channels.get(channelType);
        if (this.activeChannels.contains(channel)) {
            activeChannels.remove(channel);
            return false;
        } else {
            activeChannels.add(channel);
            return true;
        }
    }

    public void loadChannels() {
        ConfigurationSection channelsSection = config.getConfigurationSection("channels");

        this.activeChannels = new HashSet<Channel>();

        Resident resident = TownyUniverse.getInstance().getResident(this.getUniqueId());

        Town town = resident.getTownOrNull();
        Nation nation = resident.getNationOrNull();

        if (town != null && !(this.channelManager.getChannels().containsKey(town))) {

            Channel townChannel = new Channel(ChannelTypes.TOWN, channelsSection.getString("town.name"),
                    channelsSection.getString("town.format"));

            this.activeChannels.add(townChannel);
            this.channels.put(ChannelTypes.TOWN, townChannel);
            this.channelManager.addChannel(town, townChannel);
        }

        if (nation != null && !(this.channelManager.getChannels().containsKey(nation))) {

            Channel nationChannel = new Channel(ChannelTypes.NATION, channelsSection.getString("nation.name"),
                    channelsSection.getString("nation.format"));

            this.activeChannels.add(nationChannel);
            this.channels.put(ChannelTypes.NATION, nationChannel);
            this.channelManager.addChannel(nation, nationChannel);
        }

        Channel globalChannel = TownyChat.PLUGIN.getGlobalChannel();
        this.channels.put(ChannelTypes.GLOBAL, globalChannel);
        this.currentChannel = globalChannel;
    }
}
