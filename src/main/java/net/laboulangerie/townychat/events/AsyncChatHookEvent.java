package net.laboulangerie.townychat.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.laboulangerie.townychat.channels.Channel;

public class AsyncChatHookEvent extends Event {

    private static HandlerList HANDLERS = new HandlerList();

    private AsyncChatEvent event;
    private Channel channel;

    public AsyncChatHookEvent(AsyncChatEvent event, Channel channel, boolean async) {
        super(async);
        this.event = event;
        this.channel = channel;
    }

    public AsyncChatEvent getEvent() {
        return event;
    }

    public Channel getChannel() {
        return channel;
    }

    public Component getMessage() {
        return event.message();
    }

    public Player getPlayer() {
        return event.getPlayer();
    }

    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
