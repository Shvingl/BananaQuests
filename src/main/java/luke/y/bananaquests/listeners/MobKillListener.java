package luke.y.bananaquests.listeners;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.KillMobObjective;
import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobKillListener implements Listener {
    @EventHandler
    public void onMobKill(EntityDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;
        for (ActiveQuest activeQuest : BananaQuests.activeQuestsMap.get(killer)) {
            if (activeQuest.isFinished())
                return;
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (!questObjective.getClass().equals(KillMobObjective.class))
                    continue;
                if (questObjective.isFinished())
                    continue;
                if (((KillMobObjective) questObjective).getMob().equals(e.getEntity().getType())) {
                    questObjective.increaseProgress(1, killer);
                }
            }
        }
    }
}
