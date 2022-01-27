package net.laboulangerie.townychat.channels;

import java.util.HashMap;
import java.util.Map;

import com.palmergames.bukkit.towny.object.Government;

public class ChannelManager {

    private Map<Government, Channel> channelsMap;

    public ChannelManager() {
        this.channelsMap = new HashMap<>();
    }

    public Channel getChannel(Government government) {
        return this.channelsMap.get(government);
    }

    public Map<Government, Channel> getChannels() {
        return this.channelsMap;
    }

    public void addChannel(Government government, Channel channel) {
        this.channelsMap.put(government, channel);
    }
}
