package net.laboulangerie.townychat;

import java.lang.reflect.Constructor;
import java.util.Arrays;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI.CommandType;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import net.laboulangerie.townychat.commands.ChatCommands;
import net.laboulangerie.townychat.commands.towny.ReloadTownyChatCommand;
import net.laboulangerie.townychat.commands.towny.ToggleNationChatCommand;
import net.laboulangerie.townychat.commands.towny.ToggleTownChatCommand;
import net.laboulangerie.townychat.core.TownyChatRenderer;
import net.laboulangerie.townychat.listeners.TownyChatListener;
import net.laboulangerie.townychat.player.ChatPlayerManager;

public class TownyChat extends JavaPlugin {
    public static TownyChat PLUGIN;

    private ChatPlayerManager chatPlayerManager;
    private TownyChatRenderer townyChatRenderer;
    private TownyAPI townyAPI;

    @Override
    public void onEnable() {
        TownyChat.PLUGIN = this;

        this.chatPlayerManager = new ChatPlayerManager();
        this.townyChatRenderer = new TownyChatRenderer();
        this.townyAPI = TownyAPI.getInstance();

        this.saveDefaultConfig();
        this.registerListeners();
        this.getCommand("chat").setExecutor(new ChatCommands());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWN_TOGGLE, "chat", new ToggleTownChatCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.NATION_TOGGLE, "chat", new ToggleNationChatCommand());
        TownyCommandAddonAPI.addSubCommand(CommandType.TOWNYADMIN, "reloadchat", new ReloadTownyChatCommand());

        getLogger().info("Plugin started");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin Disabled");
    }

    public ChatPlayerManager getChatPlayerManager() {
        return this.chatPlayerManager;
    }

    public TownyChatRenderer getTownyChatRenderer() {
        return this.townyChatRenderer;
    }

    public TownyAPI getTownyAPI() {
        return this.townyAPI;
    }

    private void registerListeners() {
        Arrays.asList(new TownyChatListener())
                .forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));
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