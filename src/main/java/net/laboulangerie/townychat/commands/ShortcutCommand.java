package net.laboulangerie.townychat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.player.ChatPlayer;

public class ShortcutCommand implements CommandExecutor {
    private ChannelTypes channelType;

    public ShortcutCommand(ChannelTypes channelType) {
        this.channelType = channelType;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_sender_not_player");
            sender.sendMessage(MiniMessage.miniMessage().deserialize(errorMessage));
            return true;
        }

        String message = String.join(" ", args);

        Player player = (Player) sender;
        ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(player);

        if (chatPlayer.getChannels().keySet().contains(this.channelType)) {
            ChannelTypes previousChannelType = chatPlayer.getCurrentChannel().getType();
            chatPlayer.setCurrentChannel(this.channelType);
            player.chat(message);
            chatPlayer.setCurrentChannel(previousChannelType);
        }

        return true;
    }
}
