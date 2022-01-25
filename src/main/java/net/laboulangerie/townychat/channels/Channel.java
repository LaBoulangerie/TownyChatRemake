package net.laboulangerie.townychat.channels;

import java.util.Collection;

import com.palmergames.bukkit.towny.object.Resident;

import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.core.TownyChatRenderer;
import net.laboulangerie.townychat.player.ChatPlayer;

public class Channel {
    private String id, name, format;

    public Channel(String id, String name, String tag) {
        this.id = id;
        this.name = name;
        this.format = tag;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public void sendMessage(Player source, Collection<Resident> residents, Component message) {
        for (Resident res : residents) {
            if (res != null && !res.isNPC() && res.isOnline()) {
                ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(res.getPlayer());
                Boolean isChannelActive = chatPlayer.getActiveChannels().contains(chatPlayer.getChannel(this.getId()));

                if (isChannelActive) {
                    TownyChatRenderer renderer = TownyChat.PLUGIN.getTownyChatRenderer();
                    Component messageComponent = renderer.render(source,
                            source.displayName(), message, source);

                    res.getPlayer().sendMessage(messageComponent);
                }
            }
        }
    }
}
