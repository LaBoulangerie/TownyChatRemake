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

## 🙏 Support

Please contact `PainOchoco#3750` on Discord if you have any issues or requests!
