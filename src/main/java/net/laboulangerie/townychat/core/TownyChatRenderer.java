package net.laboulangerie.townychat.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.chat.ChatRenderer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChatRenderer implements ChatRenderer.ViewerUnaware {
    private FileConfiguration config;
    private ChatPlayerManager chatPlayerManager;
    private ComponentRenderer componentRenderer;

    public TownyChatRenderer() {
        this.config = TownyChat.PLUGIN.getConfig();
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
        this.componentRenderer = TownyChat.PLUGIN.getComponentRenderer();
    }

    @Override
    public @NotNull Component render(@NotNull Player source, @NotNull Component sourceDisplayName,
            @NotNull Component message) {

        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(source);
        String channelFormat = chatPlayer.getCurrentChannel().getFormat();

        // Censor the message with the word blacklist
        message = Component.text(censorString(PlainTextComponentSerializer.plainText().serialize(message)));

        if (source.hasPermission("townychat.format")) {
            // Format the message using MiniMessage
            TextComponent textMessage = (TextComponent) message;
            message = MiniMessage.miniMessage().deserialize(textMessage.content());
        }

        List<TagResolver.Single> resolvers = new ArrayList<>();
        resolvers.add(Placeholder.component("message", message));
        resolvers.add(Placeholder.component("username", source.name()));

        return componentRenderer.parse(source, channelFormat, TagResolver.resolver(resolvers));
    }

    // TODO : Remove redundant method, but how???
    public @NotNull Component spyRender(@NotNull Player source, @NotNull Component message) {

        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(source);
        String channelFormat = chatPlayer.getCurrentChannel().getSpyFormat();

        if (source.hasPermission("townychat.format")) {
            TextComponent textMessage = (TextComponent) message;
            message = MiniMessage.miniMessage().deserialize(textMessage.content());
        }

        List<TagResolver.Single> resolvers = new ArrayList<>();
        resolvers.add(Placeholder.component("message", message));
        resolvers.add(Placeholder.component("username", source.name()));

        return componentRenderer.parse(source, channelFormat, TagResolver.resolver(resolvers));
    }

    private String censorString(String string) {
        List<String> words = config.getStringList("blacklist");
        String[] censorChars = { "#", "@", "!", "*" };

        for (String word : words) {
            // Not readable to say the least but i like it
            // It generates a random string e.g. insult -> !#@!*#
            // Yes it's overcomplicated but it looks cool :)
            string = string.replaceAll("(?i)" + Pattern.quote(word),
                    new Random().ints(word.length(), 0, censorChars.length).mapToObj(i -> censorChars[i])
                            .collect(Collectors.joining()));
        }

        return string;
    }
}