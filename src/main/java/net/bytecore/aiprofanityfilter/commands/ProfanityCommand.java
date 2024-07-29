package net.bytecore.aiprofanityfilter.commands;

import net.bytecore.aiprofanityfilter.config.Messages;
import net.bytecore.aiprofanityfilter.guis.ProfanityGUI;
import net.bytecore.aiprofanityfilter.managers.ProfanityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.UUID;

public class ProfanityCommand extends Command {

    public ProfanityCommand() {
        super("profanity");
        setUsage("§cUsage: /profanity <add,remove,list,gui, exempt, unexempt, exemptlist> [optional]");
        setAliases(Arrays.asList("prof"));
        setPermission("profanity.command");
    }

    @Override
    public boolean execute(CommandSender sender, String arg, String[] args) {
        if(sender instanceof Player && !sender.hasPermission(getPermission())) {
            sender.sendMessage(Messages.getMessage(Messages.MESSAGES_NO_PERMISSION));
            return false;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Usage: /profanity <add|remove|list|gui> [word]");
            return false;
        }

        String action = args[0].toLowerCase();

        switch (action) {
            case "add":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /profanity add <word>");
                    return false;
                }
                String wordToAdd = args[1];
                if (ProfanityManager.getInstance().addWord(wordToAdd)) {
                    sender.sendMessage(ChatColor.GREEN + "Word added to the profanity list.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Word is already in the profanity list.");
                }
                break;

            case "remove":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /profanity remove <word>");
                    return false;
                }
                String wordToRemove = args[1];
                if (ProfanityManager.getInstance().removeWord(wordToRemove)) {
                    sender.sendMessage(ChatColor.GREEN + "Word removed from the profanity list.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Word not found in the profanity list.");
                }
                break;

            case "list":
                sender.sendMessage(ChatColor.YELLOW + "Profane words:");
                for (String word : ProfanityManager.getInstance().getProfaneWords()) {
                    sender.sendMessage(ChatColor.RED + "- " + word);
                }
                break;

            case "gui":
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    ProfanityGUI.openGUI(player, 0);
                } else {
                    sender.sendMessage(ChatColor.RED + "Only players can use this command.");
                }
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Usage: /profanity <add|remove|list|gui> [word]");
                return false;

            case "exempt":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /profanity exempt <player>");
                    return false;
                }
                Player playerToExempt = sender.getServer().getPlayer(args[1]);
                if (playerToExempt != null) {
                    ProfanityManager.getInstance().addExemptPlayer(playerToExempt);
                    sender.sendMessage(ChatColor.GREEN + "Player exempted from profanity checks.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                }
                break;

            case "unexempt":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /profanity unexempt <player>");
                    return false;
                }
                Player playerToUnexempt = sender.getServer().getPlayer(args[1]);
                if (playerToUnexempt != null) {
                    ProfanityManager.getInstance().removeExemptPlayer(playerToUnexempt);
                    sender.sendMessage(ChatColor.GREEN + "Player removed from profanity exempt list.");
                } else {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                }
                break;

            case "exemptlist":
                sender.sendMessage(ChatColor.YELLOW + "Exempted players:");
                for (UUID uuid : ProfanityManager.getInstance().getExemptPlayers()) {
                    Player player = Bukkit.getPlayer(uuid);
                    assert player != null;
                    sender.sendMessage(ChatColor.RED + "- " + player.getName());
                }
                break;
        }

        return true;
    }
}
