package net.laboulangerie.townychat.channels;

import org.bukkit.configuration.ConfigurationSection;
import net.laboulangerie.townychat.TownyChat;

public class Channel {

    private ChannelTypes type;
    private ConfigurationSection channelSection;

    public Channel(ChannelTypes type) {
        this.type = type;

        String typeString = type.name().toLowerCase();

        this.channelSection = TownyChat.PLUGIN.getConfig()
                .getConfigurationSection("channels." + typeString);

    }

    public ChannelTypes getType() {
        return type;
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