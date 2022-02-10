package net.laboulangerie.townychat;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.laboulangerie.townychat.channels.Channel;
import net.laboulangerie.townychat.channels.ChannelManager;
import net.laboulangerie.townychat.channels.ChannelTypes;
import net.laboulangerie.townychat.commands.ChatCommands;
import net.laboulangerie.townychat.commands.ShortcutCommand;
import net.laboulangerie.townychat.commands.SpyCommand;
import net.laboulangerie.townychat.commands.towny.ReloadTownyChatCommand;
import net.laboulangerie.townychat.commands.towny.ToggleNationChatCommand;
import net.laboulangerie.townychat.commands.towny.ToggleTownChatCommand;
import net.laboulangerie.townychat.core.TownyChatRenderer;
import net.laboulangerie.townychat.listeners.TownyChatListener;
import net.laboulangerie.townychat.listeners.TownyListener;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChat extends JavaPlugin {
    public static TownyChat PLUGIN;

    private ChatPlayerManager chatPlayerManager;
    private ChannelManager channelManager;
    private TownyChatRenderer townyChatRenderer;
    private TownyAPI townyAPI;
    private TownyUniverse townyUniverse;
    private Channel globalChannel;

    @Override
    public void onEnable() {
        TownyChat.PLUGIN = this;

        this.globalChannel = new Channel(ChannelTypes.GLOBAL);

        this.townyAPI = TownyAPI.getInstance();
        this.townyUniverse = TownyUniverse.getInstance();

        this.channelManager = new ChannelManager();
        this.chatPlayerManager = new ChatPlayerManager();
        this.townyChatRenderer = new TownyChatRenderer();

        this.saveDefaultConfig();
        this.registerListeners();

        this.getCommand("chat").setExecutor(new ChatCommands());
        this.getCommand("spy").setExecutor(new SpyCommand());

        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN_TOGGLE, "chat", new ToggleTownChatCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.NATION_TOGGLE, "chat", new ToggleNationChatCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWNYADMIN, "reloadchat", new ReloadTownyChatCommand());

        this.registerShortcutCommands();

        getLogger().info("Plugin started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }

    public ChatPlayerManager getChatPlayerManager() {
        return this.chatPlayerManager;
    }

    public ChannelManager getChannelManager() {
        return this.channelManager;
    }

    public TownyChatRenderer getTownyChatRenderer() {
        return this.townyChatRenderer;
    }

    public TownyAPI getTownyAPI() {
        return this.townyAPI;
    }

    public TownyUniverse getTownyUniverse() {
        return this.townyUniverse;
    }

    public Channel getGlobalChannel() {
        return this.globalChannel;
    }

    private void registerListeners() {
        Arrays.asList(
                new TownyChatListener(),
                new TownyListener()).forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));
    }

    private void registerShortcutCommands() {
        ConfigurationSection channelsSection = this.getConfig().getConfigurationSection("channels");

        for (ChannelTypes channelType : ChannelTypes.values()) {
            String shortcutCommandName = channelsSection.getString(channelType.name().toLowerCase() + ".command");

            if (shortcutCommandName != null) {
                registerCommand(shortcutCommandName, new ShortcutCommand(channelType));
            }
        }
    }

    public void registerCommand(String name, CommandExecutor executor) {
        try {
            final Constructor<PluginCommand> c;
            c = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            c.setAccessible(true);

            final PluginCommand command = c.newInstance(name, this);
            command.setExecutor(executor);

            Bukkit.getCommandMap().register(this.getName(), command);
        } catch (Exception e) {
            getLogger().warning("Couldn't register command '" + name + "' cause: " + e.getMessage());
        }
    }
}