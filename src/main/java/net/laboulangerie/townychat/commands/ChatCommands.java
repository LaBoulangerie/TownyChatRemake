package net.laboulangerie.townychat.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
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
            sender.sendMessage(MiniMessage.get().parse(errorMessage));
            return true;
        }

        if (args.length == 0)
            return false;

        Player player = (Player) sender;
        ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(player);

        if (TownyChat.PLUGIN.getConfig().getConfigurationSection("channels").getKeys(false)
                .contains(args[0].toLowerCase())) {

            TownyAPI townyAPI = TownyChat.PLUGIN.getTownyAPI();
            Resident resident = townyAPI.getResident(player);

            switch (args[0].toLowerCase()) {
                case "town":
                    if (!resident.hasTown()) {
                        TownyMessaging.sendErrorMsg(sender, Translatable.of("msg_err_dont_belong_town"));
                        return true;
                    }
                    break;

                case "nation":
                    if (!resident.hasNation()) {
                        TownyMessaging.sendErrorMsg(sender, Translatable.of("msg_err_dont_belong_nation"));
                        return true;
                    }
                    break;

                default:
                    break;
            }

            Channel channel = chatPlayer.getChannel(ChannelTypes.valueOf(args[0].toUpperCase()));
            chatPlayer.setCurrentChannel(channel);

            String switchMessage = TownyChat.PLUGIN.getConfig().getString("lang.channel_switched");
            TextComponent switchMessageComponent = (TextComponent) MiniMessage.get().parse(switchMessage,
                    Template.of("channel", channel.getName()));

            sender.sendMessage("\n");
            TownyMessaging.sendMsg(sender, switchMessageComponent.content());
            sender.sendMessage("\n");

            return true;
        }

        String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_channel_not_found");
        TextComponent errorMessageComponent = (TextComponent) MiniMessage.get().parse(errorMessage,
                Template.of("channel", args[0]));

        TownyMessaging.sendErrorMsg(sender, errorMessageComponent.content());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String alias, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return null;
        }

        List<String> channelTypes = new ArrayList<String>();
        channelTypes.addAll(TownyChat.PLUGIN.getConfig().getConfigurationSection("channels").getKeys(false));

        return args.length == 1
                ? channelTypes.stream().filter(id -> id.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList())
                : channelTypes;
    }
}
