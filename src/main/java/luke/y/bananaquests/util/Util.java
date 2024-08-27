package luke.y.bananaquests.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Util {
    public static void openQuestMenu(Player player) {

    }

    private Inventory createShell(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*5, ChatColor.GOLD + "Questy");
        inventory.setItem(17, new ItemStack(Material.GLASS_PANE));
        inventory.setItem(17 + 9, new ItemStack(Material.GLASS_PANE));
        inventory.setItem(17 + 9*2, new ItemStack(Material.GLASS_PANE));
        return inventory;
    }
}
