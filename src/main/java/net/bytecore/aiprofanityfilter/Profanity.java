package net.bytecore.aiprofanityfilter;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.bytecore.aiprofanityfilter.commands.ProfanityCommand;
import net.bytecore.aiprofanityfilter.commands.handler.CommandHandler;
import net.bytecore.aiprofanityfilter.managers.ConfigManager;
import net.bytecore.aiprofanityfilter.managers.ProfanityManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class Profanity extends JavaPlugin {

    @Getter
    static Profanity instance;
    private ProfanityManager profanityManager;
    private CommandHandler commandHandler;
    @Getter
    boolean placeholderApiEnabled;

    @Override
    public void onEnable() {
        instance = this;
        new ConfigManager();
        ConfigManager.getInstance().createConfig();
        profanityManager = new ProfanityManager(this);
        commandHandler = new CommandHandler();
        registerCommands();
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            placeholderApiEnabled = true;
            //new PAPIHook(this).register();
        }

    }

    @Override
    public void onDisable() {
        profanityManager.saveWords();
    }

    public void registerCommands() {
        try {
            commandHandler.registerCommand(commandHandler.getCommandMap(), "profanity", new ProfanityCommand());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    String getPlaceHolder(Player player, String string) {
        if(player == null) return "";

        return PlaceholderAPI.setPlaceholders(player, string);
    }

    public String replacePlaceholders(Player player, String text) {
        if(player == null || !isPlaceholderApiEnabled()) return text;

        String t2 = text;

        while(t2.indexOf("{") != -1 && t2.indexOf("}") != -1) {
            String placeholder = t2.substring(t2.indexOf("{") + 1, t2.indexOf("}"));

            t2 = t2.replace("{" + placeholder + "}", "");
            text = text.replace("{" + placeholder + "}", getPlaceHolder(player, "%" + placeholder + "%"));
        }

        return text;
    }

}
