package net.laboulangerie.townychat;

import java.util.Arrays;

import org.bukkit.plugin.java.JavaPlugin;

import net.laboulangerie.townychat.listeners.ChatListener;

public class TownyChat extends JavaPlugin {
    public static TownyChat PLUGIN;

    @Override
    public void onEnable() {
        TownyChat.PLUGIN = this;
        this.saveDefaultConfig();
        this.registerListeners();

        getLogger().info("Plugin started");
    }

    private void registerListeners() {
        Arrays.asList(new ChatListener()).forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));
    }
}