package luke.y.bananaquests.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Gui {

    public static void openActiveQuestsGUI(Player player) {
        player.openInventory(createShell(player));
    }

    private static Inventory createShell(Player player) {
        Inventory gui = Bukkit.createInventory(player, 9*5, ChatColor.GRAY + "Quests");
        ItemStack emptyButton = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta emptyButtonMeta = emptyButton.getItemMeta();
        emptyButtonMeta.setDisplayName("");
        emptyButton.setItemMeta(emptyButtonMeta);
        ItemStack activeButton = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack finishedButton = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemStack unstartedButton = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta activeButtonMeta = activeButton.getItemMeta();
        ItemMeta finishedButtonMeta = finishedButton.getItemMeta();
        ItemMeta unstartedButtonMeta = unstartedButton.getItemMeta();
        activeButtonMeta.setDisplayName("Aktivní Questy");
        finishedButtonMeta.setDisplayName("Splněné Questy");
        unstartedButtonMeta.setDisplayName("Nezapočaté Questy");
        activeButton.setItemMeta(activeButtonMeta);
        finishedButton.setItemMeta(finishedButtonMeta);
        unstartedButton.setItemMeta(unstartedButtonMeta);

        gui.setItem(9-1, emptyButton);
        gui.setItem(9*2-1, activeButton);
        gui.setItem(9*3-1, finishedButton);
        gui.setItem(9*4-1, unstartedButton);
        gui.setItem(9*5-1, emptyButton);
        return gui;
    }
}
