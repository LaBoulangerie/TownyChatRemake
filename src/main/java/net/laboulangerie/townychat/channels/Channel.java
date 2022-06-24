package net.laboulangerie.townychat.channels;

import com.palmergames.bukkit.towny.object.Government;

import net.laboulangerie.townychat.TownyChat;

public class Channel {

    private String id;
    private ChannelTypes type;
    private Government government;

    public Channel(ChannelTypes type, Government government) {
        this.type = type;
        this.government = government;
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
        return getParameter("name");
    }

    public String getFormat() {
        return getParameter("format");
    }

    public String getSpyFormat() {
        return getParameter("spy_format");
    }

    private String getParameter(String parameter) {
        return TownyChat.PLUGIN.getConfig().getConfigurationSection("channels." + this.type.name().toLowerCase())
                .getString(parameter);
    }
}