package net.laboulangerie.townychat.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import net.laboulangerie.townychat.TownyChat;

public class ComponentRenderer {

    private FileConfiguration config;

    public ComponentRenderer() {
        this.config = TownyChat.PLUGIN.getConfig();
    }

    public Component parse(Player player, String text) {

        String papiParsed = PlaceholderAPI.setPlaceholders(player, text);
        Component miniMessageParsed = MiniMessage.get().parse(papiParsed, parseTemplates(player));

        return miniMessageParsed;
    }

    public Component parse(Player player, String text, List<Template> additionnalTemplates) {
        List<Template> templates = parseTemplates(player);
        templates.addAll(additionnalTemplates);

        String papiParsed = PlaceholderAPI.setPlaceholders(player, text);
        Component miniMessageParsed = MiniMessage.get().parse(papiParsed, templates);

        return miniMessageParsed;
    }

    private List<Template> parseTemplates(Player player) {
        List<Template> templates = new ArrayList<Template>();
        ConfigurationSection templateSection = config.getConfigurationSection("templates");

        for (String key : templateSection.getKeys(false)) {
            templates.add(Template.of(key,
                    MiniMessage.get().parse(
                            PlaceholderAPI.setPlaceholders(player, templateSection.getString(key)))));
        }

        return templates;
    }
}
