package net.laboulangerie.townychat.listeners;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.kyori.adventure.text.Component;
import github.scarsz.discordsrv.hooks.chat.ChatHook;
import github.scarsz.discordsrv.util.LangUtil;
import github.scarsz.discordsrv.util.MessageUtil;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelManager;
import net.laboulangerie.townychat.events.AsyncChatHookEvent;
import net.laboulangerie.townychat.player.ChatPlayer;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class DiscordHook implements ChatHook {

    private ChannelManager channelManager;
    private ChatPlayerManager chatPlayerManager;

    public DiscordHook() {
        this.channelManager = TownyChat.PLUGIN.getChannelManager();
        this.chatPlayerManager = TownyChat.PLUGIN.getChatPlayerManager();
    }

    // From Minecraft to Discord
    @EventHandler(priority = EventPriority.MONITOR)
    public void onMessage(AsyncChatHookEvent event) {
        String channelId = event.getChannel().getId();

        // DiscordSRV handles it automatically
        if (channelId == "global")
            return;

        // make sure chat channel is registered with a destination
        if (DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName(channelId) == null) {
            DiscordSRV.debug("Tried looking up destination Discord channel for Towny channel "
                    + event.getChannel().getName() + " but none found");
            return;
        }

        String messageString = MessageUtil
                .stripMiniTokens(PlainTextComponentSerializer.plainText().serialize(event.getMessage()));

        // make sure message isn't blank
        if (StringUtils.isBlank(messageString)) {
            DiscordSRV.debug("Received blank TownyChatRemake message, not processing");
            return;
        }

        DiscordSRV.getPlugin().processChatMessage(event.getPlayer(), messageString, channelId, event.isCancelled(),
                event);
    }

    // From Discord to Minecraft
    @Override
    public void broadcastMessageToChannel(String channelId, Component message) {

        // default to global channel
        Channel destinationChannel = TownyChat.PLUGIN.getGlobalChannel();

        // get the destination channel
        for (Channel channel : channelManager.getChannels().values()) {
            if (channel.getId().equals(channelId)) {
                destinationChannel = channel;
                break;
            }
        }

        String messageString = MessageUtil.toLegacy(message);

        String plainMessage = LangUtil.Message.CHAT_CHANNEL_MESSAGE.toString()
                .replace("%channelcolor%", "")
                .replace("%channelname%", destinationChannel.getName())
                .replace("%channelnickname%", destinationChannel.getName())
                .replace("%message%", messageString);

        for (ChatPlayer chatPlayer : chatPlayerManager.getChatPlayers().values()) {

            if (chatPlayer.getActiveChannels().contains(destinationChannel)) {
                Player player = Bukkit.getPlayer(chatPlayer.getUniqueId());
                player.sendMessage(plainMessage);
            }
        }
    }

    @Override
    public Plugin getPlugin() {
        return TownyChat.PLUGIN;
    }

}
