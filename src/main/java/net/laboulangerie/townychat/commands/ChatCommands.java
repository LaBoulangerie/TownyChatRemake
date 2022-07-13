package net.laboulangerie.townychat.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyMessaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.player.ChatPlayer;

public class ChatCommands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_sender_not_player");
            sender.sendMessage(MiniMessage.miniMessage().deserialize(errorMessage));
            return true;
        }

        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(player);
        Set<String> channels = chatPlayer.getChannels().values().stream()
                .map(c -> c.getType().name().toLowerCase())
                .collect(Collectors.toSet());

        ChannelTypes channelType = null;

        if (channels.contains(args[0])) {
            channelType = ChannelTypes.valueOf(args[0].toUpperCase());
        } else {
            for (Channel channel : chatPlayer.getChannels().values()) {
                if (channel.getAliases().contains(args[0])) {
                    channelType = channel.getType();
                    break;
                }
            }
        }

        if (channelType != null) {

            chatPlayer.setCurrentChannel(channelType);

            Channel channel = chatPlayer.getChannel(channelType);

            if (!(chatPlayer.getActiveChannels().contains(channel))) {
                chatPlayer.toggleChannel(channelType);
            }

            String switchMessage = TownyChat.PLUGIN.getConfig().getString("lang.channel_switched");
            TextComponent switchMessageComponent = (TextComponent) MiniMessage.miniMessage().deserialize(switchMessage,
                    Placeholder.unparsed("channel", channel.getName()));

            sender.sendMessage("\n");
            TownyMessaging.sendMsg(sender, switchMessageComponent.content());
            sender.sendMessage("\n");

            return true;
        }

        String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_channel_not_found");
        TextComponent errorMessageComponent = (TextComponent) MiniMessage.miniMessage().deserialize(errorMessage,
                Placeholder.unparsed("channel", args[0]));

        TownyMessaging.sendErrorMsg(sender, errorMessageComponent.content());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return null;
        }

        Player player = (Player) sender;
        ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(player);

        List<String> channelTypes = new ArrayList<String>(
                chatPlayer.getChannels().keySet().stream().map(c -> c.name().toLowerCase())
                        .collect(Collectors.toList()));

        return args.length == 1
                ? channelTypes.stream().filter(id -> id.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList())
                : channelTypes;
    }
}
