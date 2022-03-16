package net.laboulangerie.townychat.channels;

import com.palmergames.bukkit.towny.object.Government;

import org.bukkit.configuration.ConfigurationSection;

import net.laboulangerie.townychat.TownyChat;

public class Channel {

    private String id;
    private ChannelTypes type;
    private Government government;
    private ConfigurationSection channelSection;

    public Channel(ChannelTypes type, Government government) {
        this.type = type;
        this.government = government;

        String typeString = type.name().toLowerCase();

        this.channelSection = TownyChat.PLUGIN.getConfig()
                .getConfigurationSection("channels." + typeString);

        this.id = government == null
                ? type.name().toLowerCase()
                : type.name().toLowerCase() + "-" + government.getName().toLowerCase();
    }

    public String getId() {
        return id;
    }

    public ChannelTypes getType() {
        return type;
    }

    public Government getGovernment() {
        return government;
    }

    public String getName() {
        return channelSection.getString("name");
    }

    public String getFormat() {
        return channelSection.getString("format");
    }

    public String getSpyFormat() {
        return channelSection.getString("spy_format");
    }
}