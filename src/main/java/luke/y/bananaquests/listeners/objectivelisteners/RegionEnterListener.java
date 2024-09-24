package luke.y.bananaquests.listeners.objectivelisteners;

import de.netzkronehd.wgregionevents.events.RegionEnterEvent;
import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.EnterRegionObjective;
import luke.y.bananaquests.objective.FreeOutpostObjective;
import luke.y.bananaquests.objective.QuestObjective;
import luke.y.outpost.events.OutpostFreeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class RegionEnterListener implements Listener {
    @EventHandler
    public void onOutpostFree(RegionEnterEvent e) {
        Player player = e.getPlayer();
        for (ActiveQuest activeQuest : BananaQuests.getOngoingQuests(player)) {
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (!questObjective.getClass().equals(EnterRegionObjective.class))
                    continue;
                if (questObjective.isFinished())
                    continue;
                if (((EnterRegionObjective) questObjective).getRegionID().equals(e.getRegion().getId())) {
                    questObjective.increaseProgress(1, player);
                }
            }
        }
    }
}
