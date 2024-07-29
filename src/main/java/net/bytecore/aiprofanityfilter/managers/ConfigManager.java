package net.bytecore.aiprofanityfilter.managers;

import lombok.Getter;
import net.bytecore.aiprofanityfilter.Profanity;
import net.bytecore.aiprofanityfilter.config.Config;
import net.bytecore.aiprofanityfilter.config.Messages;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;

public class ConfigManager {

    @Getter
    static ConfigManager instance;

    public ConfigManager() { instance = this; }

    @Getter
    File config, messages;

    @Getter
    FileConfiguration configData, messagesData;

    public void createConfig() {
        config = new File(Profanity.getInstance().getDataFolder(), "config.yml");
        messages = new File(Profanity.getInstance().getDataFolder(), "messages.yml");

        boolean ex = config.exists();
        if(!ex) {
            config.getParentFile().mkdirs();
            try {
                config.createNewFile();
                configData = YamlConfiguration.loadConfiguration(config);

                for(Config data: Config.values()) {
                    configData.set(data.getEntry(), data.getValue());
                }

                try {
                    configData.save(config);
                } catch(Exception exception) {
                }
            } catch(Exception exception) {
            }
        } else {
            configData = YamlConfiguration.loadConfiguration(config);
        }

        if(ex) {
            for(File file: Arrays.asList(messages)) {
                if(!file.exists()) {
                    if(configData.getConfigurationSection(file.getName().replace(".yml", "")) == null) continue;

                    file.getParentFile().mkdirs();
                    try {
                        file.createNewFile();
                    } catch(Exception exception) {
                    }

                    FileConfiguration n = YamlConfiguration.loadConfiguration(file);

                    for(Map.Entry<String, Object> data: configData.getConfigurationSection(file.getName().replace(".yml", "")).getValues(true).entrySet()) {
                        n.set(file.getName().replace(".yml", "") + "." + data.getKey(), data.getValue());
                    }

                    try {
                        n.save(file);
                        configData.set(file.getName().replace(".yml", ""), null);

                        try {
                            configData.save(config);
                        } catch(Exception exception) {
                        }
                    } catch(Exception exception) {
                    }
                }
            }
        }


        configData = YamlConfiguration.loadConfiguration(config);


        createMessages();
    }

    public void reloadConfig() {
        // Reload config.yml
        configData = YamlConfiguration.loadConfiguration(config);

        // Reload other files
        messagesData = YamlConfiguration.loadConfiguration(messages);
    }

    void createMessages() {
        if(!messages.exists()) {
            messages.getParentFile().mkdirs();
            try {
                messages.createNewFile();
                messagesData = YamlConfiguration.loadConfiguration(messages);

                for(Messages data: Messages.values()) {
                    messagesData.set(data.getEntry(), data.getValue());
                }

                try {
                    messagesData.save(messages);
                } catch(Exception exception) {
                }
            } catch(Exception exception) {
            }
        } else {
            messagesData = YamlConfiguration.loadConfiguration(messages);
        }

        int cunt = 0;
        for(Messages data: Messages.values()) {
            cunt++;
            if(messagesData.getString(data.getEntry()) == null) {
                messagesData.set(data.getEntry(), data.getValue());
            }

            if(cunt >= Messages.values().length) {
                try {
                    messagesData.save(messages);
                } catch(Exception exception) {
                }
            }
        }
    }

}
