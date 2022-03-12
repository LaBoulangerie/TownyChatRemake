package net.laboulangerie.townychat.core;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.laboulangerie.townychat.TownyChat;

public class ComponentRenderer {

    private FileConfiguration config;

    public ComponentRenderer() {
        this.config = TownyChat.PLUGIN.getConfig();
    }

    public Component parse(Player player, String text) {

        Component miniMessageParsed = getPapiMiniMessage(player).deserialize(text, parseTags(player));

        return miniMessageParsed;
    }

    public Component parse(Player player, String text, TagResolver additionnalResolver) {
        TagResolver tagResolver = TagResolver.resolver(parseTags(player), additionnalResolver);

        Component miniMessageParsed = getPapiMiniMessage(player).deserialize(text, tagResolver);

        return miniMessageParsed;
    }

    private TagResolver parseTags(Player player) {
        List<TagResolver.Single> resolvers = new ArrayList<>();
        ConfigurationSection tagSection = config.getConfigurationSection("tags");

        for (String key : tagSection.getKeys(false)) {
            String tag = tagSection.getString(key);

            resolvers.add(
                    Placeholder.component(key,
                            getPapiMiniMessage(player).deserialize(tag)));
        }

        return TagResolver.resolver(resolvers);
    }

    public MiniMessage getPapiMiniMessage(Player player) {

        return MiniMessage.builder().tags(
                TagResolver.builder()
                        .resolver(StandardTags.defaults())
                        .resolver(papiTagResolver(player))
                        .build())
                .build();
    }

    private TagResolver papiTagResolver(Player player) {

        return TagResolver.resolver("papi", (argumentQueue, context) -> {
            String placeholder = argumentQueue
                    .popOr("The <papi> tag requires exactly one argument, the PAPI placeholder").value();

            return Tag.selfClosingInserting(
                    LegacyComponentSerializer.legacySection()
                            .deserialize(PlaceholderAPI.setPlaceholders(player,
                                    '%' + placeholder + '%')));

        });
    }
}
