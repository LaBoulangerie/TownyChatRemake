package net.laboulangerie.townychat.channels;

import org.bukkit.configuration.ConfigurationSection;
import net.laboulangerie.townychat.TownyChat;

public class Channel {

    private String name, format, spyFormat;
    private ChannelTypes type;

    public Channel(ChannelTypes type) {
        this.type = type;

        String typeString = type.name().toLowerCase();

        ConfigurationSection channelSection = TownyChat.PLUGIN.getConfig()
                .getConfigurationSection("channels." + typeString);

        this.name = channelSection.getString("name");
        this.format = channelSection.getString("format");
        this.spyFormat = channelSection.getString("spy_format");
    }

    public ChannelTypes getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public String getSpyFormat() {
        return spyFormat;
    }
}