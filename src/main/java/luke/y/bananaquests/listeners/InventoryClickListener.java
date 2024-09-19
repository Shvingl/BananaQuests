package luke.y.bananaquests.listeners;

import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.util.Gui;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onGuiClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String title = ChatColor.stripColor(e.getView().getTitle());
        if (!(title.equalsIgnoreCase(Gui.activeTitle) || title.equalsIgnoreCase(Gui.finishedTitle) || title.equalsIgnoreCase(Gui.unstartedTitle)))
            return;
        if (e.getCurrentItem() == null)
            return;
        switch (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName())) {
            case Gui.activeTitle:
                Gui.openActiveQuestsGUI(player);
                break;
            case Gui.finishedTitle:
                Gui.openFinishedQuestsGUI(player);
                break;
            case Gui.unstartedTitle:
                Gui.openUnstartedQuestsGUI(player);
                break;
            default:
                break;
        }
        BananaQuests.trackedQuestMap.put(player, BananaQuests.getOngoingQuests(player).get(e.getSlot()));
        player.playSound(player, Sound.ITEM_BOOK_PAGE_TURN, 1, 1);
        Gui.openActiveQuestsGUI(player);
        e.setCancelled(true);
    }
}
