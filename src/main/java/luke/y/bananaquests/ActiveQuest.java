package luke.y.bananaquests;

import luke.y.bananaquests.objective.QuestObjective;
import org.bukkit.entity.Player;

import java.util.ArrayList;

/**
 * This class represents an ongoin/finished quest.
 * Must belong to a specific online player's quest list.
 */
public class ActiveQuest {
    private Player owner;

    public Player getOwner() {
        return owner;
    }

    private String display;

    public String getDisplay() {
        return display;
    }

    private int stage;
    private ArrayList<QuestObjective> currentObjectives;

    public ArrayList<QuestObjective> getCurrentObjectives() {
        return currentObjectives;
    }

    private boolean finished;

    public boolean isFinished() {
        return finished;
    }

    public ActiveQuest(String id, Player owner, int stage, ArrayList<QuestObjective> currentObjectives) {
        this.stage = stage;
        this.owner = owner;
        this.currentObjectives = currentObjectives;

        this.display = BananaQuests.questConfigs.get(id).getString("display");

        for (QuestObjective questObjective : currentObjectives) {
            questObjective.setOwner(this);
        }
    }

    /**
     * This method checks if all objectives are finished and if so
     * increases the stage. Should be called each time an Active Quests'
     * objective's progress increases.
     */
    public void tryMoveToNextStage() {
        if (allObjectivesFinished())
            moveToNextStage();
    }

    private boolean allObjectivesFinished() {
        int finishedObjectives = 0;
        for (QuestObjective objective : currentObjectives) {
            if (objective.isFinished())
                finishedObjectives++;
        }
        return finishedObjectives >= currentObjectives.size();
    }

    private void moveToNextStage() {
        stage++;
        currentObjectives.clear();
        //currentObjectives.add();
    }
}
