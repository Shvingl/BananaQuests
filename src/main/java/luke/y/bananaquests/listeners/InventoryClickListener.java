package luke.y.bananaquests.listeners;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.util.Gui;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Locale;
import java.util.Random;

public class InventoryClickListener implements Listener {
    Random random = new Random();
    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (!(title.contains(Gui.activeTitle) || title.contains(Gui.finishedTitle) || title.contains(Gui.unstartedTitle)))
            return;
        if (e.getCurrentItem() == null)
            return;
        e.setCancelled(true);
        switch (e.getSlot()) {
            case 17:
                Gui.openActiveQuestsGUI(player);
                player.playSound(player, Sound.ITEM_BOOK_PUT, 1, 1);
                return;
            case 17+9:
                Gui.openFinishedQuestsGUI(player);
                player.playSound(player, Sound.ITEM_BOOK_PUT, 1, 1);
                return;
            case 17+9*2:
                Gui.openUnstartedQuestsGUI(player);
                player.playSound(player, Sound.ITEM_BOOK_PUT, 1, 1);
                return;
            default:
                break;
        }
        if (title.contains(Gui.activeTitle)) {
            int questIndex = (int) (e.getSlot() - Math.floor((double)e.getSlot()/9));
            ActiveQuest clickedQuest = BananaQuests.getOngoingQuests(player).get(questIndex);
            if (BananaQuests.trackedQuestMap.get(player) == clickedQuest) {
                player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 0.7f);
                BananaQuests.trackedQuestMap.put(player, null);
            }
            else {
                player.playSound(player, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                BananaQuests.trackedQuestMap.put(player, clickedQuest);
            }
            Gui.openActiveQuestsGUI(player);
        }
    }
}
