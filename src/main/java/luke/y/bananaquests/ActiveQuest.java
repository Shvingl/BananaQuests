package luke.y.bananaquests;

import luke.y.bananaquests.objective.QuestObjective;

import java.util.ArrayList;

/**
 * This class represents an ongoin/finished quest.
 * Belongs to a specific player's quest list.
 */
public class ActiveQuest {
    private int stage;
    private ArrayList<QuestObjective> currentObjectives;

    public void ActiveQuest(String id, int stage, ArrayList<QuestObjective> currentObjectives) {
        this.stage = stage;
        this.currentObjectives = currentObjectives;
    }

    public void checkObjectives() {
        int finishedObjectives = 0;
        for (QuestObjective objective : currentObjectives) {
            if (objective.isFinished() == true)
                finishedObjectives++;
        }
        if (finishedObjectives >= currentObjectives.size()) {
            //All objectives are finished -> move to the next stage
            currentObjectives.clear();
            stage++;
        }
    }
}
