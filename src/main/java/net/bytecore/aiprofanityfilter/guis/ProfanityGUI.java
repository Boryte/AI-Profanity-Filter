package net.bytecore.aiprofanityfilter.guis;

import net.bytecore.aiprofanityfilter.Profanity;
import net.bytecore.aiprofanityfilter.managers.ProfanityManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProfanityGUI implements Listener {
    private static final int ITEMS_PER_PAGE = 45;

    public ProfanityGUI(Profanity main) {
        Bukkit.getPluginManager().registerEvents(this, main);
    }

    public static void openGUI(Player player, int page) {
        Set<String> words = ProfanityManager.getInstance().getProfaneWords();
        int totalPages = (int) Math.ceil((double) words.size() / ITEMS_PER_PAGE);

        Inventory inventory = Bukkit.createInventory(null, 54, "Profane Words - Page " + (page + 1));

        List<String> wordList = new ArrayList<>(words);
        int start = page * ITEMS_PER_PAGE;
        int end = Math.min(start + ITEMS_PER_PAGE, words.size());

        for (int i = start; i < end; i++) {
            String word = wordList.get(i);
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + word);
            item.setItemMeta(meta);
            inventory.addItem(item);
        }

        if (page > 0) {
            ItemStack prevPage = new ItemStack(Material.ARROW);
            ItemMeta prevMeta = prevPage.getItemMeta();
            prevMeta.setDisplayName(ChatColor.YELLOW + "Previous Page");
            prevPage.setItemMeta(prevMeta);
            inventory.setItem(45, prevPage);
        }

        if (page < totalPages - 1) {
            ItemStack nextPage = new ItemStack(Material.ARROW);
            ItemMeta nextMeta = nextPage.getItemMeta();
            nextMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
            nextPage.setItemMeta(nextMeta);
            inventory.setItem(53, nextPage);
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        InventoryView inventoryView = event.getView();

        if (inventoryView.getTitle().startsWith("Profane Words - Page ")) {
            event.setCancelled(true);

            int currentPage = Integer.parseInt(inventoryView.getTitle().split(" ")[3]) - 1;

            if (event.getCurrentItem() == null) return;

            if (event.getCurrentItem().getType() == Material.ARROW) {
                if (event.getSlot() == 45) {
                    openGUI(player, currentPage - 1);
                } else if (event.getSlot() == 53) {
                    openGUI(player, currentPage + 1);
                }
            }
        }
    }
}
