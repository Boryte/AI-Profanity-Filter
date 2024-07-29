package net.bytecore.aiprofanityfilter.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;

import me.clip.placeholderapi.PlaceholderAPI;
import net.bytecore.aiprofanityfilter.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public enum Messages {

    // MESSAGES
    MESSAGES_SERVER_PREFIX("messages.prefix", "&8[&6Profanity&8]"),
    MESSAGES_ONLY_FOR_PLAYERS("messages.only-for-players", "%prefix% &4Only players can perform this command&8."),
    MESSAGES_CANNOT_PERFORM_COMMAND("messages.cannot-perform-command", "%prefix% &4You can't perform this command&8!"),
    MESSAGES_PLAYER_NOT_FOUND("messages.player-not-found", "%prefix% &cThat player is not found&8."),
    MESSAGES_PLAYER_NOT_ALIVE("messages.player-not-alive", "%prefix% &cThat player is not alive&8!"),
    MESSAGES_NO_PERMISSION("messages.no-permission", "&cNo permission."),

    ;

    String entry;

    Object value;

    Messages(String entry, Object value) {
        this.entry = entry;
        this.value = value;
    }

    private static String applyPlaceholders(Player player, String message) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return PlaceholderAPI.setPlaceholders(player, message);
        }
        return message;
    }

    public static String getMessage(Messages messages, Player[] players, ImmutableMap<String, Object> replace) {
        try {
            String dude = ConfigManager.getInstance().getMessagesData().getString(messages.getEntry());
            dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
            if (players.length > 0) {
                dude = applyPlaceholders(players[0], dude.replace(Config.P_NAME, players[0] != null ? players[0].getName() : "?").replace(Config.P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
            }
            if (players.length > 1) {
                dude = applyPlaceholders(players[1], dude.replace(Config.T_NAME, players[1] != null ? players[1].getName() : "?").replace(Config.T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
            }
            if (replace != null) {
                for (Entry<String, Object> ob : replace.entrySet()) {
                    dude = dude.replace(ob.getKey(), ob.getValue().toString());
                }
            }
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(messages.getEntry())) {
                    String dude = data.getValue().toString();
                    dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                    if (players.length > 0) {
                        dude = applyPlaceholders(players[0], dude.replace(Config.P_NAME, players[0] != null ? players[0].getName() : "?").replace(Config.P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                    }
                    if (players.length > 1) {
                        dude = applyPlaceholders(players[1], dude.replace(Config.T_NAME, players[1] != null ? players[1].getName() : "?").replace(Config.T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                    }
                    if (replace != null) {
                        for (Entry<String, Object> ob : replace.entrySet()) {
                            dude = dude.replace(ob.getKey(), ob.getValue().toString());
                        }
                    }
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static String getMessage(Messages messages, ImmutableMap<String, Object> replace) {
        try {
            String dude = ConfigManager.getInstance().getMessagesData().getString(messages.getEntry());
            dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
            if (replace != null) {
                for (Entry<String, Object> ob : replace.entrySet()) {
                    dude = dude.replace(ob.getKey(), ob.getValue().toString());
                }
            }
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(messages.getEntry())) {
                    String dude = data.getValue().toString();
                    dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                    if (replace != null) {
                        for (Entry<String, Object> ob : replace.entrySet()) {
                            dude = dude.replace(ob.getKey(), ob.getValue().toString());
                        }
                    }
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static String getMessage(Messages messages) {
        try {
            String dude = ConfigManager.getInstance().getMessagesData().getString(messages.getEntry());
            dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(messages.getEntry())) {
                    String dude = data.getValue().toString();
                    dude = dude.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static List<String> getMessageList(Messages config, Player[] players, ImmutableMap<String, Object> replace) {
        try {
            List<String> dude = ConfigManager.getInstance().getMessagesData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for (String d : dude) {
                d = d.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                if (players.length > 0) {
                    d = applyPlaceholders(players[0], d.replace(Config.P_NAME, players[0] != null ? players[0].getName() : "?").replace(Config.P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                }
                if (players.length > 1) {
                    d = applyPlaceholders(players[1], d.replace(Config.T_NAME, players[1] != null ? players[1].getName() : "?").replace(Config.T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                }
                if (replace != null) {
                    for (Entry<String, Object> ob : replace.entrySet()) {
                        d = d.replace(ob.getKey(), ob.getValue().toString());
                    }
                }
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for (String d : dude) {
                        d = d.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                        if (players.length > 0) {
                            d = applyPlaceholders(players[0], d.replace(Config.P_NAME, players[0] != null ? players[0].getName() : "?").replace(Config.P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                        }
                        if (players.length > 1) {
                            d = applyPlaceholders(players[1], d.replace(Config.T_NAME, players[1] != null ? players[1].getName() : "?").replace(Config.T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                        }
                        if (replace != null) {
                            for (Entry<String, Object> ob : replace.entrySet()) {
                                d = d.replace(ob.getKey(), ob.getValue().toString());
                            }
                        }
                        damn.add(ChatColor.translateAlternateColorCodes('&', d));
                    }
                    return damn;
                }
            }
        }
        return null;
    }

    public static List<String> getMessageList(Messages config, ImmutableMap<String, Object> replace) {
        try {
            List<String> dude = ConfigManager.getInstance().getMessagesData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for (String d : dude) {
                d = d.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                if (replace != null) {
                    for (Entry<String, Object> ob : replace.entrySet()) {
                        d = d.replace(ob.getKey(), ob.getValue().toString());
                    }
                }
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for (String d : dude) {
                        d = d.replace("%prefix%", Messages.getString(Messages.MESSAGES_SERVER_PREFIX));
                        if (replace != null) {
                            for (Entry<String, Object> ob : replace.entrySet()) {
                                d = d.replace(ob.getKey(), ob.getValue().toString());
                            }
                        }
                        damn.add(ChatColor.translateAlternateColorCodes('&', d));
                    }
                    return damn;
                }
            }
        }
        return null;
    }

    public static List<String> getMessageList(Messages config) {
        try {
            List<String> dude = ConfigManager.getInstance().getMessagesData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for (String d : dude) {
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for (String d : dude) {
                        damn.add(ChatColor.translateAlternateColorCodes('&', d));
                    }
                    return damn;
                }
            }
        }
        return null;
    }

    public static String getString(Messages messages) {
        try {
            String message = ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessagesData().getString(messages.getEntry()));
            message = applyPlaceholders(null, message);
            return message;
        } catch (Exception exception) {
            for (Messages data : Messages.values()) {
                if (data.getEntry().equalsIgnoreCase(messages.getEntry())) {
                    String message = ChatColor.translateAlternateColorCodes('&', data.getValue().toString());
                    message = applyPlaceholders(null, message);
                    return message;
                }
            }
        }
        return null;
    }

}
