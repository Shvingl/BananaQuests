package luke.y.bananaquests.listeners.objectivelisteners;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.FreeOutpostObjective;
import luke.y.bananaquests.QuestObjective;
import luke.y.outpost.events.OutpostFreeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OutpostFreeListener implements Listener {
    @EventHandler
    public void onOutpostFree(OutpostFreeEvent e) {
        Player player = e.getPlayer();
        if (player == null)
            return;
        for (ActiveQuest activeQuest : BananaQuests.getOngoingQuests(player)) {
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (!questObjective.getClass().equals(FreeOutpostObjective.class))
                    continue;
                if (questObjective.isFinished())
                    continue;
                if (((FreeOutpostObjective) questObjective).getOutpostID().equals(e.getOutpostID())) {
                    questObjective.increaseProgress(1, player);
                }
            }
        }
    }
}
