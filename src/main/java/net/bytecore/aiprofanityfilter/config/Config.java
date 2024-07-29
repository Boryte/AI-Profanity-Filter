package net.bytecore.aiprofanityfilter.config;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.cryptomorin.xseries.XMaterial;
import net.bytecore.aiprofanityfilter.Profanity;
import net.bytecore.aiprofanityfilter.managers.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ImmutableMap;

import lombok.Getter;

@Getter
public enum Config {

    CORE_DO_NOT_TOUCH("core.do-not-touch", false),
    CORE_LRS("core.lrs-do-not-touch", true),
    CORE_DEBUG("core.debug", false),
    CORE_PERM_EXEMPT("core.perm-exempt", false),
    CORE_AUTO_ADD_PROFANITY("core.auto-add-profanity", true),
    CORE_AI_SENSITIVITY("core.ai-sensitivity", 0.82),

    DISCORD_WEBHOOKS_ENABLED("discord.webhooks-enabled", false),
    DISCORD_GAME_START_WEBHOOK("discord.webhook-game-start", "webhook here"),
    DISCORD_GAME_END_WEBHOOK("discord.webhook-game-end", "webhook here"),

    ;

    String entry;

    Object value;


    Config(String entry, Object value) {
        this.entry = entry;
        this.value = value;
    }

    public static String T_DNAME = "%target-displayname%";
    public static String T_NAME = "%target-name%";

    public static String P_DNAME = "%player-displayname%";
    public static String P_NAME = "%player-name%";


    public static void setData(Config config, Object data) {
        ConfigManager.getInstance().getConfigData().set(config.getEntry(), data);

        try {
            ConfigManager.getInstance().getConfigData().save(ConfigManager.getInstance().getConfig());
        } catch(Exception exception) {
        }
    }

    public static String getStringColored(Config config, ImmutableMap<String, Object> replace) {
        try {
            String dude = ConfigManager.getInstance().getConfigData().getString(config.getEntry());
            if(replace != null) {
                for(Entry<String, Object> ob: replace.entrySet()) {
                    dude = dude.replace(ob.getKey(), ob.getValue().toString());
                }
            }
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    String dude = data.getValue().toString();
                    if(replace != null) {
                        for(Entry<String, Object> ob: replace.entrySet()) {
                            dude = dude.replace(ob.getKey(), ob.getValue().toString());
                        }
                    }
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static String getStringColored(Config config, Player[] players, ImmutableMap<String, Object> replace) {
        try {
            String dude = ConfigManager.getInstance().getConfigData().getString(config.getEntry());
            if(players.length > 0) dude = Profanity.getInstance().replacePlaceholders(players[0], dude.replace(P_NAME, players[0] != null ? players[0].getName() : "?").replace(P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
            if(players.length > 1) dude = Profanity.getInstance().replacePlaceholders(players[1], dude.replace(T_NAME, players[1] != null ? players[1].getName() : "?").replace(T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
            if(replace != null) {
                for(Entry<String, Object> ob: replace.entrySet()) {
                    dude = dude.replace(ob.getKey(), ob.getValue().toString());
                }
            }
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    String dude = data.getValue().toString();
                    if(players.length > 0) dude = Profanity.getInstance().replacePlaceholders(players[0], dude.replace(P_NAME, players[0] != null ? players[0].getName() : "?").replace(P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                    if(players.length > 1) dude = Profanity.getInstance().replacePlaceholders(players[1], dude.replace(T_NAME, players[1] != null ? players[1].getName() : "?").replace(T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                    if(replace != null) {
                        for(Entry<String, Object> ob: replace.entrySet()) {
                            dude = dude.replace(ob.getKey(), ob.getValue().toString());
                        }
                    }
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static String getStringColored(Config config) {
        try {
            String dude = ConfigManager.getInstance().getConfigData().getString(config.getEntry());
            return ChatColor.translateAlternateColorCodes('&', dude);
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    String dude = data.getValue().toString();
                    return ChatColor.translateAlternateColorCodes('&', dude);
                }
            }
        }
        return null;
    }

    public static List<String> getStringListColored(Config config, ImmutableMap<String, Object> replace) {
        try {
            List<String> dude = ConfigManager.getInstance().getConfigData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for(String d: dude) {
                if(replace != null) {
                    for(Entry<String, Object> ob: replace.entrySet()) {
                        d = d.replace(ob.getKey(), ob.getValue().toString());
                    }
                }
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for(String d: dude) {
                        if(replace != null) {
                            for(Entry<String, Object> ob: replace.entrySet()) {
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

    public static List<String> getStringListColored(Config config, Player[] players, ImmutableMap<String, Object> replace) {
        try {
            List<String> dude = ConfigManager.getInstance().getConfigData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for(String d: dude) {
                if(players.length > 0) d = Profanity.getInstance().replacePlaceholders(players[0], d.replace(P_NAME, players[0] != null ? players[0].getName() : "?").replace(P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                if(players.length > 1) d = Profanity.getInstance().replacePlaceholders(players[1], d.replace(T_NAME, players[1] != null ? players[1].getName() : "?").replace(T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                if(replace != null) {
                    for(Entry<String, Object> ob: replace.entrySet()) {
                        d = d.replace(ob.getKey(), ob.getValue().toString());
                    }
                }
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for(String d: dude) {
                        if(players.length > 0) d = Profanity.getInstance().replacePlaceholders(players[0], d.replace(P_NAME, players[0] != null ? players[0].getName() : "?").replace(P_DNAME, players[0] != null ? players[0].getDisplayName() : "&c?"));
                        if(players.length > 1) d = Profanity.getInstance().replacePlaceholders(players[1], d.replace(T_NAME, players[1] != null ? players[1].getName() : "?").replace(T_DNAME, players[1] != null ? players[1].getDisplayName() : "&c?"));
                        if(replace != null) {
                            for(Entry<String, Object> ob: replace.entrySet()) {
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

    public static List<String> getStringListColored(Config config) {
        try {
            List<String> dude = ConfigManager.getInstance().getConfigData().getStringList(config.getEntry());
            List<String> damn = new ArrayList<>();
            for(String d: dude) {
                damn.add(ChatColor.translateAlternateColorCodes('&', d));
            }
            return damn;
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    List<String> dude = (List<String>) data.getValue();
                    List<String> damn = new ArrayList<>();
                    for(String d: dude) {
                        damn.add(ChatColor.translateAlternateColorCodes('&', d));
                    }
                    return damn;
                }
            }
        }
        return null;
    }

    public static String getString(Config config) {
        try {
            return ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getConfigData().getString(config.getEntry()));
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return ChatColor.translateAlternateColorCodes('&', data.getValue().toString());
                }
            }
        }
        return null;
    }


    public static Integer getInteger(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getInt(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (int) data.getValue();
                }
            }
        }
        return null;
    }

    public static Double getDouble(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getDouble(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (Double) data.getValue();
                }
            }
        }
        return null;
    }

	/*public static Float getFloat(Config config) {
		try {
			return ConfigManager.getInstance().getConfigData().getFloat(config.getEntry());
		} catch(Exception exception) {
			for(Config data: values()) {
				if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
					return (Float) data.getValue();
				}
			}
		}
		return null;
	}*/

    public static Boolean getBoolean(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getBoolean(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (boolean) data.getValue();
                }
            }
        }
        return null;
    }

    public static List<?> getList(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getList(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (List<?>) data.getValue();
                }
            }
        }
        return null;
    }

    public static List<String> getStringList(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getStringList(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (List<String>) data.getValue();
                }
            }
        }
        return null;
    }

    public static List<Integer> getIntegerList(Config config) {
        try {
            return ConfigManager.getInstance().getConfigData().getIntegerList(config.getEntry());
        } catch(Exception exception) {
            for(Config data: values()) {
                if(data.getEntry().equalsIgnoreCase(config.getEntry())) {
                    return (List<Integer>) data.getValue();
                }
            }
        }
        return null;
    }

}
