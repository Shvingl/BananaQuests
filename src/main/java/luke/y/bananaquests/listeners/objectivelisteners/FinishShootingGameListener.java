package luke.y.bananaquests.listeners.objectivelisteners;

import luke.y.bananaquests.ActiveQuest;
import luke.y.bananaquests.BananaQuests;
import luke.y.bananaquests.objective.FinishShootingGameObjective;
import luke.y.bananaquests.QuestObjective;
import luke.y.shootingGame.events.FinishShootingGameEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class FinishShootingGameListener implements Listener {
    @EventHandler
    public void onOutpostFree(FinishShootingGameEvent e) {
        Player player = e.getPlayer();
        if (player == null)
            return;
        for (ActiveQuest activeQuest : BananaQuests.getOngoingQuests(player)) {
            for (QuestObjective questObjective : activeQuest.getCurrentObjectives()) {
                if (!questObjective.getClass().equals(FinishShootingGameObjective.class))
                    continue;
                if (questObjective.isFinished())
                    continue;
                if (questObjective.getGoal() <= e.getScore()) {
                    questObjective.increaseProgress(questObjective.getGoal(), player);
                }
            }
        }
    }
}
