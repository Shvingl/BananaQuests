package luke.y.bananaquests;

import luke.y.bananaquests.objective.KillMythicMobObjective;
import luke.y.bananaquests.objective.QuestObjective;

import java.util.ArrayList;

/**
 * This class represents an ongoin/finished quest.
 * Must belong to a specific online player's quest list.
 */
public class ActiveQuest {
    private int stage;
    private ArrayList<QuestObjective> currentObjectives;

    public ActiveQuest(String id, int stage, ArrayList<QuestObjective> currentObjectives) {
        this.stage = stage;
        this.currentObjectives = currentObjectives;
    }

    private boolean allObjectivesFinished() {
        int finishedObjectives = 0;
        for (QuestObjective objective : currentObjectives) {
            if (objective.isFinished())
                finishedObjectives++;
        }
        if (finishedObjectives >= currentObjectives.size()) {
            return true;
        }
        return false;
    }

    private void moveToNextStage() {
        stage++;
        currentObjectives.clear();
        //currentObjectives.add();
    }
}
