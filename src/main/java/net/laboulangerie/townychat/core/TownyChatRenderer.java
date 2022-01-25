package net.laboulangerie.townychat.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.chat.ChatRenderer;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChatRenderer implements ChatRenderer {
    private FileConfiguration config;
    private ChatPlayerManager chatPlayerManager;

    public TownyChatRenderer() {
        this.config = TownyChat.PLUGIN.getConfig();
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
            @NotNull Component message,
            @NotNull Audience viewer) {

        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(source);
        String channelFormat = chatPlayer.getCurrentChannel().getFormat();

        String papiFormat = PlaceholderAPI.setPlaceholders(source, channelFormat);
        List<Template> templates = parseTemplates(source, message);

        Component parsedComponent = MiniMessage.get().parse(papiFormat, templates);

        return parsedComponent;
    }

    private List<Template> parseTemplates(Player player, Component message) {
        ConfigurationSection section = config.getConfigurationSection("templates");

        TextComponent textMessage = (TextComponent) message;
        Component messageComponent = MiniMessage.get().parse(textMessage.content());

        ArrayList<Template> templates = new ArrayList<>(Arrays.asList(
                Template.of("message", messageComponent)));

        for (String key : section.getKeys(false)) {

            String string = PlaceholderAPI.setPlaceholders(player, section.getString(key));
            Component component = MiniMessage.get().parse(string);

            templates.add(Template.of(key, component));
        }

        return templates;
    }
}
