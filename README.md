# 💬 TownyChatRemake ✨

TownyChatRemake is a simple yet fully customizable chat plugin that syncs with [Towny](https://github.com/TownyAdvanced/Towny).

## 👀 Preview

![image](https://user-images.githubusercontent.com/47084457/150990835-58cbb247-5d27-4409-8b81-fae8e9ebfed7.png "TownyChatRemake Preview")

## 🛠 Usage

### Commands

-   `/chat <channel>` - Switch between channels, e.g. `/chat town`
-   `/town toggle chat` - Toggle town chat
-   `/nation toggle chat` - Toggle nation chat
-   `/gc` `/tc` `/nc` - Channel shortcut commands, e.g. `/tc hi town!`, can be modified in the config.
-   `/ta reloadchat` - Reload config, will change to `/ta reload chat` on the next Towny release.

### Permissions

-   `townychat.chat` - Default permission to switch channel

### ⚙️ Recommended Towny Settings

```yml
town.set_tag_automatically = true
nation.set_tag_automatically = true
```

## 🙏 Support

Please contact `PainOchoco#3750` on Discord if you have any issues or requests!

## 📋 To do list

I'm open for suggestions, here is my current to do list:

-   Check for town/nation leave and set ChatPlayer#currentChannel to global channel
-   Same for town/nation deletion
-   Add permission nodes for channel's shortcut commands
-   Moderation commands :
    -   /mute
    -   /spy
