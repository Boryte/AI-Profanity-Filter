package net.bytecore.aiprofanityfilter.managers;

import lombok.Getter;
import net.bytecore.aiprofanityfilter.Profanity;
import net.bytecore.aiprofanityfilter.commands.api.ProfanityEvent;
import net.bytecore.aiprofanityfilter.config.Config;
import net.bytecore.aiprofanityfilter.utils.ProfanityAIChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class ProfanityManager implements Listener {

    @Getter
    static ProfanityManager instance;
    private final Set<String> profaneWords;
    private final Set<UUID> exemptPlayers;
    private final File file;
    private final FileConfiguration config;

    public ProfanityManager (Profanity main) {
        instance = this;
        Bukkit.getPluginManager().registerEvents(this, main);
        this.profaneWords = new HashSet<>();
        this.exemptPlayers = new HashSet<>();
        this.file = new File(Profanity.getInstance().getDataFolder(), "profanity.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.config = YamlConfiguration.loadConfiguration(file);
        loadWords();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (Config.getBoolean(Config.CORE_DEBUG)) {
            Profanity.getInstance().getLogger().info("Chat event triggered: " + message);
        }

        if (Config.getBoolean(Config.CORE_PERM_EXEMPT) && player.hasPermission("profanity.exempt")) {
            Profanity.getInstance().getLogger().info("Player is exempt: " + player.getName());
            return;
        }

        if (ProfanityManager.getInstance().isExempt(player)) {
            Profanity.getInstance().getLogger().info("Player is exempt (via manager): " + player.getName());
            return;
        }

        if (ProfanityManager.getInstance().isProfane(message)) {
            Profanity.getInstance().getLogger().info("Message is profane: " + message);
            Bukkit.getScheduler().runTask(Profanity.getInstance(), () -> {
                new ProfanityEvent(player, message, false);
                event.setCancelled(true);
                if (Config.getBoolean(Config.CORE_DEBUG)) {
                    player.sendMessage(ChatColor.RED + "Your message was filtered by system for profanity: " + message);
                }
            });
            return;
        }

        ProfanityAIChecker.checkProfanityAsync(message).thenAccept(isProfane -> {
            if (isProfane) {
                Profanity.getInstance().getLogger().info("Message is profane (AI): " + message);
                Bukkit.getScheduler().runTask(Profanity.getInstance(), () -> {
                    new ProfanityEvent(player, message, true);
                    if (Config.getBoolean(Config.CORE_AUTO_ADD_PROFANITY)) {
                        ProfanityManager.getInstance().addWord(message);
                    }
                    event.setCancelled(true);
                    if (Config.getBoolean(Config.CORE_DEBUG)) {
                        player.sendMessage(ChatColor.DARK_RED + "Your message was filtered by AI for profanity: " + message);
                    }
                });
            }
        }).exceptionally(ex -> {
            Profanity.getInstance().getLogger().severe("Failed to check profanity: " + ex.getMessage());
            return null;
        });
    }


    public boolean addWord(String word) {
        boolean added = profaneWords.add(word.toLowerCase());
        if (added) {
            saveWords();
        }
        return added;
    }

    public boolean removeWord(String word) {
        boolean removed = profaneWords.remove(word.toLowerCase());
        if (removed) {
            saveWords();
        }
        return removed;
    }

    public boolean isProfane(String word) {
        return profaneWords.contains(word.toLowerCase());
    }

    public Set<String> getProfaneWords() {
        return new HashSet<>(profaneWords);
    }

    public void loadWords() {
        profaneWords.clear();
        profaneWords.addAll(config.getStringList("profanity"));
        for (String uuid : config.getStringList("exempt")) {
            exemptPlayers.add(UUID.fromString(uuid));
        }
    }

    public void saveWords() {
        config.set("profanity", new ArrayList<>(profaneWords));
        config.set("exempt", exemptPlayers.stream().map(UUID::toString).collect(Collectors.toList()));
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addExemptPlayer(Player player) {
        exemptPlayers.add(player.getUniqueId());
        saveWords();
    }

    public void removeExemptPlayer(Player player) {
        exemptPlayers.remove(player.getUniqueId());
        saveWords();
    }

    public boolean isExempt(Player player) {
        return exemptPlayers.contains(player.getUniqueId()) || player.hasPermission("profanity.exempt");
    }

}
