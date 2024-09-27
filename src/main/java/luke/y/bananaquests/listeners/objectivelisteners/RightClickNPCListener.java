package luke.y.bananaquests.listeners.objectivelisteners;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.items.MythicItem;
import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.GiveMythicItemToNPCObjective;
import luke.y.bananaquests.QuestObjective;
import luke.y.bananaquests.objective.RightClickNPCObjective;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class RightClickNPCListener implements Listener {
    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent e) {
        Player clicker = e.getClicker();
        for (ActiveQuest activeQuest : BananaQuests.getOngoingQuests(clicker)) {
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (questObjective.isFinished())
                    continue;
                if (!(questObjective.getClass().equals(RightClickNPCObjective.class) || questObjective.getClass().equals(GiveMythicItemToNPCObjective.class)) )
                    continue;
                if (questObjective.getClass().equals(RightClickNPCObjective.class)) {
                    if (((RightClickNPCObjective) questObjective).getNpcID() == (e.getNPC().getId())) {
                        questObjective.increaseProgress(1, clicker);
                    }
                }
                else {
                    if (((GiveMythicItemToNPCObjective) questObjective).getNpcID() == (e.getNPC().getId())) {
                        ItemStack itemInHand = clicker.getInventory().getItemInMainHand();
                        MythicItem mythicItem = ((GiveMythicItemToNPCObjective) questObjective).getMythicItem();
                        if (MythicBukkit.inst().getItemManager().isMythicItem(itemInHand)) {
                            //clicker.sendMessage("It is a mythic item");
                            if (MythicBukkit.inst().getItemManager().getMythicTypeFromItem(itemInHand).equals(mythicItem.getMythicTypeOverride())) {
                                //clicker.sendMessage("Je to ten item.");
                                while (questObjective.getProgress() < questObjective.getGoal() && itemInHand.getAmount() > 0) {
                                    itemInHand.setAmount(itemInHand.getAmount() - 1);
                                    questObjective.increaseProgress(1, clicker);
                                }
                                clicker.playSound(clicker, Sound.ITEM_ARMOR_EQUIP_GENERIC, 1, 1);
                            }
                        }
                    }
                }
            }
        }
    }
}
