package net.laboulangerie.townychat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.player.ChatPlayer;

public class ShortcutCommand implements CommandExecutor {
    private Channel channel;

    public ShortcutCommand(Channel channel) {
        this.channel = channel;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.err_sender_not_player");
            sender.sendMessage(MiniMessage.get().parse(errorMessage));
            return true;
        }

        String message = String.join(" ", args);

        Player player = (Player) sender;
        ChatPlayer chatPlayer = TownyChat.PLUGIN.getChatPlayerManager().getChatPlayer(player);

        Channel previousChannel = chatPlayer.getCurrentChannel();
        chatPlayer.setCurrentChannel(this.channel);
        chatPlayer.sendMessage(Component.text(message));
        chatPlayer.setCurrentChannel(previousChannel);

        return true;
    }
}