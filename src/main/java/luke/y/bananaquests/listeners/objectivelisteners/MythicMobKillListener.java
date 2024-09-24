package luke.y.bananaquests.listeners.objectivelisteners;

import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.KillMobObjective;
import luke.y.bananaquests.objective.KillMythicMobObjective;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MythicMobKillListener implements Listener {
    @EventHandler
    public void onMobKill(MythicMobDeathEvent e) {
        Player killer = (Player) e.getKiller();
        if (killer == null)
            return;
        for (ActiveQuest activeQuest : BananaQuests.getOngoingQuests(killer)) {
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (!questObjective.getClass().equals(KillMythicMobObjective.class))
                    continue;
                if (questObjective.isFinished())
                    continue;
                if (((KillMythicMobObjective) questObjective).getMythicMob().equals(e.getMobType())) {
                    questObjective.increaseProgress(1, killer);
                }
            }
        }
    }
}
