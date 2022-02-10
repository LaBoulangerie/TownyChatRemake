package net.laboulangerie.townychat.commands.towny;

import com.palmergames.bukkit.towny.TownyMessaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class ToggleTownChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        FileConfiguration config = TownyChat.PLUGIN.getConfig();

        if (!(sender instanceof Player)) {
            String errorMessage = config.getString("lang.sender_not_player");
            sender.sendMessage(MiniMessage.get().parse(errorMessage));
            return true;
        }

        ChatPlayerManager chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();

        Player player = (Player) sender;
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);

        boolean isEnabled = chatPlayer.toggleChannel(ChannelTypes.TOWN);
        Channel townChannel = chatPlayer.getChannel(ChannelTypes.TOWN);

        String enabled = config.getString("lang.enabled");
        String disabled = config.getString("lang.disabled");
        String toggledMessage = config.getString("lang.channel_toggled");

        TextComponent messageComponent = (TextComponent) MiniMessage.get().parse(toggledMessage,
                Template.of("channel", townChannel.getName()),
                Template.of("status", isEnabled ? enabled : disabled));

        TownyMessaging.sendMsg(player, messageComponent.content());
        return false;
    }
}