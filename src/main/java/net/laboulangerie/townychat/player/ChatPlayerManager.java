package net.laboulangerie.townychat.player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ChatPlayerManager {
    private Map<UUID, ChatPlayer> playersMap;

    public ChatPlayerManager() {
        this.playersMap = new HashMap<>();

        Bukkit.getOnlinePlayers().stream().forEach(p -> loadChatPlayer(p));
    }

    public void loadChatPlayer(Player player) {
        ChatPlayer chatPlayer = new ChatPlayer(player);
        this.playersMap.put(player.getUniqueId(), chatPlayer);
    }

    public void unloadChatPlayer(Player player) {
        this.playersMap.remove(player.getUniqueId());
    }

    public ChatPlayer getChatPlayer(Player player) {
        return this.playersMap.get(player.getUniqueId());
    }

    public Set<ChatPlayer> getSpies() {
        return playersMap.values().stream().filter(ChatPlayer::isSpying).collect(Collectors.toSet());
    }
}
