# 💬 TownyChatRemake ✨

**TownyChatRemake** is a simple yet fully customizable chat plugin that syncs with [Towny](https://github.com/TownyAdvanced/Towny).

## 👀 Preview

![image](https://user-images.githubusercontent.com/47084457/150990835-58cbb247-5d27-4409-8b81-fae8e9ebfed7.png "TownyChatRemake Preview")

## ✨ Features

-   Towns and nations specific channels
-   Fully customizable and easy-to-use configuration for server owners
-   User-friendly shortcut commands
-   Spy mode
-   [DiscordSRV](https://discordsrv.com/) support

## 🛠 Usage

⚠️ You must use [Paper 1.18.2](https://papermc.io/downloads) to run this plugin.

This is because it uses the [Adventure library](https://github.com/KyoriPowered/adventure) to handle chat formatting.

### Commands

-   `/chat <channel>` - Switch between channels, e.g. `/chat town`
-   `/town toggle chat` - Toggle town chat
-   `/nation toggle chat` - Toggle nation chat
-   `/gc` `/tc` `/nc` - Channel shortcut commands, e.g. `/tc hi town!`, can be modified in the config.
-   `/ta reload chat` - Reload config

### Permissions

-   `townychat.chat` - Default permission to switch channels
-   `townychat.spy` - Toggle spy mode, it allows you to receive messages from all the channels
-   `townychat.format` - Chat formatting with the [MiniMessage Format](https://docs.adventure.kyori.net/minimessage/format.html)

### Config

Everything should be explained in the config.yml file, but here are some reminders:

-   The `tags`' config section is made so that you can add components easily in other config strings.
-   You can use PAPI placeholders in every config strings except in the `lang` section like so: `<papi:YOUR_PLACEHOLDER_NAME>` (e.g `<papi:townyadvanced_town_tag>`)
-   Useful links:
    -   [MiniMessages format](https://docs.adventure.kyori.net/minimessage#format)
    -   [MiniMessages viewer](https://webui.adventure.kyori.net/)
    -   [Every PAPI placeholders](https://github.com/PlaceholderAPI/PlaceholderAPI/wiki/Placeholders)
    -   [Towny placeholders](https://github.com/TownyAdvanced/Towny/wiki/Placeholders)

### ⚙️ Recommended Towny Settings

```yml
town.set_tag_automatically = true
nation.set_tag_automatically = true
```

### DiscordSRV Channels

While this plugin supports DiscordSRV on the global channel,
You can also link Discord channels to towns and nations specific in-game channels.

Go to DiscordSRV's `config.yml` and to the `Channels` setting.
You can add channels using this format: `"government-name"` (e.g. `"town-mytown"`), all lowercase.

## 📋 To do list

By priority:

-   [ ] Better permissions

    -   [ ] `townychat.hear.<channel>` - Recieve message from that channel
    -   [ ] `townychat.write.<channel>` - Write in that channel
    -   [ ] `townychat.format.`
        -   [ ] `colors` - Use the colors in message (e.g aqua or #00FF00)
        -   [ ] `decorations` - Use decoration in message (italic, bold, underline, strikethrough, obfuscated)
        -   [ ] `misc` - Use every other MiniMessage features in message (click, hover, keybind, translatable, insertion, rainbow, gradient, transition, font, newline)

-   [ ] Switch channels quickly with /tc /nc /gc...
-   [ ] Cooldown
-   [ ] Support for legacy colors formatting (e.g. &e for yellow text)
-   [ ] (?) Custom channels
-   [ ] (?) Muteable parameter for channels

## 🙏 Support

Please contact `PainOchoco#3570` on Discord if you have any issues or requests!
