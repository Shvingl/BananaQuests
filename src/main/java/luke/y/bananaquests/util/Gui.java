package luke.y.bananaquests.util;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Gui {

    public static final String activeTitle = "Aktivní Questy";
    public static final String finishedTitle = "Dokončené Questy";
    public static final String unstartedTitle = "Nezapočaté Questy";

    public static void openActiveQuestsGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*5, activeTitle);
        inventory.setContents(createShell(player).getStorageContents());
        ItemStack activeButton = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        ItemMeta activeButtonMeta = activeButton.getItemMeta();
        activeButtonMeta.addEnchant(Enchantment.MENDING, 1, true);
        activeButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        activeButtonMeta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + activeTitle.toUpperCase());
        activeButton.setItemMeta(activeButtonMeta);
        inventory.setItem(9*2-1, activeButton);

        ItemStack questItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta questItemMeta = questItem.getItemMeta();

        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            if (!activeQuest.isFinished()) {
                questItemMeta.setDisplayName(activeQuest.getDisplay());
                ArrayList<String> lore = new ArrayList<>();
                for (QuestObjective objective : activeQuest.getCurrentObjectives()) {
                    lore.add("- " + objective.getDescription() + " " + objective.getProgress() + "/" + objective.getGoal());
                }
                questItemMeta.setLore(lore);
                questItem.setItemMeta(questItemMeta);
                inventory.addItem(questItem);
            }
        }
        player.openInventory(inventory);
    }

    public static void openFinishedQuestsGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*5, finishedTitle);
        inventory.setContents(createShell(player).getStorageContents());
        ItemStack activeButton = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
        ItemMeta activeButtonMeta = activeButton.getItemMeta();
        activeButtonMeta.addEnchant(Enchantment.MENDING, 1, true);
        activeButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        activeButtonMeta.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + finishedTitle.toUpperCase());
        activeButton.setItemMeta(activeButtonMeta);
        inventory.setItem(9*3-1, activeButton);

        ItemStack questItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta questItemMeta = questItem.getItemMeta();

        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            if (activeQuest.isFinished()) {
                questItemMeta.setDisplayName(activeQuest.getDisplay());
                questItem.setItemMeta(questItemMeta);
                inventory.addItem(questItem);
            }
        }
        player.openInventory(inventory);
    }

    public static void openUnstartedQuestsGUI(Player player) {
        Inventory inventory = Bukkit.createInventory(player, 9*5, unstartedTitle);
        inventory.setContents(createShell(player).getStorageContents());
        ItemStack activeButton = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta activeButtonMeta = activeButton.getItemMeta();
        activeButtonMeta.addEnchant(Enchantment.MENDING, 1, true);
        activeButtonMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        activeButtonMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + unstartedTitle.toUpperCase());
        activeButton.setItemMeta(activeButtonMeta);
        inventory.setItem(9*4-1, activeButton);

        ItemStack questItem = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta questItemMeta = questItem.getItemMeta();

        Set<String> allQuestIDs = BananaQuests.validQuestIDs;
        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(player)) {
            allQuestIDs.remove(activeQuest.getId());
        }

        for (String questID : allQuestIDs) {
            questItemMeta.setDisplayName(BananaQuests.questConfigs.get(questID).getString("display"));
            ArrayList<String> lore = new ArrayList<>();
            lore.add(BananaQuests.questConfigs.get(questID).getString("hint"));
            questItemMeta.setLore(lore);
            questItem.setItemMeta(questItemMeta);
            inventory.addItem(questItem);
        }

        player.openInventory(inventory);
    }

    private static Inventory createShell(Player player) {
        Inventory gui = Bukkit.createInventory(player, 9*5);
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
        activeButtonMeta.setDisplayName(activeTitle);
        finishedButtonMeta.setDisplayName(finishedTitle);
        unstartedButtonMeta.setDisplayName(unstartedTitle);
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
