package net.laboulangerie.townychat.channels;

public class Channel {
    private String name, format;
    private ChannelTypes type;

    public Channel(ChannelTypes type, String name, String format) {
        this.type = type;
        this.name = name;
        this.format = format;
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
}