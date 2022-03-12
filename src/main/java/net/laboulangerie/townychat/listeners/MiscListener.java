package net.laboulangerie.townychat.listeners;

import org.bukkit.advancement.Advancement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import io.papermc.paper.advancement.AdvancementDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.laboulangerie.townychat.TownyChat;
import net.laboulangerie.townychat.core.ComponentRenderer;

public class MiscListener implements Listener {

    private ConfigurationSection miscSection;
    private ComponentRenderer componentRenderer;

    public MiscListener() {
        this.miscSection = TownyChat.PLUGIN.getConfig().getConfigurationSection("misc");
        this.componentRenderer = TownyChat.PLUGIN.getComponentRenderer();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String joinString = miscSection.getString("join_message");
        Component joinComponent = componentRenderer.parse(event.getPlayer(), joinString);
        event.joinMessage(joinComponent);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        String joinString = miscSection.getString("quit_message");
        Component joinComponent = componentRenderer.parse(event.getPlayer(), joinString);
        event.quitMessage(joinComponent);
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {
        Advancement advancement = event.getAdvancement();
        AdvancementDisplay advancementDisplay = advancement.getDisplay();

        if (!advancementDisplay.doesAnnounceToChat() || advancementDisplay == null)
            return;

        AdvancementDisplay.Frame frame = advancementDisplay.frame();

        String advancementString = miscSection.getString("advancement." + frame.name().toLowerCase());
        Component advancementComponent = componentRenderer.parse(event.getPlayer(), advancementString,
                TagResolver.resolver(
                        Placeholder.component("advancement_title", advancementDisplay.title()),
                        Placeholder.component("advancement_description", advancementDisplay.description())));

        event.getPlayer().sendMessage(advancementDisplay.description());

        event.message(advancementComponent);
    }

}
