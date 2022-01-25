package net.laboulangerie.townychat.commands.towny;

import com.palmergames.bukkit.towny.TownyMessaging;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class ToggleTownChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias,
            @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            String errorMessage = TownyChat.PLUGIN.getConfig().getString("lang.sender_not_player");
            sender.sendMessage(MiniMessage.get().parse(errorMessage));
            return true;
        }

        ChatPlayerManager chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();

        Player player = (Player) sender;
        ChatPlayer chatPlayer = chatPlayerManager.getChatPlayer(player);
        String message = chatPlayer.toggleChannel(chatPlayer.getChannel("town"));
        TownyMessaging.sendMsg(player, message);
        return false;
    }

}